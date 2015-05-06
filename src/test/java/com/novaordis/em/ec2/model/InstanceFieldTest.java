package com.novaordis.em.ec2.model;

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
public class InstanceFieldTest
{
    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(InstanceFieldTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void toInstanceOutputField_correctValues() throws Exception
    {
        assertEquals(InstanceField.NAME, InstanceField.toInstanceField("name"));
        assertEquals(InstanceField.NAME, InstanceField.toInstanceField("Name"));
        assertEquals(InstanceField.NAME, InstanceField.toInstanceField("NAME"));

        assertEquals(InstanceField.PUBLIC_IP, InstanceField.toInstanceField("public-ip"));
        assertEquals(InstanceField.PUBLIC_IP, InstanceField.toInstanceField("Public-IP"));
        assertEquals(InstanceField.PUBLIC_IP, InstanceField.toInstanceField("PUBLIC-IP"));
    }

    @Test
    public void toInstanceOutputField_null() throws Exception
    {
        try
        {
            InstanceField.toInstanceField(null);
            fail("null should fail");
        }
        catch (IllegalArgumentException e)
        {
            log.info(e.getMessage());
        }
    }

    @Test
    public void toInstanceOutputField_bogus() throws Exception
    {
        try
        {
            InstanceField.toInstanceField("bogus");
            fail("bogus value should fail");
        }
        catch (IllegalArgumentException e)
        {
            log.info(e.getMessage());
        }
    }

    // ID --------------------------------------------------------------------------------------------------------------

    @Test
    public void ID() throws Exception
    {
        InstanceField field = InstanceField.toInstanceField("id");
        assertEquals(InstanceField.ID, field);
        String s = field.fromInstance(new Instance("i-0ea9c7f8"));
        assertEquals("i-0ea9c7f8", s);
    }

    // NAME ------------------------------------------------------------------------------------------------------------

    @Test
    public void NAME() throws Exception
    {
        InstanceField field = InstanceField.toInstanceField("name");
        assertEquals(InstanceField.NAME, field);
        Instance i = new Instance("i-0ea9c7f8");
        i.setName("f01");
        String s = field.fromInstance(i);
        assertEquals("f01", s);
    }

    // PUBLIC_IP -------------------------------------------------------------------------------------------------------

    @Test
    public void PUBLIC_IP() throws Exception
    {
        InstanceField field = InstanceField.toInstanceField("public-ip");
        assertEquals(InstanceField.PUBLIC_IP, field);
        Instance i = new Instance("i-0ea9c7f8");
        i.setPublicIp("54.149.178.21");
        String s = field.fromInstance(i);
        assertEquals("54.149.178.21", s);
    }

    // STATE -----------------------------------------------------------------------------------------------------------

    @Test
    public void STATE() throws Exception
    {
        InstanceField field = InstanceField.toInstanceField("state");
        assertEquals(InstanceField.STATE, field);
        Instance i = new Instance("i-0ea9c7f8");
        i.setState(InstanceState.RUNNING);
        String s = field.fromInstance(i);
        assertEquals("running", s);
    }

    @Test
    public void null_STATE() throws Exception
    {
        InstanceField field = InstanceField.toInstanceField("state");
        Instance i = new Instance("i-0ea9c7f8");
        assertNull(i.getState());
        String s = field.fromInstance(i);
        assertNull(s);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------
}




