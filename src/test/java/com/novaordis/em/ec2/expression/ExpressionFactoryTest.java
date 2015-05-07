package com.novaordis.em.ec2.expression;

import com.novaordis.em.ec2.expression.operator.BinaryOperator;
import com.novaordis.em.ec2.expression.operator.Equals;
import com.novaordis.em.ec2.expression.operator.Or;
import com.novaordis.em.ec2.model.Field;
import com.novaordis.em.ec2.model.FieldBasedVariableBuilder;
import com.novaordis.em.ec2.model.InstanceField;
import com.novaordis.em.ec2.model.VariableBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2015 Nova Ordis LLC
 */
public class ExpressionFactoryTest
{
    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void testSimpleConstant() throws Exception
    {
        VariableBuilder variableBuilder = FieldBasedVariableBuilder.getInstance();
        Expression expr = ExpressionFactory.create(variableBuilder, "a");

        assertTrue(expr instanceof Constant);
        Constant c = (Constant)expr;

        Object value = c.getValue();
        assertTrue(value instanceof String);
        assertEquals("a", value);
    }

    @Test
    public void testSimpleField() throws Exception
    {
        VariableBuilder variableBuilder = FieldBasedVariableBuilder.getInstance();
        Expression expr = ExpressionFactory.create(variableBuilder, "name");

        assertTrue(expr instanceof Variable);
        Variable v = (Variable)expr;

        assertTrue(v instanceof FieldBasedVariable);
        FieldBasedVariable fbv = (FieldBasedVariable)v;

        assertEquals("name", fbv.getLiteral());

        Field field = fbv.getDelegate();
        assertEquals("name", field.getLiteral());
    }

    @Test
    public void binaryExpression() throws Exception
    {
        VariableBuilder variableBuilder = FieldBasedVariableBuilder.getInstance();
        Expression expr = ExpressionFactory.create(variableBuilder, "a=a");

        assertTrue(expr instanceof BinaryExpression);
        BinaryExpression binaryExpression = (BinaryExpression)expr;

        Expression leftOperand = binaryExpression.getLeftOperand();
        assertTrue(leftOperand instanceof Constant);
        Constant leftConstant = (Constant)leftOperand;
        assertEquals("a", leftConstant.getLiteral());
        assertEquals("a", leftConstant.getValue());

        Expression rightOperand = binaryExpression.getRightOperand();
        assertTrue(rightOperand instanceof Constant);
        Constant rightConstant = (Constant)rightOperand;
        assertEquals("a", rightConstant.getLiteral());
        assertEquals("a", rightConstant.getValue());

        BinaryOperator operator = binaryExpression.getOperator();
        assertTrue(operator instanceof Equals);

        String literal = expr.getLiteral();
        assertEquals("a=a", literal);
    }

    @Test
    public void binaryExpression2() throws Exception
    {
        VariableBuilder variableBuilder = FieldBasedVariableBuilder.getInstance();
        Expression expr = ExpressionFactory.create(variableBuilder, "a=b");

        assertTrue(expr instanceof BinaryExpression);
        BinaryExpression binaryExpression = (BinaryExpression)expr;

        Expression leftOperand = binaryExpression.getLeftOperand();
        assertTrue(leftOperand instanceof Constant);
        Constant leftConstant = (Constant)leftOperand;
        assertEquals("a", leftConstant.getLiteral());
        assertEquals("a", leftConstant.getValue());

        Expression rightOperand = binaryExpression.getRightOperand();
        assertTrue(rightOperand instanceof Constant);
        Constant rightConstant = (Constant)rightOperand;
        assertEquals("b", rightConstant.getLiteral());
        assertEquals("b", rightConstant.getValue());

        BinaryOperator operator = binaryExpression.getOperator();
        assertTrue(operator instanceof Equals);

        String literal = expr.getLiteral();
        assertEquals("a=b", literal);
    }

    @Test
    public void binaryExpression_FieldEqualsConstant() throws Exception
    {
        VariableBuilder variableBuilder = FieldBasedVariableBuilder.getInstance();
        Expression expr = ExpressionFactory.create(variableBuilder, "name=orange");

        assertTrue(expr instanceof BinaryExpression);
        BinaryExpression binaryExpression = (BinaryExpression)expr;

        Expression leftOperand = binaryExpression.getLeftOperand();
        assertTrue(leftOperand instanceof FieldBasedVariable);
        FieldBasedVariable fbv = (FieldBasedVariable)leftOperand;
        assertEquals(InstanceField.NAME, fbv.getDelegate());

        Expression rightOperand = binaryExpression.getRightOperand();
        assertTrue(rightOperand instanceof Constant);
        Constant rightConstant = (Constant)rightOperand;
        assertEquals("orange", rightConstant.getLiteral());
        assertEquals("orange", rightConstant.getValue());

        BinaryOperator operator = binaryExpression.getOperator();
        assertTrue(operator instanceof Equals);

        String literal = expr.getLiteral();
        assertEquals("name=orange", literal);
    }

    @Test
    public void binaryExpression_FieldEqualsAlternativeConstants() throws Exception
    {
        VariableBuilder variableBuilder = FieldBasedVariableBuilder.getInstance();
        Expression expr = ExpressionFactory.create(variableBuilder, "name=orange|apple|peach");

        assertTrue(expr instanceof BinaryExpression);
        BinaryExpression binaryExpression = (BinaryExpression)expr;
        assertTrue(binaryExpression.getOperator() instanceof Or);

        BinaryExpression lex = (BinaryExpression)binaryExpression.getLeftOperand();
        assertEquals(InstanceField.NAME, ((FieldBasedVariable)lex.getLeftOperand()).getDelegate());
        assertTrue(lex.getOperator() instanceof Equals);
        assertEquals("orange", (((Constant)lex.getRightOperand())).getValue());

        BinaryExpression rex = (BinaryExpression)binaryExpression.getRightOperand();
        assertTrue(rex.getOperator() instanceof Or);

        BinaryExpression lrex = (BinaryExpression)rex.getLeftOperand();
        BinaryExpression rrex = (BinaryExpression)rex.getRightOperand();

        assertEquals(InstanceField.NAME, ((FieldBasedVariable)lrex.getLeftOperand()).getDelegate());
        assertTrue(lrex.getOperator() instanceof Equals);
        assertEquals("apple", (((Constant)lrex.getRightOperand())).getValue());

        assertEquals(InstanceField.NAME, ((FieldBasedVariable)rrex.getLeftOperand()).getDelegate());
        assertTrue(rrex.getOperator() instanceof Equals);
        assertEquals("peach", (((Constant)rrex.getRightOperand())).getValue());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------
}




