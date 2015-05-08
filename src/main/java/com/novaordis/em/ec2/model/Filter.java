package com.novaordis.em.ec2.model;

import com.novaordis.em.ec2.expression.Expression;
import com.novaordis.em.ec2.expression.ExpressionFactory;
import org.apache.log4j.Logger;

/**
 * The encapsulation of a logical expression that must evaluate to true in order for the underlying instance to be
 * included in the result.
 *
 * Examples:
 *
 * name=f01
 *
 * name=f01|f02|f03
 *
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2015 Nova Ordis LLC
 */
public class Filter
{
    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(Filter.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private Expression expression;

    // Constructors ----------------------------------------------------------------------------------------------------

    public Filter(String expressionLiteral) throws Exception
    {
        this.expression = ExpressionFactory.create(FieldBasedVariableBuilder.getInstance(), expressionLiteral);
        log.debug(this + " created");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * May return null.
     */
    public String getLiteral()
    {
        return expression == null ? null : expression.getLiteral();
    }

    public boolean allows(Instance instance) throws Exception
    {
        boolean allowed = (Boolean)expression.evaluate(instance);
        log.debug("instance " + instance + " is " + (allowed ? "allowed" : "rejected") + " by filter " + this);
        return allowed;
    }

    @Override
    public String toString()
    {
        return "" + expression;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------
}




