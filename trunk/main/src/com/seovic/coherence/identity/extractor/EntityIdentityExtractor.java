package com.seovic.coherence.identity.extractor;


import com.seovic.coherence.identity.IdentityExtractor;
import com.seovic.core.Entity;


/**
 * An {@link IdentityExtractor} that can be used with the classes implementing
 * {@link Entity} interface.
 *
 * @author ic  2009.06.09
 */
public class EntityIdentityExtractor<T>
        implements IdentityExtractor<T, Entity<T>>
    {
    /**
     * {@inheritDoc}
     */
    public T extractIdentity(Entity<T> entity)
        {
        return entity.getId();
        }
    }
