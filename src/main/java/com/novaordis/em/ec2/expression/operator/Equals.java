package com.novaordis.em.ec2.expression.operator;

import com.novaordis.em.ec2.expression.Expression;
import org.apache.log4j.Logger;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2015 Nova Ordis LLC
 */
public class Equals implements BinaryOperator
{
    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(Equals.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // BinaryOperator implementation -----------------------------------------------------------------------------------

    @Override
    public String getLiteral()
    {
        return "=";
    }

    @Override
    public Object evaluate(Expression leftOperand, Expression rightOperand)
    {
        throw new RuntimeException("NYE");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString()
    {
        return "=";
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------
}




