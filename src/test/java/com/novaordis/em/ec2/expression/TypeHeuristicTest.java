package com.novaordis.em.ec2.expression;

import com.novaordis.em.ec2.model.InstanceState;
import org.apache.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2015 Nova Ordis LLC
 */
public class TypeHeuristicTest
{
    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(TypeHeuristicTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void bothNull() throws Exception
    {
        assertFalse(TypeHeuristic.equals(null, null));
    }

    @Test
    public void firstNull() throws Exception
    {
        assertFalse(TypeHeuristic.equals(null, "blah"));
    }

    @Test
    public void secondNull() throws Exception
    {
        assertFalse(TypeHeuristic.equals("blah", null));
    }

    @Test
    public void bothStrings_Equal() throws Exception
    {
        assertTrue(TypeHeuristic.equals("apple", "apple"));
    }

    @Test
    public void bothStrings_NotEqual() throws Exception
    {
        assertFalse(TypeHeuristic.equals("apple", "orange"));
    }

    @Test
    public void stateAndString_Equal() throws Exception
    {
        assertTrue(TypeHeuristic.equals(InstanceState.RUNNING, "running"));
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------
}




