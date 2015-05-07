package com.novaordis.em.ec2.expression;

import com.novaordis.em.ec2.expression.operator.Operator;
import com.novaordis.em.ec2.model.VariableResolver;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2015 Nova Ordis LLC
 */
public class Constant implements Expression
{
    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private final Object value;

    // Constructors ----------------------------------------------------------------------------------------------------

    public Constant(Object value)
    {
        this.value = value;
    }

    // Expression implementation ---------------------------------------------------------------------------------------

    @Override
    public String getLiteral()
    {
        return value == null ? null : value.toString();
    }

    @Override
    public Operator getOperator()
    {
        return null;
    }

    /**
     * @see Expression#evaluate(VariableResolver
     */
    @Override
    public Object evaluate(VariableResolver variableResolver) throws EvaluationException
    {
        return value;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public Object getValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        if (value == null)
        {
            return null;
        }

        if (value instanceof String)
        {
            return "'" + value + "'";
        }

        return value.toString();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------
}




