package com.novaordis.em.ec2.expression;

import com.novaordis.em.ec2.expression.operator.BinaryOperator;
import com.novaordis.em.ec2.expression.operator.Equals;
import com.novaordis.em.ec2.expression.operator.Or;
import com.novaordis.em.ec2.model.VariableBuilder;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2015 Nova Ordis LLC
 */
public class ExpressionFactory
{
    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    public static Expression create(VariableBuilder variableBuilder, String literal) throws InvalidExpressionException
    {
        int split;

        if ((split = literal.indexOf('|')) != -1)
        {
            Or or = new Or();

            String left = literal.substring(0, split);
            BinaryExpression lex = (BinaryExpression)create(variableBuilder, left);
            Variable lexVariable = (Variable) lex.getLeftOperand();
            BinaryOperator lexOperator = lex.getOperator();

            BinaryExpression template = new BinaryExpression();
            template.setLeftOperand(lexVariable);
            template.setOperator(lexOperator);

            String right = literal.substring(split + 1);
            Expression rex = createWithLeftTemplate(right, template);

            return new BinaryExpression(lex, or, rex);
        }

        if ((split = literal.indexOf('=')) != -1)
        {
            Equals equals = new Equals();

            String left = literal.substring(0, split);
            Expression lex = create(variableBuilder, left);

            String right = literal.substring(split + 1);
            Expression rex = create(variableBuilder, right);

            return new BinaryExpression(lex, equals, rex);
        }

        //
        // attempt to build a variable
        //

        Variable variable = variableBuilder.buildVariable(literal);

        if (variable != null)
        {
            return variable;
        }

        //
        // attempt to build a constant
        //

        return new Constant(literal); // this throws InvalidExpressionException() in case something goes wrong
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    private ExpressionFactory()
    {
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    private static Expression createWithLeftTemplate(String literal, BinaryExpression template)
        throws InvalidExpressionException
    {
        int split;

        if ((split = literal.indexOf('|')) != -1)
        {
            Or or = new Or();

            String left = literal.substring(0, split);
            Expression lex = createWithLeftTemplate(left, template);

            String right = literal.substring(split + 1);
            Expression rex = createWithLeftTemplate(right, template);

            return new BinaryExpression(lex, or, rex);
        }

        Expression rex = new Constant(literal);
        BinaryExpression binaryExpression = new BinaryExpression();
        binaryExpression.setLeftOperand(template.getLeftOperand());
        binaryExpression.setOperator(template.getOperator());
        binaryExpression.setRightOperand(rex);
        return binaryExpression;
    }

    // Inner classes ---------------------------------------------------------------------------------------------------
}




