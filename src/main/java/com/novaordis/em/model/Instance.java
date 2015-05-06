package com.novaordis.em.model;

import java.net.InetAddress;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2015 Nova Ordis LLC
 */
public class Instance
{
    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String id;
    private String name;
    private InetAddress publicIp;
    private InetAddress privateIp;
    private InstanceState state;

    // Constructors ----------------------------------------------------------------------------------------------------

    public Instance()
    {
        this(null);
    }

    public Instance(String id)
    {
        this.id = id;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public void setId(String id)
    {
        if (this.id != null && !this.id.equals(id))
        {
            throw new IllegalArgumentException("illegal attempt to replace ID '" + this.id + "' with '" + id + "'");
        }

        this.id = id;
    }

    public String getId()
    {
        return id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setPublicIp(String s) throws IllegalArgumentException
    {
        try
        {
            publicIp = InetAddress.getByName(s);
        }
        catch(Exception e)
        {
            throw new IllegalArgumentException("failed to convert '" + s + "' into an IP address", e);
        }
    }

    public InetAddress getPublicIp()
    {
        return publicIp;
    }

    public void setPrivateIp(String s) throws IllegalArgumentException
    {
        try
        {
            privateIp = InetAddress.getByName(s);
        }
        catch(Exception e)
        {
            throw new IllegalArgumentException("failed to convert '" + s + "' into an IP address", e);
        }
    }

    public InetAddress getPrivateIp()
    {
        return privateIp;
    }

    public void setState(InstanceState state)
    {
        this.state = state;
    }

    /**
     * @exception java.lang.IllegalArgumentException if the string cannot be converted to InstanceState
     */
    public void setState(String s)
    {
        this.state = InstanceState.toInstanceState(s);
    }

    public InstanceState getState()
    {
        return state;
    }

    public Object get(InstanceField field)
    {
        if (InstanceField.ID.equals(field))
        {
            return id;
        }
        else if (InstanceField.NAME.equals(field))
        {
            return name;
        }
        else if (InstanceField.STATE.equals(field))
        {
            return state;
        }
        else if (InstanceField.PUBLIC_IP.equals(field))
        {
            return publicIp;
        }
        else if (InstanceField.PRIVATE_IP.equals(field))
        {
            return privateIp;
        }
        else
        {
            return null;
        }
    }

    @Override
    public String toString()
    {
        return id == null ? "null" : id;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------
}




