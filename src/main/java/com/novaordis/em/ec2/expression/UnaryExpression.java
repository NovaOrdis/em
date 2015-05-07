package com.novaordis.em.ec2.expression;

import com.novaordis.em.ec2.expression.operator.Operator;
import com.novaordis.em.ec2.expression.operator.UnaryOperator;
import com.novaordis.em.ec2.model.VariableResolver;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2015 Nova Ordis LLC
 */
public class UnaryExpression implements Expression
{
    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private UnaryOperator operator;
    private Expression operand;

    // Constructors ----------------------------------------------------------------------------------------------------

    public UnaryExpression(UnaryOperator operator, Expression operand)
        throws InvalidExpressionException
    {
        this.operator = operator;
        this.operand = operand;
    }

    // Expression implementation ---------------------------------------------------------------------------------------

    @Override
    public String getLiteral()
    {
        return operator.getLiteral() + operand.getLiteral();
    }

    @Override
    public Operator getOperator()
    {
        return operator;
    }

    /**
     * @see com.novaordis.em.ec2.expression.Expression#evaluate(com.novaordis.em.ec2.model.VariableResolver
     */
    @Override
    public Boolean evaluate(VariableResolver variableResolver) throws EvaluationException
    {
        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------
}




