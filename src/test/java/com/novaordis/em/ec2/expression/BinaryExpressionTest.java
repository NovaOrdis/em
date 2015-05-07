package com.novaordis.em.ec2.expression;

import com.novaordis.em.ec2.model.FieldBasedVariableBuilder;
import com.novaordis.em.ec2.model.Instance;
import com.novaordis.em.ec2.model.InstanceState;
import com.novaordis.em.ec2.model.VariableBuilder;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2015 Nova Ordis LLC
 */
public class BinaryExpressionTest extends ExpressionTest
{
    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Equals ----------------------------------------------------------------------------------------------------------

    @Test
    public void evaluateEquals_Name_Equality() throws Exception
    {
        VariableBuilder fbvb = FieldBasedVariableBuilder.getInstance();
        Expression e = ExpressionFactory.create(fbvb, "name=orange");
        Instance instance = new Instance();
        instance.setName("orange");
        Object result = e.evaluate(instance);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean)result);
    }

    @Test
    public void evaluateEquals_Name_NonEquality() throws Exception
    {
        VariableBuilder fbvb = FieldBasedVariableBuilder.getInstance();
        Expression e = ExpressionFactory.create(fbvb, "name=orange");
        Instance instance = new Instance();
        instance.setName("apple");
        Object result = e.evaluate(instance);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void evaluateEquals_State_Equality() throws Exception
    {
        VariableBuilder fbvb = FieldBasedVariableBuilder.getInstance();
        Expression e = ExpressionFactory.create(fbvb, "state=running");
        Instance instance = new Instance();
        instance.setState(InstanceState.RUNNING);
        Object result = e.evaluate(instance);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean)result);
    }

    // Or --------------------------------------------------------------------------------------------------------------

    @Test
    public void evaluateOr_ReturnsTrue_FirstExpression() throws Exception
    {
        VariableBuilder fbvb = FieldBasedVariableBuilder.getInstance();
        Expression e = ExpressionFactory.create(fbvb, "name=orange|apple");
        Instance instance = new Instance();
        instance.setName("orange");
        Object result = e.evaluate(instance);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean)result);
    }

    @Test
    public void evaluateOr_ReturnsTrue_SecondExpression() throws Exception
    {
        VariableBuilder fbvb = FieldBasedVariableBuilder.getInstance();
        Expression e = ExpressionFactory.create(fbvb, "name=orange|apple");
        Instance instance = new Instance();
        instance.setName("apple");
        Object result = e.evaluate(instance);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean)result);
    }

    @Test
    public void evaluateOr_ReturnsFalse() throws Exception
    {
        VariableBuilder fbvb = FieldBasedVariableBuilder.getInstance();
        Expression e = ExpressionFactory.create(fbvb, "name=orange|apple");
        Instance instance = new Instance();
        instance.setName("lemon");
        Object result = e.evaluate(instance);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected BinaryExpression getExpressionToTest(String literal) throws Exception
    {
        throw new Exception("NYE");
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------
}




