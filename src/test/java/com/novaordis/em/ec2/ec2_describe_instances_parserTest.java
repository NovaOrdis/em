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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2015 Nova Ordis LLC
 */
public class ec2_describe_instances_parserTest
{
    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(ec2_describe_instances_parserTest.class);

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
            new ec2_describe_instances_parser(new String[]{"--no-such-argument"});
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
            new ec2_describe_instances_parser(new String[]{"--list"});
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
        ec2_describe_instances_parser p = new ec2_describe_instances_parser(new String[]{"--list", "name:public-ip"});

        List<InstanceField> fields = p.getOutputFields();

        assertEquals(2, fields.size());

        assertEquals(InstanceField.NAME, fields.get(0));
        assertEquals(InstanceField.PUBLIC_IP, fields.get(1));
    }

    // command line arguments: --filter --------------------------------------------------------------------------------

    @Test
    public void commandLine_NothingFollowsFilter() throws Exception
    {
        try
        {
            new ec2_describe_instances_parser(new String[]{"--filters"});
            fail("nothing follows --filter, should fail with UserErrorException");
        }
        catch(UserErrorException e)
        {
            log.info(e.getMessage());
        }
    }

    @Test
    public void commandLine_FilterOutput() throws Exception
    {
        ec2_describe_instances_parser p = new ec2_describe_instances_parser(new String[]{"--filter", "state=running"});

        List<Filter> filters = p.getFilters();

        assertEquals(1, filters.size());

    }

    @Test
    public void commandLine_FiltersOutput() throws Exception
    {
        ec2_describe_instances_parser p = new ec2_describe_instances_parser(new String[]{"--filters", "state=running"});

        List<Filter> filters = p.getFilters();

        assertEquals(1, filters.size());

    }

    // parse() ---------------------------------------------------------------------------------------------------------

    @Test
    public void noCommandOutputParsed() throws Exception
    {
        ec2_describe_instances_parser p = new ec2_describe_instances_parser(new String[0]);

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
        ec2_describe_instances_parser p = new ec2_describe_instances_parser(new String[0]);

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

        ec2_describe_instances_parser p = new ec2_describe_instances_parser(new String[0]);
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

        ec2_describe_instances_parser p = new ec2_describe_instances_parser(
            new String[] {"--filters", "state=running", "--list", "name:state:public-ip"});

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

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------
}




