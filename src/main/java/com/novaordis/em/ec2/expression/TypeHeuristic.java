package com.novaordis.em.ec2.expression;

import com.novaordis.em.ec2.model.Instance;
import com.novaordis.em.ec2.model.InstanceState;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2015 Nova Ordis LLC
 */
public class TypeHeuristic
{
    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * Equality taking into consideration type heuristic. Needed for expression evaluation.
     */
    public static boolean equals(Object o, Object o2)
    {
        if (o == null || o2 == null)
        {
            return false;
        }

        // special consideration for enums

        if (o instanceof InstanceState || o2 instanceof InstanceState)
        {
            // try to convert both to InstanceState
            InstanceState is =
                o instanceof InstanceState ? (InstanceState)o : InstanceState.toInstanceState(o.toString());
            InstanceState is2 =
                o2 instanceof InstanceState ? (InstanceState)o2 : InstanceState.toInstanceState(o2.toString());

            return is != null && is.equals(is2);
        }

        return o.equals(o2);
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    private TypeHeuristic()
    {
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------
}




