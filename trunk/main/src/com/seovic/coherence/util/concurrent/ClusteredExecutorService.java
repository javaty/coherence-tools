/*
 * Copyright 2009 Aleksandar Seovic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.seovic.coherence.util.concurrent;


import org.springframework.core.task.TaskExecutor;

import com.tangosol.net.InvocationService;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.MemberListener;
import com.tangosol.net.Member;
import com.tangosol.net.AbstractInvocable;
import com.tangosol.net.MemberEvent;
import com.tangosol.net.InvocationObserver;

import com.tangosol.util.Base;

import java.util.concurrent.Executor;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.CountDownLatch;

import java.util.Set;
import java.util.Iterator;
import java.util.Collections;
import java.util.List;

import java.io.Serializable;


/**
 * ExecutorService implementation that parallelizes task execution across the
 * cluster nodes using Invocation Service.
 * 
 * @author Aleksandar Seovic  2009.11.02
 */
@SuppressWarnings({"unchecked"})
public class ClusteredExecutorService
        extends AbstractExecutorService
        implements TaskExecutor, Executor, MemberListener
    {
    // ---- constructors and initializers -----------------------------------

    /**
     * Construct <tt>ClusteredExecutorService</tt> instance using default
     * invocation service ("InvocationService").
     */
    public ClusteredExecutorService()
        {
        this("InvocationService");
        }

    /**
     * Construct <tt>ClusteredExecutorService</tt> instance.
     *
     * @param invocationServiceName  the name of the invocation service to use
     */
    public ClusteredExecutorService(String invocationServiceName)
        {
        m_invocationServiceName = invocationServiceName;
        initialize();
        }

    /**
     * Initialize this executor service.
     */
    protected synchronized void initialize()
        {
        String invocationServiceName = m_invocationServiceName;
        m_invocationService = (InvocationService)
                CacheFactory.getService(invocationServiceName);
        if (m_invocationService == null)
            {
            throw new IllegalArgumentException("Invocation service ["
                   + invocationServiceName + "] is not defined.");
            }

        m_serviceMembers = m_invocationService.getInfo().getServiceMembers();
        m_memberIterator = m_serviceMembers.iterator();
        m_invocationService.addMemberListener(this);
        }


    // ---- Executor implementation -----------------------------------------

    /**
     * Executes the given command at some time in the future.
     *
     * @param command  the runnable task
     */
    public void execute(Runnable command)
        {
        if (!(command instanceof ClusteredFutureTask))
            {
            command = new ClusteredFutureTask<Object>(command, null);
            }
        command.run();
        }


    // ---- ExecutorService implementation ----------------------------------

    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value)
        {
        return new ClusteredFutureTask<T>(runnable, value);
        }

    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable)
        {
        return new ClusteredFutureTask<T>(callable);
        }

    public void shutdown()
        {
        }

    public List<Runnable> shutdownNow()
        {
        return null;
        }

    public boolean isShutdown()
        {
        return false;
        }

    public boolean isTerminated()
        {
        return false;
        }

    public boolean awaitTermination(long l, TimeUnit timeUnit)
            throws InterruptedException
        {
        return false;
        }


    // ---- MemberListener implementation -----------------------------------

    public void memberJoined(MemberEvent memberEvent)
        {
        addMember(memberEvent.getMember());
        }

    public void memberLeaving(MemberEvent memberEvent)
        {
        removeMember(memberEvent.getMember());
        }

    public void memberLeft(MemberEvent memberEvent)
        {
        removeMember(memberEvent.getMember());
        }


    // ---- helper methods --------------------------------------------------

    /**
     * Return the member that should execute submitted command.
     *
     * @return the member to execute submitted command on
     */
    protected synchronized Member getExecutionMember()
        {
        Iterator<Member> it = m_memberIterator;
        if (it == null || !it.hasNext())
            {
            m_memberIterator = it = m_serviceMembers.iterator();
            }
        return it.next();
        }

    /**
     * Add member.
     *
     * @param member  member to add
     */
    protected synchronized void addMember(Member member)
        {
        m_serviceMembers.add(member);
        m_memberIterator = m_serviceMembers.iterator();
        }

    /**
     * Remove member.
     * 
     * @param member  member to remove
     */
    protected synchronized void removeMember(Member member)
        {
        m_serviceMembers.remove(member);
        m_memberIterator = m_serviceMembers.iterator();
        }


    // ---- inner class: CallableAdapter ------------------------------------

    private static class CallableAdapter<T>
            implements Callable<T>, Serializable
        {
        // ---- constructors --------------------------------------------

        private CallableAdapter(Runnable runnable, T result)
            {
            m_runnable = runnable;
            m_result = result;
            }

        // ---- Callable implementation ---------------------------------

        public T call() throws Exception
            {
            m_runnable.run();
            return m_result;
            }

        // ---- data members --------------------------------------------

        private final Runnable m_runnable;
        private final T m_result;
        }


    // ---- inner class: InvocableAdapter -----------------------------------

    private static class InvocableAdapter<T>
            extends AbstractInvocable
            implements Serializable
        {
        public InvocableAdapter(Callable<T> callable)
            {
            m_callable = callable;
            }

        public void run()
            {
            try
                {
                m_result = m_callable.call();
                }
            catch (Exception e)
                {
                throw Base.ensureRuntimeException(e);
                }
            }

        public Object getResult()
            {
            return m_result;
            }

        private final Callable<T> m_callable;
        private volatile T m_result;
        }


    // ---- inner class: InvocableAdapter -----------------------------------

    private class ClusteredFutureTask<T>
            implements RunnableFuture<T>, InvocationObserver
        {
        // ---- constructors --------------------------------------------

        public ClusteredFutureTask(Callable<T> callable)
            {
            m_callable = callable;
            m_latch    = new CountDownLatch(1);
            }

        public ClusteredFutureTask(Runnable runnable, T result)
            {
            this(new CallableAdapter<T>(runnable, result));
            }

        // ---- RunnableFuture implementation ---------------------------

        public void run()
            {
            m_invocationService.execute(
                    new InvocableAdapter(m_callable),
                    Collections.singleton(getExecutionMember()),
                    this);
            }

        public boolean cancel(boolean b)
            {
            return false;
            }

        public boolean isCancelled()
            {
            return false;
            }

        public boolean isDone()
            {
            return m_fDone;
            }

        public T get()
                throws InterruptedException, ExecutionException
            {
            m_latch.await();
            return getInternal();
            }

        public T get(long l, TimeUnit timeUnit)
                throws InterruptedException, ExecutionException, TimeoutException
            {
            if (!m_latch.await(l, timeUnit))
                {
                throw new TimeoutException();
                }

            return getInternal();
            }

        protected T getInternal()
                throws ExecutionException
            {
            if (m_exception != null)
                {
                throw new ExecutionException(m_exception);
                }
            return m_result;
            }

        // ---- InvocationObserver implementation -----------------------

        public void memberCompleted(Member member, Object result)
            {
            m_result = (T) result;
            m_fDone  = true;
            m_latch.countDown();
            }

        public void memberFailed(Member member, Throwable throwable)
            {
            m_exception = throwable;
            m_fDone     = true;
            m_latch.countDown();
            }

        public void memberLeft(Member member)
            {
            m_exception = new MemberLeftException(member);
            m_fDone     = true;
            m_latch.countDown();
            }

        public void invocationCompleted()
            {
            }

        // ---- data members --------------------------------------------

        private final Callable<T> m_callable;
        private final CountDownLatch m_latch;
        private volatile T m_result;
        private volatile Throwable m_exception;
        private volatile boolean m_fDone;
        }


    // ---- data members ----------------------------------------------------

    /**
     * The name of the invocation service to use.
     */
    private final String m_invocationServiceName;

    /**
     * The invocation service to use.
     */
    private volatile InvocationService m_invocationService;

    /**
     * A set of members that can be used to execute tasks.
     */
    private volatile Set<Member> m_serviceMembers;

    /**
     * Member set iterator.
     */
    private volatile Iterator<Member> m_memberIterator;
    }
