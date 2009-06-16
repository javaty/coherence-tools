package com.seovic.coherence.loader.properties;


import com.seovic.coherence.test.objects.Person;
import com.seovic.coherence.test.objects.City;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;


/**
 * Tests for {BeanWrapperPropertySetter}.
 *
 * @author ic  2009.06.16
 */
public class BeanWrapperPropertySetterTests
    {

    @Test(expected = RuntimeException.class)
    public void testWithBadPropertyName()
        {
        BeanWrapperPropertySetter setter = new BeanWrapperPropertySetter("bad");
        City city = new City();
        setter.setValue(city, "value");
        }

    @Test
    public void testWithStringPropertyType()
        {
        BeanWrapperPropertySetter setter = new BeanWrapperPropertySetter(
                "name");
        Person person = new Person();
        assertNull(person.getName());
        setter.setValue(person, "Ivan");
        assertEquals("Ivan", person.getName());
        }

    @Test
    public void testWithPrimitivePropertyType()
        {
        BeanWrapperPropertySetter setter = new BeanWrapperPropertySetter(
                "idNo");
        Person person = new Person();
        assertEquals(0, person.getIdNo());
        setter.setValue(person, 2504);
        assertEquals(2504, person.getIdNo());
        }

    @Test
    public void testWithComplexPropertyType()
        {
        BeanWrapperPropertySetter setter = new BeanWrapperPropertySetter(
                "address");
        Person person = new Person();
        assertNull(person.getAddress());
        Person.Address merced = new Person.Address("Merced", "Santiago",
                                                   "Chile");
        setter.setValue(person, merced);
        assertEquals(merced, person.getAddress());
        BeanWrapperPropertySetter updater = new BeanWrapperPropertySetter(
                "address.street");
        updater.setValue(person, "Av Bernard O'Higgins");
        assertEquals("Av Bernard O'Higgins", person.getAddress().getStreet());
        }
    }
