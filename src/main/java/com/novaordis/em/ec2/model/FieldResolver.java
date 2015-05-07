package com.novaordis.em.ec2.model;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2015 Nova Ordis LLC
 */
public interface FieldResolver extends VariableResolver
{
    /**
     * @return null if a value for the given field is not found.
     */
    Object getValue(Field field);

}
