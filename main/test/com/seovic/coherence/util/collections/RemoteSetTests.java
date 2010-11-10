package com.seovic.coherence.util.collections;

import com.tangosol.net.NamedCache;
import com.tangosol.net.CacheFactory;
import com.seovic.core.factory.TreeSetFactory;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.util.Arrays;
import java.util.Iterator;


/**
 * @author Aleksandar Seovic  2010.11.08
 */
public class RemoteSetTests {
    private static final NamedCache cache = CacheFactory.getCache("test-sets");

    @Before
    public void setup() {
        cache.remove(1L);
    }

    @Test
    public void testCreation() {
        RemoteSet l = new RemoteSet(cache, 1L);
        assertEquals(0, l.size());
        assertTrue(l.isEmpty());
    }

    @Test
    public void testAdditionAndRemoval() {
        RemoteSet<String> l = new RemoteSet<String>(cache, 1L);
        assertEquals(0, l.size());

        l.add("one");
        l.add("two");
        l.add("three");
        assertEquals(3, l.size());
        assertFalse(l.isEmpty());
        assertTrue(l.contains("one"));

        l.remove("one");
        assertEquals(2, l.size());
        assertFalse(l.contains("one"));

        l.clear();
        assertTrue(l.isEmpty());
    }

    @Test
    public void testAdditionAndRemovalWithPrimitives() {
        RemoteSet<Long> l = new RemoteSet<Long>("test-sets", 1L);
        assertEquals(0, l.size());

        l.add(0L);
        l.add(10L);
        l.add(200L);
        l.add(3000L);
        assertEquals(4, l.size());
        assertFalse(l.isEmpty());
        assertTrue(l.contains(10L));

        l.remove(10L);
        assertEquals(3, l.size());
        assertFalse(l.contains(10L));

        l.clear();
        assertTrue(l.isEmpty());
    }

    @Test
    public void testBulkAdditionAndRemoval() {
        RemoteSet<String> l = new RemoteSet<String>(cache, 1L);
        assertEquals(0, l.size());

        l.addAll(Arrays.asList("one", "two", "two", "three", "three", "four", "five"));
        assertEquals(5, l.size());
        assertFalse(l.isEmpty());
        assertTrue(l.containsAll(Arrays.asList("one", "three", "five")));

        l.retainAll(Arrays.asList("one", "two", "three"));
        assertEquals(3, l.size());
        assertFalse(l.containsAll(Arrays.asList("one", "three", "five")));

        l.removeAll(Arrays.asList("one", "three"));
        assertEquals(1, l.size());
        assertTrue(l.contains("two"));

        l.clear();
        assertTrue(l.isEmpty());
    }

    @Test
    public void testBulkAdditionAndRemovalWithPrimitives() {
        RemoteSet<Integer> l = new RemoteSet<Integer>("test-sets", 1L);
        assertEquals(0, l.size());

        l.addAll(Arrays.asList(0, 1, 1, 2, 2, 3, 3, 4, 5));
        assertEquals(6, l.size());
        assertFalse(l.isEmpty());
        assertTrue(l.containsAll(Arrays.asList(1, 3, 5)));

        l.retainAll(Arrays.asList(1, 2, 3));
        assertEquals(3, l.size());
        assertFalse(l.containsAll(Arrays.asList(1, 3, 5)));

        l.removeAll(Arrays.asList(1, 3));
        assertEquals(1, l.size());
        assertTrue(l.contains(2));

        l.clear();
        assertTrue(l.isEmpty());
    }

    @Test
    public void testToArray() {
        RemoteSet<Integer> l = new RemoteSet<Integer>(cache, 1L, new TreeSetFactory<Integer>());

        l.addAll(Arrays.asList(0, 1, 2));
        assertTrue(Arrays.equals(new Object[] {0, 1, 2}, l.toArray()));
        assertTrue(Arrays.equals(new Integer[] {0, 1, 2}, l.toArray(new Integer[3])));
    }

    @Test
    public void testIterator() {
        RemoteSet<String> l = new RemoteSet<String>(cache, 1L);

        l.addAll(Arrays.asList("one", "two", "three"));
        for (Iterator<String> it = l.iterator(); it.hasNext(); ) {
            it.next();
            it.remove();
        }
        assertTrue(l.isEmpty());
    }
}