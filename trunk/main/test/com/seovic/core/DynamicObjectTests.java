package com.seovic.core;


import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Date;

import com.seovic.test.objects.Person;
import com.seovic.test.objects.Address;


/**
 * @author Aleksandar Seovic  2010.10.11
 */
@SuppressWarnings({"deprecation"})
public class DynamicObjectTests {
    @Test
    public void testConstructionFromBean() {
        Date dob = new Date();
        Person p = new Person(1L, "Homer", dob, new Address("111 Main St", "Springfield", "USA"));
        DynamicObject o = new DynamicObject(p);

        assertEquals(1L, o.getLong("id"));
        assertEquals("Homer", o.getString("name"));
        assertEquals(dob, o.getDate("dob"));
        assertEquals(p.getAddress(), o.getValue("address"));
    }

    @Test
    public void testConstructionFromBeanWithPropertyList() {
        Person p = new Person(1L, "Homer", null, new Address("111 Main St", "Springfield", "USA"));
        DynamicObject o = new DynamicObject(p, PropertyList.fromString("name,address:(city,country)"));

        assertNull(o.getDate("dob"));
        assertEquals("Homer", o.getString("name"));

        DynamicObject address = (DynamicObject) o.getValue("address");
        assertEquals("Springfield", address.getString("city"));
        assertEquals("USA", address.getString("country"));
    }

    @Test
    public void testUpdate() {
        Person p = new Person(1L, "Homer", null, new Address("111 Main St", "Springfield", "USA"));

        DynamicObject address = new DynamicObject();
        address.setValue("street", "555 Main St");

        DynamicObject o = new DynamicObject();
        o.setValue("id", "5");
        o.setValue("name", "Homer Simpson");
        o.setValue("dob", "2010-10-10");
        o.setValue("address", address);

        o.update(p);

        assertEquals(5L, p.getId());
        assertEquals("Homer Simpson", p.getName());
        assertEquals(new Date(110, 9, 10), p.getDob());
        assertEquals("555 Main St", p.getAddress().getStreet());
    }
}