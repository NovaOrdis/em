package com.novaordis.em.ec2.model;

import com.novaordis.em.ec2.expression.FieldBasedVariable;
import com.novaordis.em.ec2.expression.Variable;

/**
 * We can afford to use a static singleton as no state is required.
 *
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2015 Nova Ordis LLC
 */
public class FieldBasedVariableBuilder implements VariableBuilder
{
    // Constants -------------------------------------------------------------------------------------------------------

    private static final FieldBasedVariableBuilder INSTANCE = new FieldBasedVariableBuilder();

    // Static ----------------------------------------------------------------------------------------------------------

    public static VariableBuilder getInstance()
    {
        return INSTANCE;
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    private FieldBasedVariableBuilder()
    {
    }

    // VariableBuilder implementation ----------------------------------------------------------------------------------

    /**
     * Not optimal, we create an instance unnecessarily. We can implement more efficiently if needed.
     *
     * @see VariableBuilder#isVariable(String)
     */
    @Override
    public boolean isVariable(String literal)
    {
        return buildVariable(literal) != null;
    }

    /**
     * @see VariableBuilder#buildVariable(String)
     */
    @Override
    public Variable buildVariable(String literal)
    {
        for(InstanceField f : InstanceField.values())
        {
            // capitalization matters
            if (f.getLiteral().toLowerCase().equals(literal))
            {
                return new FieldBasedVariable(f);
            }
        }

        return null;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------
}




