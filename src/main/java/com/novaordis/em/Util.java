package com.novaordis.em;

import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2015 Nova Ordis LLC
 */
public class Util
{
    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(Util.class);

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * Dynamically turn on debugging if "--debug" is among arguments.
     */
    public static void enableDebugDynamically()
    {
        Category root = Logger.getLogger(Util.class);
        Category parent;
        while((parent = root.getParent()) != null)
        {
            root = parent;
        }

        root.setLevel(Level.DEBUG);

        log.debug("enabled debug dynamically");
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    private Util()
    {
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------
}




