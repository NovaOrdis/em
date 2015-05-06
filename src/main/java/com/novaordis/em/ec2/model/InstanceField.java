package com.novaordis.em.ec2.model;

import java.net.InetAddress;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Fields that are not handled yet:
 *
 * "Amazon Machine Image": "ami-4dbf9e7d" (Red Hat Enterpirse Linux 7.1 (HVM) SSD volume type), etc.
 *
 * "Root device type": "ebs"
 *
 * "Virtualization type: "hvm"
 *
 * "Instance type": t2.micro, t2.small, c4.large, etc.
 *
 * "Network" Virtual Private Cloud vpc-69de730c
 *
 * "Subnet" subnet-53993c24 default in us-west-2b
 *
 *
 *
 *
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

    public String getOutputLiteral()
    {
        return commandLineLiteral;
    }

    public static InstanceField toInstanceField(String commandLineLiteral)
    {
        if (commandLineLiteral == null)
        {
            throw new IllegalArgumentException("null command line literal");
        }

        for(InstanceField f : InstanceField.values())
        {
            if (f.getCommandLineLiteral().equalsIgnoreCase(commandLineLiteral))
            {
                return f;
            }
        }

        throw new IllegalArgumentException("no InstanceField enum value for '" + commandLineLiteral + "'");
    }

    /**
     * @return the string rendering of the field or null if the instance does not have the field we're trying to get.
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
            InstanceState state = instance.getState();

            if (state == null)
            {
                return null;
            }

            return state.getLiteral();
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
            return InstanceState.toInstanceState(s);
        }
        else
        {
            throw new RuntimeException(this + " does not know to handle fromInstance()");
        }
    }
}
