package com.seovic.core;


import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;


/**
 * @author Aleksandar Seovic  2010.10.11
 */
public class PropertyListTests {
    @Test
    public void testFromString() {
        PropertyList pl = PropertyList.fromString("name,address:(city,state),orders:(date,lineItems:(sku,price),amount)");

        List<PropertySpec> l = pl.getProperties();
        assertEquals(3, l.size());

        assertEquals("name", l.get(0).getName());
        assertNull(l.get(0).getPropertyList());

        assertEquals("address", l.get(1).getName());
        assertNotNull(l.get(1).getPropertyList());

        assertEquals("orders", l.get(2).getName());
        assertNotNull(l.get(2).getPropertyList());

        PropertySpec lineItems = l.get(2).getPropertyList().getProperties().get(1);
        assertEquals("lineItems", lineItems.getName());
        assertNotNull(lineItems.getPropertyList());
    }
}
