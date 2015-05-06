package com.novaordis.em.parser;

import com.novaordis.em.UserErrorException;
import com.novaordis.em.Util;
import com.novaordis.em.model.Filter;
import com.novaordis.em.model.Instance;
import com.novaordis.em.model.InstanceField;
import com.novaordis.em.output.Output;
import com.novaordis.em.output.OutputFormat;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Parses the Amazon EC2 'ec2-describe-instances' command output and re-outputs the result according to the
 * "--output" flag specified on command line. Detailed usage in
 *
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2015 Nova Ordis LLC
 */
public class Ec2DescribeInstancesParser
{
    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(Ec2DescribeInstancesParser.class);

    // Static ----------------------------------------------------------------------------------------------------------

    public static void main(String[] args) throws Exception
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        //noinspection TryFinallyCanBeTryWithResources
        try
        {
            Ec2DescribeInstancesParser p = new Ec2DescribeInstancesParser(args);
            p.parse(br);
            p.output();
        }
        finally
        {
            br.close();
        }
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    private List<Instance> instances;
    private List<InstanceField> outputFields;
    private List<Filter> filters;
    private OutputFormat outputFormat;

    // Constructors ----------------------------------------------------------------------------------------------------

    public Ec2DescribeInstancesParser(String[] args) throws Exception
    {
        outputFields = new ArrayList<>();
        filters = new ArrayList<>();
        parseCommandLine(args);
        log.debug(this + " constructed");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return the list of instances resulted from parsing the 'ec2-describe-instances' command output.
     *
     * @exception IllegalStateException if no command output was parsed,
     */
    public List<Instance> getInstances()
    {
        if (instances == null)
        {
            throw new IllegalStateException("no 'ec2-describe-instances' command output was parsed");
        }

        return instances;
    }

    public List<InstanceField> getOutputFields()
    {
        return outputFields;
    }

    public void output()
    {
        output(System.out);
    }

    public void output(PrintStream out)
    {
        if (instances == null)
        {
            throw new IllegalStateException("no 'ec2-describe-instances' command output was parsed");
        }

        Output.render(out, outputFormat, outputFields, instances);
    }

    public List<Filter> getFilters()
    {
        return filters;
    }

    @Override
    public String toString()
    {
        return "Ec2DescribeInstancesParser[filters: " + filterToString() + ", output: " + outputFieldsToString() + "]";
    }

    // Package protected -----------------------------------------------------------------------------------------------

    void parse(BufferedReader br) throws Exception
    {
        String line;
        InstanceParser instanceParser = new InstanceParser();
        while(((line = br.readLine())) != null)
        {
            if (instances == null)
            {
                instances = new ArrayList<>();
            }

            if (!instanceParser.parse(line))
            {
                addWithFilter(instanceParser.getInstance());

                // start a new parser
                instanceParser = new InstanceParser();

                // feed it the line that was just rejected, must be accepted otherwise we have a problem
                if (!instanceParser.parse(line))
                {
                    throw new IllegalArgumentException("line '" + line + "' not accepted by a new instance parser");
                }
            }
        }

        // add the last instance we did not have a chance to get in the loop
        addWithFilter(instanceParser.getInstance());
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    private void parseCommandLine(String[] args) throws UserErrorException
    {
        for(int i = 0; i < args.length; i ++)
        {
            if ("--debug".equals(args[i]))
            {
                Util.enableDebugDynamically();
            }
            else if ("--list".equalsIgnoreCase(args[i]) || "--table".equalsIgnoreCase(args[i]))
            {
                if (i == args.length - 1)
                {
                    throw new UserErrorException("a format specification (output fields) should follow " + args[i]);
                }

                outputFormat = OutputFormat.valueOf(args[i].substring(2));
                setOutputFields(args[++i]);
            }
            else if ("--filter".equalsIgnoreCase(args[i]) || "--filters".equalsIgnoreCase(args[i]))
            {
                if (i == args.length - 1)
                {
                    throw new UserErrorException("filter definition(s) should follow --filter(s)");
                }

                setFilters(args[++i]);
            }
            else
            {
                throw new UserErrorException("unknown argument: '" + args[i] + "'");
            }
        }
    }

    private void setOutputFields(String arg) throws UserErrorException
    {
        StringTokenizer st = new StringTokenizer(arg, ":");

        while(st.hasMoreTokens())
        {
            String outputFieldString = st.nextToken();

            try
            {
                outputFields.add(InstanceField.toInstanceField(outputFieldString));
            }
            catch(IllegalArgumentException e)
            {
                throw new UserErrorException("unknown output field '" + outputFieldString + "'");
            }
        }
    }

    private String outputFieldsToString()
    {
        String s = "";

        for(Iterator i = outputFields.iterator(); i.hasNext(); )
        {
            s += i.next().toString();

            if (i.hasNext())
            {
                s += ", ";
            }
        }

        return s;
    }

    private void setFilters(String arg) throws UserErrorException
    {
        // currently we only handle just one filter

        int i = arg.indexOf('=');
        String fieldName = arg.substring(0, i);
        String fieldValue = arg.substring(i + 1);
        InstanceField field = InstanceField.toInstanceField(fieldName);
        Filter filter = new Filter(field, fieldValue);
        filters.add(filter);
    }

    /**
     * Add the instance to the internal instance list if it satisfies the filters.
     */
    private void addWithFilter(Instance instance)
    {
        for(Filter f: filters)
        {
            if (!f.allows(instance))
            {
                log.debug("instance " + instance + " was filtered out by " + f);
                return;
            }
        }

        instances.add(instance);
    }

    private String filterToString()
    {
        String s = "";

        for(Iterator i = filters.iterator(); i.hasNext(); )
        {
            s += i.next().toString();

            if (i.hasNext())
            {
                s += ", ";
            }
        }

        return s;
    }


    // Inner classes ---------------------------------------------------------------------------------------------------
}




