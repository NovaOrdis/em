package com.novaordis.em.ec2.model;

import org.apache.log4j.Logger;

/**
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

    private InstanceField field;
    private Object fieldValue;

    // Constructors ----------------------------------------------------------------------------------------------------

    public Filter(InstanceField field, String value)
    {
        this.field = field;
        this.fieldValue = field.fromString(value);
        log.debug(this + " created");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public boolean allows(Instance instance)
    {
        Object instanceValue = instance.get(field);

        //noinspection UnnecessaryLocalVariable
        boolean allowed = instanceValue != null && instanceValue.equals(fieldValue);
        return allowed;
    }

    @Override
    public String toString()
    {
        return field + "=" + fieldValue;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------
}




