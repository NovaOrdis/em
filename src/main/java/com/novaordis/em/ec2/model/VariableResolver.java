package com.novaordis.em.ec2.model;

import com.novaordis.em.ec2.expression.Variable;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2015 Nova Ordis LLC
 */
public interface VariableResolver
{
    /**
     * @return null if a value for the given variable is not found.
     */
    Object getValue(Variable variable);

}
