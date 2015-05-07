package com.novaordis.em.ec2.model;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2015 Nova Ordis LLC
 */
public class FilterTest
{
    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void nameEquality_SingleValue() throws Exception
    {
        Filter f = new Filter("name=orange");

        Instance instance = new Instance();
        instance.setName("orange");

        assertTrue(f.allows(instance));
    }

    @Test
    public void nameInequality_SingleValue() throws Exception
    {
        Filter f = new Filter("name=orange");

        Instance instance = new Instance();
        instance.setName("apple");

        assertFalse(f.allows(instance));
    }

    @Test
    public void nameInclusion() throws Exception
    {
        Filter f = new Filter("name=apple|orange");

        Instance instance = new Instance();
        instance.setName("apple");

        assertTrue(f.allows(instance));
    }

    @Test
    public void nameExclusion() throws Exception
    {
        Filter f = new Filter("name=apple|orange");

        Instance instance = new Instance();
        instance.setName("lemon");

        assertFalse(f.allows(instance));
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------
}




