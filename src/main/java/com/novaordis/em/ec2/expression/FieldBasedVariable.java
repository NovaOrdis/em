package com.novaordis.em.ec2.expression;

import com.novaordis.em.ec2.model.Field;
import com.novaordis.em.ec2.model.FieldResolver;
import com.novaordis.em.ec2.model.VariableResolver;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2015 Nova Ordis LLC
 */

public class FieldBasedVariable extends Variable
{
    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private Field delegate;

    // Constructors ----------------------------------------------------------------------------------------------------

    public FieldBasedVariable(Field field)
    {
        this.delegate = field;
    }

    // Variable implementation -----------------------------------------------------------------------------------------

    @Override
    public String getLiteral()
    {
        if (delegate == null)
        {
            return null;
        }

        return delegate.getLiteral();
    }

    /**
     * @see Expression#evaluate(com.novaordis.em.ec2.model.VariableResolver)
     */
    @Override
    public Object evaluate(VariableResolver variableResolver) throws EvaluationException
    {
        // we're dealing with fields, so we're expecting a field resolved
        if (!(variableResolver instanceof FieldResolver))
        {
            throw new IllegalArgumentException("we need a FieldResolver and we got this: " + variableResolver);
        }

        FieldResolver fieldResolver = (FieldResolver)variableResolver;
        return fieldResolver.getValue(delegate);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public Field getDelegate()
    {
        return delegate;
    }

    // rely on superclass
//    @Override
//    public String toString()
//    {
//    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------
}




