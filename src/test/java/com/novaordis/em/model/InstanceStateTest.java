package com.novaordis.em.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2015 Nova Ordis LLC
 */
public class InstanceStateTest
{
    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void running() throws Exception
    {
        String literal = "running";

        InstanceState s = InstanceState.toInstanceState(literal);
        assertEquals(InstanceState.RUNNING, s);
    }

    @Test
    public void stopped() throws Exception
    {
        String literal = "stopped";

        InstanceState s = InstanceState.toInstanceState(literal);
        assertEquals(InstanceState.STOPPED, s);
    }

    @Test
    public void shuttingDown() throws Exception
    {
        String literal = "shutting-down";

        InstanceState s = InstanceState.toInstanceState(literal);
        assertEquals(InstanceState.SHUTTING_DOWN, s);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------
}




