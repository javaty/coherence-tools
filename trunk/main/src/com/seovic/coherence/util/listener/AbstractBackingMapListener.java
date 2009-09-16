package com.seovic.coherence.util.listener;


import com.tangosol.util.AbstractMapListener;
import com.tangosol.util.MapEvent;
import com.tangosol.util.ConverterCollections;
import com.tangosol.util.Binary;
import com.tangosol.util.ExternalizableHelper;

import com.tangosol.net.BackingMapManagerContext;

import com.tangosol.net.cache.CacheEvent;


/**
 * Abstract base class for backing map listeners.
 *
 * @author Aleksandar Seovic  2009.02.27
 */
public abstract class AbstractBackingMapListener
        extends AbstractMapListener {

    private BackingMapManagerContext context;

    protected AbstractBackingMapListener() {
    }

    protected AbstractBackingMapListener(
            BackingMapManagerContext context) {
        this.context = context;
    }

    protected BackingMapManagerContext getContext() {
        return context;
    }

    protected void setContext(BackingMapManagerContext context) {
        this.context = context;
    }

    protected MapEvent convertFromInternal(MapEvent event) {
        return ConverterCollections.getMapEvent(
                event.getMap(),
                event,
                context.getKeyFromInternalConverter(),
                context.getValueFromInternalConverter());
    }

    protected Object getKey(MapEvent event) {
        return context.getKeyFromInternalConverter()
                .convert(event.getKey());
    }

    protected Object getOldValue(MapEvent event) {
        return context.getValueFromInternalConverter()
                .convert(event.getOldValue());
    }

    protected Object getNewValue(MapEvent event) {
        return context.getValueFromInternalConverter()
                .convert(event.getNewValue());
    }

    protected boolean isEviction(MapEvent event) {
        return context.isKeyOwned(event.getKey())
                && event instanceof CacheEvent
                && ((CacheEvent) event).isSynthetic();
    }

    protected boolean isEntryWritePending(MapEvent event) {
    	if (event.getId() == MapEvent.ENTRY_DELETED || !context.isKeyOwned(event.getKey())) {
    		return false;
    	}
    	Object newValue = event.getNewValue();
    	return newValue instanceof Binary && isEntryValueNotStoredYet(context, (Binary) newValue );
    }

    protected boolean isDistribution(MapEvent event) {
        return !context.isKeyOwned(event.getKey());
    }

    protected static boolean isEntryValueNotStoredYet(BackingMapManagerContext context, Binary binaryValue) {
    	return context.isInternalValueDecorated(binaryValue, ExternalizableHelper.DECO_STORE);
    }
}
