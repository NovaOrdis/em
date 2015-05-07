package com.novaordis.em.ec2.expression;

import com.novaordis.em.ec2.expression.operator.Operator;
import com.novaordis.em.ec2.model.VariableResolver;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2015 Nova Ordis LLC
 */
public class Variable implements Expression
{
    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Expression implementation ---------------------------------------------------------------------------------------

    @Override
    public String getLiteral()
    {
        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    @Override
    public Operator getOperator()
    {
        return null;
    }

    /**
     * @see Expression#evaluate(VariableResolver)
     */
    @Override
    public Object evaluate(VariableResolver variableResolver) throws EvaluationException
    {
        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public String toString()
    {
        String literal = getLiteral();

        if (literal == null)
        {
            return "null";
        }

        return "${" + literal + "}";
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------
}




