package com.novaordis.em.ec2.expression;

import com.novaordis.em.ec2.model.Field;
import com.novaordis.em.ec2.model.Instance;
import com.novaordis.em.ec2.model.InstanceField;
import com.novaordis.em.ec2.model.VariableResolver;
import org.apache.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2015 Nova Ordis LLC
 */
public class FieldBasedVariableTest extends VariableTest
{
    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(FieldBasedVariableTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void evaluate() throws Exception
    {
        FieldBasedVariable fbv = getExpressionToTest("name");

        Instance instance = new Instance();
        instance.setName("apple");

        Object result = fbv.evaluate(instance);
        assertEquals("apple", result);
    }

    @Test
    public void evaluate_nullValue() throws Exception
    {
        FieldBasedVariable fbv = getExpressionToTest("name");

        Instance instance = new Instance();

        Object result = fbv.evaluate(instance);
        assertNull(result);
    }

    @Test
    public void evaluate_notAFieldResolver() throws Exception
    {
        FieldBasedVariable fbv = getExpressionToTest("name");

        VariableResolver gardenVarietyVariableResolver = new VariableResolver()
        {
            @Override
            public Object getValue(Variable variable)
            {
                throw new RuntimeException("NOT YET IMPLEMENTED");
            }
        };

        try
        {
            fbv.evaluate(gardenVarietyVariableResolver);
            fail("should fail because we don't pass a FieldResolver");
        }
        catch(IllegalArgumentException iae)
        {
            log.info(iae.getMessage());
        }
    }


    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected FieldBasedVariable getExpressionToTest(String literal) throws Exception
    {
        Field field = InstanceField.toInstanceField(literal);
        return new FieldBasedVariable(field);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------
}




