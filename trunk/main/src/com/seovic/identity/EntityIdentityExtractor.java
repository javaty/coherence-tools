package com.seovic.identity;


import com.seovic.core.Entity;

import java.io.Serializable;


/**
 * An {@link IdentityExtractor} that can be used with the classes implementing
 * {@link Entity} interface.
 *
 * @author ic  2009.06.09
 */
public class EntityIdentityExtractor<T>
        implements IdentityExtractor<T, Entity<T>>, Serializable
    {
    /**
     * {@inheritDoc}
     */
    public T extractIdentity(Entity<T> entity)
        {
        return entity.getId();
        }
    }
