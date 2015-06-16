package com.novaordis.em.ec2;

import com.novaordis.em.UserErrorException;
import com.novaordis.em.ec2.model.Filter;
import com.novaordis.em.ec2.model.Instance;
import com.novaordis.em.ec2.model.InstanceField;
import com.novaordis.em.ec2.model.InstanceState;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.InetAddress;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2015 Nova Ordis LLC
 */
public class grep_instancesTest
{
    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(grep_instancesTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // command line arguments ------------------------------------------------------------------------------------------

    @Test
    public void commandLine_UnknownArgument() throws Exception
    {
        try
        {
            new grep_instances(new String[]{"--no-such-argument"});
            fail("unknown argument, should fail with UserErrorException");
        }
        catch(UserErrorException e)
        {
            log.info(e.getMessage());
        }
    }

    // command line arguments: --output ---------------------------------------------------------------------------------

    @Test
    public void commandLine_NothingFollowsList() throws Exception
    {
        try
        {
            new grep_instances(new String[]{"--list"});
            fail("nothing follows --list, should fail with UserErrorException");
        }
        catch(UserErrorException e)
        {
            log.info(e.getMessage());
        }
    }

    @Test
    public void commandLine_CorrectList() throws Exception
    {
        grep_instances g = new grep_instances(new String[]{"--list", "name:public-ip"});

        List<InstanceField> fields = g.getOutputFields();

        assertEquals(2, fields.size());

        assertEquals(InstanceField.NAME, fields.get(0));
        assertEquals(InstanceField.PUBLIC_IP, fields.get(1));

        //
        // test no filters
        //
        assertTrue(g.getFilters().isEmpty());
    }

    // command line arguments: implicit filter -------------------------------------------------------------------------

    @Test
    public void commandLine_NoFilters() throws Exception
    {
        grep_instances g = new grep_instances(new String[0]);

        //
        // test no filters
        //

        assertTrue(g.getFilters().isEmpty());
    }

    @Test
    public void commandLine_OneFilter() throws Exception
    {
        grep_instances p = new grep_instances(new String[]{"state=running"});

        List<Filter> filters = p.getFilters();

        assertEquals(1, filters.size());

        Filter f = filters.get(0);

        assertEquals("state=running", f.getLiteral());
    }

    // create() ---------------------------------------------------------------------------------------------------------

    @Test
    public void noCommandOutputParsed() throws Exception
    {
        grep_instances p = new grep_instances(new String[0]);

        try
        {
            p.getInstances();
            fail("should fail with IllegalStateException because no command output was parsed");
        }
        catch(IllegalStateException e)
        {
            log.info(e.getMessage());
        }
    }

    @Test
    public void bogusLine() throws Exception
    {
        String input = "bogus line\n";

        BufferedReader br = new BufferedReader(new StringReader(input));
        grep_instances p = new grep_instances(new String[0]);

        try
        {
            p.parse(br);
            fail("should throw IllegalArgumentException, on account of bogus line");
        }
        catch(IllegalArgumentException e)
        {
            log.info(e.getMessage());
        }
    }

    @Test
    public void testBasicUsage() throws Exception
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
                "RESERVATION\tr-05430309\t801854874259\t\n" +
                "INSTANCE\ti-0ea9c7f8\tami-4dbf9e7d\tec2-52-24-56-245.us-west-2.compute.amazonaws.com\tip-172-31-29-74.us-west-2.compute.internal\trunning\tinstallation_access\t0\t\tt2.micro\t2015-05-05T00:58:09+0000\tus-west-2b\t\t\t\tmonitoring-disabled\t52.24.56.245\t172.31.29.74\tvpc-69de730c\tsubnet-53993c24\tebs\t\t\t\thvm\txen\tjrRud1430787488730\tsg-f8257e9d\tdefault\tfalse\t\n" +
                "BLOCKDEVICE\t/dev/sda1\tvol-6fe8877d\t2015-05-05T00:58:13.000Z\ttrue\t\t\n" +
                "NIC\teni-aaf5f1dc\tsubnet-53993c24\tvpc-69de730c\t801854874259\tin-use\t172.31.29.74\tip-172-31-29-74.us-west-2.compute.internal\ttrue\n" +
                "NICATTACHMENT\teni-attach-cf77a8ee\t0\tattached\t2015-05-04T17:58:09-0700\ttrue\n" +
                "NICASSOCIATION\t52.24.56.245\tamazon\t172.31.29.74\n" +
                "GROUP\tsg-f8257e9d\tAMQ Broker\n" +
                "PRIVATEIPADDRESS\t172.31.29.74\tip-172-31-29-74.us-west-2.compute.internal\tec2-52-24-56-245.us-west-2.compute.amazonaws.com\n" +
                "TAG\tinstance\ti-0ea9c7f8\tName\tb01\n";


        BufferedReader br = new BufferedReader(new StringReader(input));

        grep_instances p = new grep_instances(new String[0]);
        p.parse(br);

        br.close();

        List<Instance> instances = p.getInstances();

        assertEquals(2, instances.size());

        Instance instance = instances.get(0);

        assertEquals("i-db0ab82d", instance.getId());
        assertEquals(InstanceState.STOPPED, instance.getState());

        Instance instance2 = instances.get(1);

        assertEquals("i-0ea9c7f8", instance2.getId());
        assertEquals(InstanceState.RUNNING, instance2.getState());
    }

    @Test
    public void testNoTAGLine_FollowedByRESERVATION() throws Exception
    {
        String input =
            "RESERVATION\tr-a43778a8\t801854874259\t\n" +
                "INSTANCE\ti-5886f2ae\tami-4dbf9e7d\tec2-52-24-51-225.us-west-2.compute.amazonaws.com\tip-172-31-17-69.us-west-2.compute.internal\trunning\tinstallation_access\t0\t\tt2.micro\t2015-05-07T22:22:33+0000\tus-west-2b\t\t\t\tmonitoring-disabled\t52.24.51.225\t172.31.17.69\tvpc-69de730c\tsubnet-53993c24\tebs\t\t\t\thvm\txen\t0b166d9e-e6d6-4238-a3bd-77c5213cf153\tsg-bb3222de\tdefault\tfalse\t\n" +
                "BLOCKDEVICE\t/dev/sda1\tvol-78384d6a\t2015-05-07T22:22:36.000Z\ttrue\t\t\n" +
                "NIC\teni-cfbcbfb9\tsubnet-53993c24\tvpc-69de730c\t801854874259\tin-use\t172.31.17.69\tip-172-31-17-69.us-west-2.compute.internal\ttrue\n" +
                "NICATTACHMENT\teni-attach-b373a892\t0\tattached\t2015-05-07T15:22:33-0700\ttrue\n" +
                "NICASSOCIATION\t52.24.51.225\tamazon\t172.31.17.69\n" +
                "GROUP\tsg-bb3222de\tgeneral-purpose-client\n" +
                "PRIVATEIPADDRESS\t172.31.17.69\tip-172-31-17-69.us-west-2.compute.internal\tec2-52-24-51-225.us-west-2.compute.amazonaws.com\n" +
                "RESERVATION\tr-befbb8b2\t801854874259\t\n" +
                "INSTANCE\ti-c090f836\tami-4dbf9e7d\t\tip-172-31-29-173.us-west-2.compute.internal\tstopped\tinstallation_access\t0\t\tt2.micro\t2015-05-07T18:04:44+0000\tus-west-2b\t\t\t\tmonitoring-disabled\t\t172.31.29.173\tvpc-69de730c\tsubnet-53993c24\tebs\t\t\t\thvm\txen\tVlgfa1430878448727\tsg-f8257e9d\tdefault\tfalse\t\n" +
                "BLOCKDEVICE\t/dev/sda1\tvol-3e99f02c\t2015-05-06T02:14:11.000Z\ttrue\t\t\n" +
                "NIC\teni-86f3f5f0\tsubnet-53993c24\tvpc-69de730c\t801854874259\tin-use\t172.31.29.173\tip-172-31-29-173.us-west-2.compute.internal\ttrue\n" +
                "NICATTACHMENT\teni-attach-a0e93081\t0\tattached\t2015-05-05T19:14:09-0700\ttrue\n" +
                "GROUP\tsg-f8257e9d\tAMQ Broker\n" +
                "PRIVATEIPADDRESS\t172.31.29.173\tip-172-31-29-173.us-west-2.compute.internal\t\n" +
                "TAG\tinstance\ti-c090f836\tName\tb01";

        BufferedReader br = new BufferedReader(new StringReader(input));

        grep_instances p = new grep_instances(new String[0]);

        p.parse(br);

        br.close();

        List<Instance> instances = p.getInstances();

        assertEquals(2, instances.size());

        Instance instance = instances.get(0);
        assertEquals("i-5886f2ae", instance.getId());
        assertNull(instance.getName());
        assertEquals(InetAddress.getByName("172.31.17.69"), instance.getPrivateIp());
        assertEquals(InetAddress.getByName("52.24.51.225"), instance.getPublicIp());

        Instance instance2 = instances.get(1);
        assertEquals("i-c090f836", instance2.getId());
        assertEquals("b01", instance2.getName());
        assertEquals(InetAddress.getByName("172.31.29.173"), instance2.getPrivateIp());
        assertNull(instance2.getPublicIp());
    }

    @Test
    public void testNoTAGLine_FollowedByINSTANCE() throws Exception
    {
        String input =
            "RESERVATION\tr-a43778a8\t801854874259\t\n" +
                "INSTANCE\ti-5886f2ae\tami-4dbf9e7d\tec2-52-24-51-225.us-west-2.compute.amazonaws.com\tip-172-31-17-69.us-west-2.compute.internal\trunning\tinstallation_access\t0\t\tt2.micro\t2015-05-07T22:22:33+0000\tus-west-2b\t\t\t\tmonitoring-disabled\t52.24.51.225\t172.31.17.69\tvpc-69de730c\tsubnet-53993c24\tebs\t\t\t\thvm\txen\t0b166d9e-e6d6-4238-a3bd-77c5213cf153\tsg-bb3222de\tdefault\tfalse\t\n" +
                "BLOCKDEVICE\t/dev/sda1\tvol-78384d6a\t2015-05-07T22:22:36.000Z\ttrue\t\t\n" +
                "NIC\teni-cfbcbfb9\tsubnet-53993c24\tvpc-69de730c\t801854874259\tin-use\t172.31.17.69\tip-172-31-17-69.us-west-2.compute.internal\ttrue\n" +
                "NICATTACHMENT\teni-attach-b373a892\t0\tattached\t2015-05-07T15:22:33-0700\ttrue\n" +
                "NICASSOCIATION\t52.24.51.225\tamazon\t172.31.17.69\n" +
                "GROUP\tsg-bb3222de\tgeneral-purpose-client\n" +
                "PRIVATEIPADDRESS\t172.31.17.69\tip-172-31-17-69.us-west-2.compute.internal\tec2-52-24-51-225.us-west-2.compute.amazonaws.com\n" +
                "INSTANCE\ti-c090f836\tami-4dbf9e7d\t\tip-172-31-29-173.us-west-2.compute.internal\tstopped\tinstallation_access\t0\t\tt2.micro\t2015-05-07T18:04:44+0000\tus-west-2b\t\t\t\tmonitoring-disabled\t\t172.31.29.173\tvpc-69de730c\tsubnet-53993c24\tebs\t\t\t\thvm\txen\tVlgfa1430878448727\tsg-f8257e9d\tdefault\tfalse\t\n" +
                "BLOCKDEVICE\t/dev/sda1\tvol-3e99f02c\t2015-05-06T02:14:11.000Z\ttrue\t\t\n" +
                "NIC\teni-86f3f5f0\tsubnet-53993c24\tvpc-69de730c\t801854874259\tin-use\t172.31.29.173\tip-172-31-29-173.us-west-2.compute.internal\ttrue\n" +
                "NICATTACHMENT\teni-attach-a0e93081\t0\tattached\t2015-05-05T19:14:09-0700\ttrue\n" +
                "GROUP\tsg-f8257e9d\tAMQ Broker\n" +
                "PRIVATEIPADDRESS\t172.31.29.173\tip-172-31-29-173.us-west-2.compute.internal\t\n" +
                "TAG\tinstance\ti-c090f836\tName\tb01";

        BufferedReader br = new BufferedReader(new StringReader(input));

        grep_instances p = new grep_instances(new String[0]);

        p.parse(br);

        br.close();

        List<Instance> instances = p.getInstances();

        assertEquals(2, instances.size());

        Instance instance = instances.get(0);
        assertEquals("i-5886f2ae", instance.getId());
        assertNull(instance.getName());
        assertEquals(InetAddress.getByName("172.31.17.69"), instance.getPrivateIp());
        assertEquals(InetAddress.getByName("52.24.51.225"), instance.getPublicIp());

        Instance instance2 = instances.get(1);
        assertEquals("i-c090f836", instance2.getId());
        assertEquals("b01", instance2.getName());
        assertEquals(InetAddress.getByName("172.31.29.173"), instance2.getPrivateIp());
        assertNull(instance2.getPublicIp());
    }

    // end-to-end with filters and output ------------------------------------------------------------------------------

    @Test
    public void endToEndWithFiltersAndOutput() throws Exception
    {
        String input = "RESERVATION\tr-25811829\t801854874259\t\n" +
            "INSTANCE\ti-db0ab82d\tami-4dbf9e7d\tec2-52-24-116-231.us-west-2.compute.amazonaws.com\tip-172-31-25-44.us-west-2.compute.internal\trunning\tovidu_default\t0\t\tt2.micro\t2015-05-05T06:49:00+0000\tus-west-2b\t\t\t\tmonitoring-disabled\t52.24.116.231\t172.31.25.44\tvpc-69de730c\tsubnet-53993c24\tebs\t\t\t\thvm\txen\tUgPIf1428346622222\tsg-16481073\tdefault\tfalse\t\n" +
            "BLOCKDEVICE\t/dev/sda1\tvol-4e65df5c\t2015-04-06T18:57:05.000Z\ttrue\t\t\n" +
            "NIC\teni-041b4a72\tsubnet-53993c24\tvpc-69de730c\t801854874259\tin-use\t172.31.25.44\tip-172-31-25-44.us-west-2.compute.internal\ttrue\n" +
            "NICATTACHMENT\teni-attach-d0c2f3d9\t0\tattached\t2015-04-06T11:57:02-0700\ttrue\n" +
            "NICASSOCIATION\t52.24.116.231\tamazon\t172.31.25.44\n" +
            "GROUP\tsg-16481073\tnfs-server\n" +
            "PRIVATEIPADDRESS\t172.31.25.44\tip-172-31-25-44.us-west-2.compute.internal\tec2-52-24-116-231.us-west-2.compute.amazonaws.com\n" +
            "TAG\tinstance\ti-db0ab82d\tName\tf01\n" +
            "RESERVATION\tr-05430309\t801854874259\t\n" +
            "INSTANCE\ti-0ea9c7f8\tami-4dbf9e7d\t\tip-172-31-29-74.us-west-2.compute.internal\tstopped\tinstallation_access\t0\t\tt2.micro\t2015-05-05T00:58:09+0000\tus-west-2b\t\t\t\tmonitoring-disabled\t\t172.31.29.74\tvpc-69de730c\tsubnet-53993c24\tebs\t\t\t\thvm\txen\tjrRud1430787488730\tsg-f8257e9d\tdefault\tfalse\t\n" +
            "BLOCKDEVICE\t/dev/sda1\tvol-6fe8877d\t2015-05-05T00:58:13.000Z\ttrue\t\t\n" +
            "NIC\teni-aaf5f1dc\tsubnet-53993c24\tvpc-69de730c\t801854874259\tin-use\t172.31.29.74\tip-172-31-29-74.us-west-2.compute.internal\ttrue\n" +
            "NICATTACHMENT\teni-attach-cf77a8ee\t0\tattached\t2015-05-04T17:58:09-0700\ttrue\n" +
            "GROUP\tsg-f8257e9d\tAMQ Broker\n" +
            "PRIVATEIPADDRESS\t172.31.29.74\tip-172-31-29-74.us-west-2.compute.internal\t\n" +
            "TAG\tinstance\ti-0ea9c7f8\tName\tb01\n";

        BufferedReader br = new BufferedReader(new StringReader(input));

        grep_instances p = new grep_instances(
            new String[] {"state=running", "--list", "name:state:public-ip"});

        p.parse(br);

        br.close();

        List<Instance> instances = p.getInstances();

        assertEquals(1, instances.size());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        p.output(ps);

        ps.close();

        String output = new String(baos.toByteArray());
        assertEquals("f01:running:52.24.116.231\n", output);
    }

    // collection ------------------------------------------------------------------------------------------------------

    @Test
    public void test_ProgrammaticallyCreatedInstanceDoesNotHaveAReservationLine() throws Exception
    {
        String input =
            "RESERVATION\tr-befbb8b2\t801854874259\t\n" +
                "INSTANCE\ti-c090f836\tami-4dbf9e7d\t\tip-172-31-29-173.us-west-2.compute.internal\tstopped\tinstallation_access\t0\t\tt2.micro\t2015-05-07T18:04:44+0000\tus-west-2b\t\t\t\tmonitoring-disabled\t\t172.31.29.173\tvpc-69de730c\tsubnet-53993c24\tebs\t\t\t\thvm\txen\tVlgfa1430878448727\tsg-f8257e9d\tdefault\tfalse\t\n" +
                "BLOCKDEVICE\t/dev/sda1\tvol-3e99f02c\t2015-05-06T02:14:11.000Z\ttrue\t\t\n" +
                "NIC\teni-86f3f5f0\tsubnet-53993c24\tvpc-69de730c\t801854874259\tin-use\t172.31.29.173\tip-172-31-29-173.us-west-2.compute.internal\ttrue\n" +
                "NICATTACHMENT\teni-attach-a0e93081\t0\tattached\t2015-05-05T19:14:09-0700\ttrue\n" +
                "GROUP\tsg-f8257e9d\tAMQ Broker\n" +
                "PRIVATEIPADDRESS\t172.31.29.173\tip-172-31-29-173.us-west-2.compute.internal\t\n" +
                "TAG\tinstance\ti-c090f836\tName\tb01\n" +
                "INSTANCE\ti-c190f837\tami-4dbf9e7d\t\tip-172-31-29-174.us-west-2.compute.internal\tstopped\tinstallation_access\t1\t\tt2.micro\t2015-05-07T09:17:37+0000\tus-west-2b\t\t\t\tmonitoring-disabled\t\t172.31.29.174\tvpc-69de730c\tsubnet-53993c24\tebs\t\t\t\thvm\txen\tVlgfa1430878448727\tsg-f8257e9d\tdefault\tfalse\t\n" +
                "BLOCKDEVICE\t/dev/sda1\tvol-7499f066\t2015-05-06T02:14:12.000Z\ttrue\t\t\n" +
                "NIC\teni-99f3f5ef\tsubnet-53993c24\tvpc-69de730c\t801854874259\tin-use\t172.31.29.174\tip-172-31-29-174.us-west-2.compute.internal\ttrue\n" +
                "NICATTACHMENT\teni-attach-aee9308f\t0\tattached\t2015-05-05T19:14:09-0700\ttrue\n" +
                "GROUP\tsg-f8257e9d\tAMQ Broker\n" +
                "PRIVATEIPADDRESS\t172.31.29.174\tip-172-31-29-174.us-west-2.compute.internal\t\n" +
                "TAG\tinstance\ti-c190f837\tName\tb02\n" +
                "RESERVATION\tr-a43778a8\t801854874259\t\n" +
                "INSTANCE\ti-5886f2ae\tami-4dbf9e7d\tec2-52-24-51-225.us-west-2.compute.amazonaws.com\tip-172-31-17-69.us-west-2.compute.internal\trunning\tinstallation_access\t0\t\tt2.micro\t2015-05-07T22:22:33+0000\tus-west-2b\t\t\t\tmonitoring-disabled\t52.24.51.225\t172.31.17.69\tvpc-69de730c\tsubnet-53993c24\tebs\t\t\t\thvm\txen\t0b166d9e-e6d6-4238-a3bd-77c5213cf153\tsg-bb3222de\tdefault\tfalse\t\n" +
                "BLOCKDEVICE\t/dev/sda1\tvol-78384d6a\t2015-05-07T22:22:36.000Z\ttrue\t\t\n" +
                "NIC\teni-cfbcbfb9\tsubnet-53993c24\tvpc-69de730c\t801854874259\tin-use\t172.31.17.69\tip-172-31-17-69.us-west-2.compute.internal\ttrue\n" +
                "NICATTACHMENT\teni-attach-b373a892\t0\tattached\t2015-05-07T15:22:33-0700\ttrue\n" +
                "NICASSOCIATION\t52.24.51.225\tamazon\t172.31.17.69\n" +
                "GROUP\tsg-bb3222de\tgeneral-purpose-client\n" +
                "PRIVATEIPADDRESS\t172.31.17.69\tip-172-31-17-69.us-west-2.compute.internal\tec2-52-24-51-225.us-west-2.compute.amazonaws.com\n";

        BufferedReader br = new BufferedReader(new StringReader(input));

        grep_instances p = new grep_instances(new String[0]);

        p.parse(br);

        br.close();

        List<Instance> instances = p.getInstances();

        assertEquals(3, instances.size());

        Instance instance = instances.get(0);
        assertEquals("i-c090f836", instance.getId());
        assertEquals("b01", instance.getName());

        Instance instance2 = instances.get(1);
        assertEquals("i-c190f837", instance2.getId());
        assertEquals("b02", instance2.getName());

        Instance instance3 = instances.get(2);
        assertEquals("i-5886f2ae", instance3.getId());
        assertNull(instance3.getName());
        assertEquals(InetAddress.getByName("172.31.17.69"), instance3.getPrivateIp());
        assertEquals(InetAddress.getByName("52.24.51.225"), instance3.getPublicIp());
    }

    @Test
    public void noInstances() throws Exception
    {
        BufferedReader br = new BufferedReader(new StringReader(""));

        grep_instances p = new grep_instances(new String[0]);

        p.parse(br);

        br.close();

        List<Instance> instances = p.getInstances();
        assertTrue(instances.isEmpty());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------
}




