package com.novaordis.em.parser;

import com.novaordis.em.model.Instance;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.net.InetAddress;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2015 Nova Ordis LLC
 */
public class InstanceParserTest
{
    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void testBasicUsage_NoPublicIP() throws Exception
    {
        String input =
            "RESERVATION\tr-25811829\t801854874259\t\n" +
                "INSTANCE\ti-db0ab82d\tami-4dbf9e7d\t\tip-172-31-25-44.us-west-2.compute.internal\tstopped\tovidu_default\t0\t\tt2.micro\t2015-04-06T18:57:02+0000\tus-west-2b\t\tmonitoring-disabled\t\t172.31.25.44\tvpc-69de730c\tsubnet-53993c24\tebs\t\t\t\thvm\txen\tUgPIf1428346622222\tsg-16481073\tdefault\tfalse\t\n" +
                "BLOCKDEVICE\t/dev/sda1\tvol-4e65df5c\t2015-04-06T18:57:05.000Z\ttrue\t\t\n" +
                "NIC\teni-041b4a72\tsubnet-53993c24\tvpc-69de730c\t801854874259\tin-use\t172.31.25.44\tip-172-31-25-44.us-west-2.compute.internal\ttrue\n" +
                "NICATTACHMENT\teni-attach-d0c2f3d9\t0\tattached\t2015-04-06T11:57:02-0700\ttrue\n" +
                "GROUP\tsg-16481073\tnfs-server\n" +
                "PRIVATEIPADDRESS\t172.31.25.44\tip-172-31-25-44.us-west-2.compute.internal\t\n" +
                "TAG\tinstance\ti-db0ab82d\tName\tf01\n" +
                "RESERVATION\tr-05430309\t801854874259\t\n";


        BufferedReader br = new BufferedReader(new StringReader(input));

        String line;

        InstanceParser ip = new InstanceParser();

        assertFalse(ip.isComplete());

        int lineCount = 0;

        while((line = br.readLine()) != null)
        {
            lineCount ++;

            if (lineCount == 8)
            {
                // the TAG line
                assertTrue(ip.parse(line));
                assertTrue(ip.isComplete());
            }
            else if (lineCount == 9)
            {
                // another instance that start with "RESERVATION"
                assertFalse(ip.parse(line));
                assertTrue(ip.isComplete());
            }
            else
            {
                assertTrue(ip.parse(line));
                assertFalse(ip.isComplete());
            }
        }

        br.close();

        Instance instance = ip.getInstance();

        assertEquals("i-db0ab82d", instance.getId());
        assertEquals("f01", instance.getName());
        assertEquals(null, instance.getPublicIp());
    }

    @Test
    public void testBasicUsage_PublicIP() throws Exception
    {
        String input =
            "RESERVATION\tr-25811829\t801854874259\t\n" +
                "INSTANCE\ti-db0ab82d\tami-4dbf9e7d\tec2-52-24-116-231.us-west-2.compute.amazonaws.com\tip-172-31-25-44.us-west-2.compute.internal\trunning\tovidu_default\t0\t\tt2.micro\t2015-05-05T06:49:00+0000\tus-west-2b\t\t\t\tmonitoring-disabled\t52.24.116.231\t172.31.25.44\tvpc-69de730c\tsubnet-53993c24\tebs\t\t\t\thvm\txen\tUgPIf1428346622222\tsg-16481073\tdefault\tfalse\t\n" +
                "BLOCKDEVICE\t/dev/sda1\tvol-4e65df5c\t2015-04-06T18:57:05.000Z\ttrue\t\t\n" +
                "NIC\teni-041b4a72\tsubnet-53993c24\tvpc-69de730c\t801854874259\tin-use\t172.31.25.44\tip-172-31-25-44.us-west-2.compute.internal\ttrue\n" +
                "NICATTACHMENT\teni-attach-d0c2f3d9\t0\tattached\t2015-04-06T11:57:02-0700\ttrue\n" +
                "NICASSOCIATION\t52.24.116.231\tamazon\t172.31.25.44\n" +
                "GROUP\tsg-16481073\tnfs-server\n" +
                "PRIVATEIPADDRESS\t172.31.25.44\tip-172-31-25-44.us-west-2.compute.internal\tec2-52-24-116-231.us-west-2.compute.amazonaws.com\n" +
                "TAG\tinstance\ti-db0ab82d\tName\tf01\n" +
                "RESERVATION\tr-05430309\t801854874259\t\n";

        BufferedReader br = new BufferedReader(new StringReader(input));

        String line;

        InstanceParser ip = new InstanceParser();

        int lineCount = 0;

        // the difference is the extra "NICASSOCIATION" line

        while((line = br.readLine()) != null)
        {
            lineCount ++;

            if (lineCount == 9)
            {
                // the TAG line
                assertTrue(ip.parse(line));
                assertTrue(ip.isComplete());
            }
            else if (lineCount == 10)
            {
                // another instance that start with "RESERVATION"
                assertFalse(ip.parse(line));
            }
            else
            {
                assertTrue(ip.parse(line));
            }
        }

        br.close();

        Instance instance = ip.getInstance();

        assertEquals("i-db0ab82d", instance.getId());
        assertEquals("f01", instance.getName());
        assertEquals(InetAddress.getByName("52.24.116.231"), instance.getPublicIp());
    }

    // parseTag() ------------------------------------------------------------------------------------------------------

    @Test
    public void parseTag() throws Exception
    {
        String input = "TAG\tinstance\ti-db0ab82d\tName\tf01\n";

        InstanceParser p = new InstanceParser();

        p.parseTag(input);

        Instance i = p.getInstance();

        assertEquals("i-db0ab82d", i.getId());
        assertEquals("f01", i.getName());
    }

    @Test
    public void parseTag_NoName() throws Exception
    {
        String input="TAG\tinstance\ti-8de9807b\tName\t\n";

        InstanceParser p = new InstanceParser();

        p.parseTag(input);

        Instance i = p.getInstance();

        assertEquals("i-8de9807b", i.getId());
        assertNull(i.getName());
    }

    // parseNicAssociation() -------------------------------------------------------------------------------------------

    @Test
    public void parseNicAssociation() throws Exception
    {
        String input = "NICASSOCIATION\t52.24.116.231\tamazon\t172.31.25.44\n";

        InstanceParser p = new InstanceParser();

        p.parseNicAssociation(input);

        Instance i = p.getInstance();

        assertEquals(InetAddress.getByName("52.24.116.231"), i.getPublicIp());
    }

    // parsePrivateNicAddress() ----------------------------------------------------------------------------------------


    @Test
    public void parserrivateIpAddress() throws Exception
    {
        String input = "PRIVATEIPADDRESS\t172.31.25.44\tip-172-31-25-44.us-west-2.compute.internal\tec2-52-24-168-63.us-west-2.compute.amazonaws.com";

        InstanceParser p = new InstanceParser();

        p.parsePrivateIpAddress(input);

        Instance i = p.getInstance();

        assertEquals(InetAddress.getByName("172.31.25.44"), i.getPrivateIp());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------
}




