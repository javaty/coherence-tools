package com.seovic.coherence.loader.properties;


import com.seovic.coherence.test.objects.Person;

import com.seovic.expression.ExpressionException;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Date;


/**
 * Tests for {@link ExpressionPropertyGetter}.
 *
 * @author ic  2009.06.16
 */
public class ExpressionPropertyGetterTests
    {

    @Test(expected = ExpressionException.class)
    public void testWithBadProperty()
        {
        ExpressionPropertyGetter getter = new ExpressionPropertyGetter("bad");
        Person ivan = new Person("Ivan", 2504l, null, null);
        getter.getValue(ivan);
        }

    @Test
    public void testWithComplexType()
        {
        ExpressionPropertyGetter getter = new ExpressionPropertyGetter("address.city");
        Person.Address address = new Person.Address("Merced", "Santiago", "Chile");
        Person ivan = new Person("Ivan", 2504l, new Date(), address);
        assertEquals("Santiago", getter.getValue(ivan));
        }

    @Test
    public void testWithExpressionTransformation()
        {
        ExpressionPropertyGetter getter = new ExpressionPropertyGetter("name + ':' + idNo");
        Person ivan = new Person("Ivan", 2504l, null, null);
        assertEquals("Ivan:2504", getter.getValue(ivan));
        getter = new ExpressionPropertyGetter("name == 'Ivan'");
        assertTrue((Boolean) getter.getValue(ivan));
        }
    }
