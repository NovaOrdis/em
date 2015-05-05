package com.novaordis.em.output;

import com.novaordis.em.model.Instance;
import com.novaordis.em.model.InstanceField;
import org.apache.log4j.Logger;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;

/**
 * Static methods to render information as lists, tables, etc. to console.
 *
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2015 Nova Ordis LLC
 */
public class Output
{
    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(Output.class);

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * This is the layer that replaces non-existent values (null) with "N/A".
     */
    public static void render(PrintStream out, OutputFormat outputFormat,
                              List<InstanceField> outputFields, List<Instance> instances)
    {
        if (OutputFormat.list.equals(outputFormat))
        {
            list(out, outputFields, instances);
        }
        else if (OutputFormat.table.equals(outputFormat))
        {
            table(out, outputFields, instances);
        }
        else
        {
            throw new IllegalArgumentException("don't know to handle '" + outputFormat + "' output format");
        }
    }

    public static void list(PrintStream out, List<InstanceField> outputFields, List<Instance> instances)
    {
        boolean output = false;
        for(Iterator<Instance> i = instances.iterator(); i.hasNext(); )
        {
            Instance instance = i.next();

            for(Iterator<InstanceField> fi = outputFields.iterator(); fi.hasNext(); )
            {
                InstanceField f = fi.next();

                String s = f.fromInstance(instance);
                s = s == null ? "N/A" : s;

                out.print(s);
                output = true;

                if (fi.hasNext())
                {
                    out.print(':');
                }
            }

            if (i.hasNext())
            {
                out.print(' ');
            }
        }

        if (output)
        {
            out.print('\n');
            out.flush();
        }
    }

    public static void table(PrintStream out, List<InstanceField> outputFields, List<Instance> instances)
    {
        if (instances.isEmpty())
        {
            out.println("no instances");
            return;
        }

        int widths[] = new int[outputFields.size()];

        // initialize with the size of the output literals
        for(int i = 0; i < outputFields.size(); i ++)
        {
            widths[i] = outputFields.get(i).getOutputLiteral().length();
        }

        // do two passes, first to calibrate and the second to render

        // calibration

        for(Instance instance: instances)
        {
            for(int i = 0; i < outputFields.size(); i ++)
            {
                InstanceField f = outputFields.get(i);

                String rendering = f.fromInstance(instance);
                if (rendering != null && rendering.length() > widths[i])
                {
                    widths[i] = rendering.length();
                }
            }
        }

        // build format strings

        int spacesAfterColumn = 1;
        String format[] = new String[outputFields.size()];
        for(int i = 0; i < outputFields.size(); i ++)
        {
            // do not allow any space after the last column
            spacesAfterColumn = i < outputFields.size() - 1 ? spacesAfterColumn : 0;
            format[i] = "%1$-" + (widths[i] + spacesAfterColumn) + "s";
        }

        // header rendering

        for(int i = 0; i < outputFields.size(); i ++)
        {
            out.print(String.format(format[i], outputFields.get(i).getOutputLiteral()));
        }

        out.println();

        // field rendering

        for(Instance instance: instances)
        {
            for(int i = 0; i < outputFields.size(); i ++)
            {
                InstanceField f = outputFields.get(i);
                String rendering = f.fromInstance(instance);
                rendering = rendering == null ? "" : rendering;
                out.print(String.format(format[i], rendering));
            }

            out.println();
        }

        out.flush();
    }


    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    private Output()
    {
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------
}




