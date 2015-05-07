package com.novaordis.em.ec2.model;

import com.novaordis.em.ec2.expression.Variable;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2015 Nova Ordis LLC
 */
public interface VariableBuilder
{
    boolean isVariable(String literal);

    /**
     * Capitalization matters.
     *
     * @return null if it cannot build a Variable that corresponds to the given literal
     */
    Variable buildVariable(String literal);
}
