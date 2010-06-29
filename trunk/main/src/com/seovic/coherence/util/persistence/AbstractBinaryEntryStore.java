package com.seovic.coherence.util.persistence;


import com.tangosol.net.cache.BinaryEntryStore;
import com.tangosol.util.BinaryEntry;
import java.util.Set;


/**
 * @author Aleksandar Seovic  2010.06.29
 */
@SuppressWarnings({"unchecked"})
public abstract class AbstractBinaryEntryStore implements BinaryEntryStore {
    @Override
    public void load(BinaryEntry entry) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void loadAll(Set set) {
        for (BinaryEntry entry : (Set<BinaryEntry>) set) {
            load(entry);
        }
    }

    @Override
    public void store(BinaryEntry entry) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void storeAll(Set set) {
        for (BinaryEntry entry : (Set<BinaryEntry>) set) {
            store(entry);
        }
    }

    @Override
    public void erase(BinaryEntry entry) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void eraseAll(Set set) {
        for (BinaryEntry entry : (Set<BinaryEntry>) set) {
            erase(entry);
        }
    }
}
