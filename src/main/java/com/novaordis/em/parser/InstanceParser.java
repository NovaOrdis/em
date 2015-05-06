package com.novaordis.em.parser;

import com.novaordis.em.model.Instance;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Parses the Amazon EC2 'ec2-describe-instances' command output.
 *
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2015 Nova Ordis LLC
 */
public class InstanceParser
{
    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(InstanceParser.class);

    public static final String RESERVATION = "RESERVATION";
    public static final String INSTANCE = "INSTANCE";
    public static final String BLOCKDEVICE = "BLOCKDEVICE";
    public static final String NIC = "NIC";
    public static final String NICATTACHMENT = "NICATTACHMENT";
    public static final String NICASSOCIATION = "NICASSOCIATION";
    public static final String GROUP = "GROUP";
    public static final String PRIVATEIPADDRESS = "PRIVATEIPADDRESS";
    public static final String TAG = "TAG";
    // NOTE: If adding "known" headers, also update KNOWN_HEADERS below:
    public static final String[] KNOWN_HEADERS =
        {RESERVATION, INSTANCE, BLOCKDEVICE, NIC, NICATTACHMENT, NICASSOCIATION, GROUP, PRIVATEIPADDRESS, TAG};

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private Instance instance;
    private boolean complete;
    private Map<String, String> lines;

    // Constructors ----------------------------------------------------------------------------------------------------

    public InstanceParser()
    {
        this.instance = new Instance();
        this.lines = new HashMap<>();
        log.debug(this + " created");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return true if the line was accepted and state updated based on it or false if the line was not accepted
     */
    public boolean parse(String line)
    {
        if (complete)
        {
            return false;
        }

        int i = line.indexOf('\t');

        String header;

        if (i == -1)
        {
            header = line;
        }
        else
        {
            header = line.substring(0, i);
        }

        boolean unknownHeader = true;
        for (String knownHeader : KNOWN_HEADERS)
        {
            if (knownHeader.equalsIgnoreCase(header))
            {
                String oldLine = lines.put(knownHeader, line);
                if (oldLine != null)
                {
                    // we have two lines for the same header
                    throw new IllegalStateException("two lines for header " + knownHeader);
                }
                unknownHeader = false;
                break;
            }
        }

        if (unknownHeader)
        {
            throw new IllegalArgumentException("unknown line header '" + header + "'");
        }

        if (TAG.equalsIgnoreCase(header))
        {
            // this header is always the last in the series (so far)
            parseAll();
            complete = true;
        }

        return true;
    }

    /**
     * @return the current instance being built. It may not be complete. The instance is complete if the isComplete()
     * method returns true;
     *
     * @see InstanceParser#isComplete();
     */
    public Instance getInstance()
    {
        return instance;
    }

    /**
     * @return true if the instance being parsed is complete (and subsequent invocations of parse(...) on this parser
     * instance will return false, or false otherwise.
     */
    public boolean isComplete()
    {
        return complete;
    }

    @Override
    public String toString()
    {
        return "InstanceParser[" + Integer.toHexString(System.identityHashCode(this)) + ", " + instance + "]";
    }

    // Package protected -----------------------------------------------------------------------------------------------

    /**
     * Package protected for testing.
     */
    void parseTag(String tabSeparatedLine)
    {
        String[] tokens = tabSeparatedLine.split("\t");

        String header = tokens[0];

        if (!TAG.equals(header))
        {
            throw new IllegalArgumentException("not a valid " + TAG + " line");
        }

        String instanceLabel = tokens[1];

        if (!"instance".equalsIgnoreCase(instanceLabel))
        {
            throw new IllegalArgumentException("expecting 'instance' on position 1 and got '" + instanceLabel + "'");
        }

        String id = tokens[2];
        instance.setId(id); // noop if the ID was set to the same value

        String nameLabel = tokens[3];
        if (!"Name".equalsIgnoreCase(nameLabel))
        {
            throw new IllegalArgumentException("expecting 'Name' on position 3 and got '" + nameLabel + "'");
        }

        if (tokens.length >= 5)
        {
            String name = tokens[4].trim();
            if (name.length() > 0)
            {
                instance.setName(name);
            }
        }
    }

    /**
     * Package protected for testing.
     */
    void parseNicAssociation(String tabSeparatedLine)
    {
        String[] tokens = tabSeparatedLine.split("\t");

        String header = tokens[0];

        if (!NICASSOCIATION.equals(header))
        {
            throw new IllegalArgumentException("not a valid " + NICASSOCIATION + " line");
        }

        String publicIpString = tokens[1];
        instance.setPublicIp(publicIpString);
    }

    /**
     * Package protected for testing.
     */
    void parsePrivateIpAddress(String tabSeparatedLine)
    {
        String[] tokens = tabSeparatedLine.split("\t");

        String header = tokens[0];

            if (!PRIVATEIPADDRESS.equals(header))
        {
            throw new IllegalArgumentException("not a valid " + PRIVATEIPADDRESS + " line");
        }

        String privateIpAddress = tokens[1];
        instance.setPrivateIp(privateIpAddress);
    }

    /**
     * Package protected for testing.
     */
    void parseInstance(String tabSeparatedLine)
    {
        if (tabSeparatedLine == null)
        {
            throw new IllegalArgumentException("null " + INSTANCE + " line");
        }

        String[] tokens = tabSeparatedLine.split("\t");

        String header = tokens[0];

        if (!INSTANCE.equals(header))
        {
            throw new IllegalArgumentException("not a valid " + INSTANCE + " line");
        }

        String id = tokens[1];

        instance.setId(id); // if the ID is identical with the ones extracted before, this is a noop; otherwise throws IllegalArgumentException

        String stateString = tokens[5];
        instance.setState(stateString);
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    private void parseAll()
    {
        // get the instance ID and name from the TAG line

        String line = lines.get(TAG);

        if (line == null)
        {
            throw new IllegalStateException("no '" + TAG + "' line found");
        }

        parseTag(line);

        // get the public IP address from the NICASSOCIATION line

        line = lines.get(NICASSOCIATION);

        if (line != null)
        {
            parseNicAssociation(line);
        }

        line = lines.get(PRIVATEIPADDRESS);

        if (line != null)
        {
            parsePrivateIpAddress(line);
        }

        line = lines.get(INSTANCE);

        parseInstance(line);
    }

    // Inner classes ---------------------------------------------------------------------------------------------------
}




