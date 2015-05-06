package com.novaordis.em.ec2.output;

import com.novaordis.em.ec2.model.Instance;
import com.novaordis.em.ec2.model.InstanceField;
import com.novaordis.em.ec2.model.InstanceState;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2015 Nova Ordis LLC
 */
public class OutputTest
{
    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(OutputTest.class);


    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // list() ----------------------------------------------------------------------------------------------------------

    @Test
    public void list_EmptyInstanceList() throws Exception
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        List<InstanceField> fields = Arrays.asList(InstanceField.NAME);

        Output.list(ps, fields, new ArrayList<Instance>());

        ps.close();

        assertEquals(0, baos.toByteArray().length);
    }

    @Test
    public void list() throws Exception
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        List<InstanceField> fields = Arrays.asList(InstanceField.ID);
        List<Instance> instances = Arrays.asList(new Instance("i-db0ab82d"), new Instance("i-0ea9c7f8"));

        Output.list(ps, fields, instances);

        ps.close();

        String output = new String(baos.toByteArray());
        assertEquals("i-db0ab82d i-0ea9c7f8\n", output);
    }

    @Test
    public void list_NonExistentValues() throws Exception
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        List<InstanceField> fields = Arrays.asList(InstanceField.NAME);
        List<Instance> instances = Arrays.asList(new Instance("i-db0ab82d"), new Instance("i-0ea9c7f8"));

        // we did not specify a name, we expect "N/A N/A\n"

        Output.list(ps, fields, instances);

        ps.close();

        String output = new String(baos.toByteArray());
        assertEquals("N/A N/A\n", output);
    }

    // table() ---------------------------------------------------------------------------------------------------------

    @Test
    public void table_emptyInstanceList() throws Exception
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        List<InstanceField> fields = Arrays.asList(InstanceField.NAME, InstanceField.ID);
        List<Instance> instances = new ArrayList<>();

        Output.table(ps, fields, instances);

        ps.close();

        String output = new String(baos.toByteArray());
        assertEquals("no instances\n", output);
    }

    @Test
    public void table() throws Exception
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        List<InstanceField> fields = Arrays.asList(InstanceField.NAME, InstanceField.ID, InstanceField.STATE);

        Instance instance = new Instance("i-db0ab82d");
        instance.setName("A");
        instance.setState(InstanceState.STOPPED);

        Instance instance2 = new Instance("i-0ea9c7f8");
        instance2.setName("Instance with a very long name");
        instance2.setState(InstanceState.RUNNING);

        List<Instance> instances = Arrays.asList(instance, instance2);

        Output.table(ps, fields, instances);

        ps.close();

        String output = new String(baos.toByteArray());

        log.info("\n#################################################\n"+output);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------
}




