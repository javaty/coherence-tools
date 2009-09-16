package com.seovic.coherence.util.listener;


import com.tangosol.net.BackingMapManagerContext;

import com.tangosol.util.MapEvent;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Configurable backing map listener that uses Spring to configure
 * cache-specific listeners that should handle map events.
 * <p/>
 * By default, listener definitions are loaded from the
 * <tt>backing-map-listener-context.xml</tt> configuration file, which should be
 * located in the classpath. Each cache-specific listener defined within that
 * file should extend {@link ManagedBackingMapListener} class.
 * 
 * @author Aleksandar Seovic  2009.06.30
 */
public class ConfigurableBackingMapListener
        extends AbstractBackingMapListener {
    // ---- configuration context -------------------------------------------

    private static final ApplicationContext ctx =
            new ClassPathXmlApplicationContext("backing-map-listener-context.xml");


    // ---- data members ----------------------------------------------------

    private ManagedBackingMapListener delegate;


    // ---- constructors ----------------------------------------------------

    /**
     * Construct <tt>ConfigurableBackingMapListener</tt> instance.
     *
     * @param context    backing map manager context
     * @param cacheName  name of the cache to set up listener for
     */
    public ConfigurableBackingMapListener(BackingMapManagerContext context, String cacheName) {
        super(context);
        delegate = ctx.containsBean(cacheName)
                   ? (ManagedBackingMapListener) ctx.getBean(cacheName)
                   : new ManagedBackingMapListener();
        delegate.setContext(getContext());
    }

    /**
     * {@inheritDoc}
     */
    public void entryInserted(MapEvent event) {
        delegate.entryInserted(event);
    }

    /**
     * {@inheritDoc}
     */
    public void entryUpdated(MapEvent event) {
        delegate.entryUpdated(event);
    }

    /**
     * {@inheritDoc}
     */
    public void entryDeleted(MapEvent event) {
        delegate.entryDeleted(event);
    }
}
