package com.seovic.coherence.test;

import com.tangosol.net.DefaultConfigurableCacheFactory;

import java.lang.annotation.*;

/**
 * An annotation that can be used to decorate Coherence test classes in order
 * to specify test configuration parameters, such as the number of cache
 * servers to start, the amount of memory each cache server should use, and
 * the cache configuration file.
 *
 * @author Aleksandar Seovic  2008.12.27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
@Inherited
public @interface CoherenceTest {
    int    servers() default 2;
    String memory()  default "64m";
    String config()  default DefaultConfigurableCacheFactory.FILE_CFG_CACHE;
}
