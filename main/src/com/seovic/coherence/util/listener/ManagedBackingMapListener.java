package com.seovic.coherence.util.listener;


import com.tangosol.net.BackingMapManagerContext;


/**
 * Base class for Spring-managed backing map listeners.
 *
 * @author Aleksandar Seovic  2009.06.30
 */
public class ManagedBackingMapListener<K,V> extends AbstractBackingMapListener<K,V> {

    public ManagedBackingMapListener() {
    }

    public ManagedBackingMapListener(BackingMapManagerContext context) {
        super(context);
    }

    /**
     * Set backing map manager context for this listener.
     *
     * @param context  backing map manager context
     */
    public void setContext(BackingMapManagerContext context) {
        super.setContext(context);
    }
}
