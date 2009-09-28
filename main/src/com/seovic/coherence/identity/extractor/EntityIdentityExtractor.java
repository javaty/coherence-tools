package com.seovic.coherence.identity.extractor;

import com.seovic.coherence.identity.IdentityExtractor;
import com.seovic.lang.Entity;


/**
 * @author ic  2009.06.09
 */
public class EntityIdentityExtractor<T> implements IdentityExtractor<T, Entity<T>>{
    public T extractIdentity(Entity<T> entity) {
        return entity.getId();
    }
}
