package com.novaordis.em.ec2.expression;

import com.novaordis.em.ec2.expression.operator.BinaryOperator;
import com.novaordis.em.ec2.expression.operator.Equals;
import com.novaordis.em.ec2.expression.operator.Or;
import com.novaordis.em.ec2.model.VariableResolver;

import javax.lang.model.element.TypeElement;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2015 Nova Ordis LLC
 */
public class BinaryExpression implements Expression
{
    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private BinaryOperator operator;
    private Expression leftOperand;
    private Expression rightOperand;

    // Constructors ----------------------------------------------------------------------------------------------------

    public BinaryExpression() throws InvalidExpressionException
    {
        this(null, null, null);
    }

    public BinaryExpression(Expression leftOperand, BinaryOperator operator, Expression rightOperand)
        throws InvalidExpressionException
    {
        this.operator = operator;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    // Expression implementation ---------------------------------------------------------------------------------------

    @Override
    public String getLiteral()
    {
        return leftOperand.getLiteral() + operator.getLiteral() + rightOperand.getLiteral();
    }

    @Override
    public BinaryOperator getOperator()
    {
        return operator;
    }

    /**
     * @see Expression#evaluate(com.novaordis.em.ec2.model.VariableResolver
     */
    @Override
    public Object evaluate(VariableResolver variableResolver) throws EvaluationException
    {
        if (operator instanceof Equals)
        {
            Object leftValue = leftOperand == null ? null : leftOperand.evaluate(variableResolver);
            Object rightValue = rightOperand == null ? null : rightOperand.evaluate(variableResolver);
            return TypeHeuristic.equals(leftValue, rightValue);
        }
        else if (operator instanceof Or)
        {
            Object leftValue = leftOperand == null ? null : leftOperand.evaluate(variableResolver);
            if (!(leftValue instanceof Boolean))
            {
                throw new EvaluationException(leftOperand + " evaluated to a non-boolean: " + leftValue);
            }

            boolean isTrue = (Boolean)leftValue;
            if (isTrue)
            {
                // no need to evaluate the right expression
                return true;
            }

            Object rightValue = rightOperand == null ? null : rightOperand.evaluate(variableResolver);
            if (!(rightValue instanceof Boolean))
            {
                throw new EvaluationException(rightOperand + " evaluated to a non-boolean: " + rightValue);
            }

            return rightValue;
        }
        else
        {
            throw new RuntimeException("NOT YET IMPLEMENTED: " + operator);
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public void setOperator(BinaryOperator o)
    {
        this.operator = o;
    }

    public Expression getLeftOperand()
    {
        return leftOperand;
    }

    public void setLeftOperand(Expression e)
    {
        this.leftOperand = e;
    }

    public Expression getRightOperand()
    {
        return rightOperand;
    }

    public void setRightOperand(Expression e)
    {
        this.rightOperand = e;
    }

    @Override
    public String toString()
    {
        return
            (leftOperand == null ? "" : leftOperand.toString()) +
                (operator == null ? "" : operator.toString()) +
                (rightOperand == null ? "" : rightOperand.toString());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------
}




