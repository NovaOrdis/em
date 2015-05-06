package com.novaordis.em.model;

import org.apache.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2015 Nova Ordis LLC
 */
public class InstanceTest
{
    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(InstanceTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void id() throws Exception
    {
        Instance i = new Instance();

        assertNull(i.getId());
        assertNull(i.get(InstanceField.ID));

        i.setId("i-0ea9c7f8");

        assertEquals("i-0ea9c7f8", i.getId());
        assertEquals("i-0ea9c7f8", i.get(InstanceField.ID));

        // attempt to set the same value

        i.setId("i-0ea9c7f8");

        assertEquals("i-0ea9c7f8", i.getId());
        assertEquals("i-0ea9c7f8", i.get(InstanceField.ID));

        // attempt to set a different value

        try
        {
            i.setId("i-00000000");
            fail("should fail because id is already set to a different value");
        }
        catch(IllegalArgumentException e)
        {
            log.info(e.getMessage());
        }

        assertEquals("i-0ea9c7f8", i.getId());
        assertEquals("i-0ea9c7f8", i.get(InstanceField.ID));
    }

    @Test
    public void name() throws Exception
    {
        Instance i = new Instance();

        assertNull(i.getName());
        assertNull(i.get(InstanceField.NAME));

        i.setName("blah");

        assertEquals("blah", i.getName());
        assertEquals("blah", i.get(InstanceField.NAME));
    }

    @Test
    public void state() throws Exception
    {
        Instance i = new Instance();

        assertNull(i.getState());
        assertNull(i.get(InstanceField.STATE));

        i.setState(InstanceState.RUNNING);

        assertEquals(InstanceState.RUNNING, i.getState());
        assertEquals(InstanceState.RUNNING, i.get(InstanceField.STATE));
    }

    @Test
    public void setIllegalState() throws Exception
    {
        Instance i = new Instance();

        try
        {
            i.setState("no-such-state");
            fail("should fail, illegal state");
        }
        catch (IllegalArgumentException e)
        {
            log.info(e.getMessage());
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------
}




