package com.novaordis.em.model;

import java.net.InetAddress;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2015 Nova Ordis LLC
 */
public enum InstanceField
{
    ID("id"),
    NAME("name"),
    PUBLIC_IP("public-ip"),
    PRIVATE_IP("private-ip"),
    STATE("state");

    private String commandLineLiteral;

    InstanceField(String commandLineLiteral)
    {
        this.commandLineLiteral = commandLineLiteral;
    }

    public String getCommandLineLiteral()
    {
        return commandLineLiteral;
    }

    public static InstanceField toInstanceField(String commandLineLiteral)
    {
        if (commandLineLiteral == null)
        {
            throw new IllegalArgumentException("null command line literal");
        }

        if (ID.getCommandLineLiteral().equalsIgnoreCase(commandLineLiteral))
        {
            return ID;
        }
        else if (NAME.getCommandLineLiteral().equalsIgnoreCase(commandLineLiteral))
        {
            return NAME;
        }
        else if (PUBLIC_IP.getCommandLineLiteral().equalsIgnoreCase(commandLineLiteral))
        {
            return PUBLIC_IP;
        }
        else if (PRIVATE_IP.getCommandLineLiteral().equalsIgnoreCase(commandLineLiteral))
        {
            return PRIVATE_IP;
        }
        else if (STATE.getCommandLineLiteral().equalsIgnoreCase(commandLineLiteral))
        {
            return STATE;
        }
        else
        {
            throw new IllegalArgumentException("no InstanceOutputField enum value for '" + commandLineLiteral + "'");
        }
    }

    /**
     * @return null if the instance does not have the field we're trying to get and convert to string.
     */
    public String fromInstance(Instance instance)
    {
        if (ID.equals(this))
        {
            return instance.getId();
        }
        else if (NAME.equals(this))
        {
            return instance.getName();
        }
        else if (PUBLIC_IP.equals(this))
        {
            InetAddress addr = instance.getPublicIp();

            if (addr == null)
            {
                return null;
            }

            String s = addr.toString();

            int i = s.indexOf('/');

            if (i != -1)
            {
                s = s.substring(i + 1);
            }

            return s;
        }
        else if (PRIVATE_IP.equals(this))
        {
            InetAddress addr = instance.getPrivateIp();

            if (addr == null)
            {
                return null;
            }

            String s = addr.toString();

            int i = s.indexOf('/');

            if (i != -1)
            {
                s = s.substring(i + 1);
            }

            return s;
        }
        else if (STATE.equals(this))
        {
            return instance.getState().toString();
        }
        else
        {
            throw new RuntimeException(this + " does not know to handle fromInstance()");
        }
    }

    public Object fromString(String s)
    {
        if (ID.equals(this) || NAME.equals(this))
        {
            return s;
        }
        else if (PUBLIC_IP.equals(this) || PRIVATE_IP.equals(this))
        {
            try
            {
                return InetAddress.getByName(s);
            }
            catch (Exception e)
            {
                throw new IllegalArgumentException("'" + s + "' cannot be converted to an IP address");
            }
        }
        else if (STATE.equals(this))
        {
            return InstanceState.valueOf(s);
        }
        else
        {
            throw new RuntimeException(this + " does not know to handle fromInstance()");
        }
    }
}
