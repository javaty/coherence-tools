package com.seovic.coherence.util;


import com.seovic.coherence.util.processor.MethodInvocationProcessor;

import com.tangosol.net.NamedCache;
import com.tangosol.net.CacheFactory;


/**
 * @author Aleksandar Seovic  2011.03.30
 */
@SuppressWarnings({"unchecked"})
public class RemoteObject<T> {
    private final NamedCache cache;
    private final Object key;

    public RemoteObject(String cacheName, Object key) {
        this(CacheFactory.getCache(cacheName), key);
    }

    public RemoteObject(NamedCache cache, Object key) {
        this.cache = cache;
        this.key   = key;
    }

    public T get() {
        return (T) cache.get(key);
    }

    public void put(T obj) {
        cache.put(key, obj);
    }

    public boolean exists() {
        return get() != null;
    }
    
    public Object invoke(String methodName, Object... args) {
        return cache.invoke(key, new MethodInvocationProcessor(methodName, true, args));
    }
}
