package com.novaordis.em.ec2.expression;

import com.novaordis.em.ec2.expression.operator.Operator;
import com.novaordis.em.ec2.model.VariableResolver;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2015 Nova Ordis LLC
 */
public interface Expression
{
    String getLiteral();

    /**
     * May return null if the expression has no operator (it's a Constant or a Variable, for example).
     */
    Operator getOperator();

    /**
     * @param resolver - the source of values for variables, in case the expression contains variables that need to be
     *                 resolved. If the resolver is not needed during the evaluation of the expression, null is legal.
     *                 Otherwise, if the resolver is null and variables need to be resolved, the method will throw
     *                 IllegalArgumentException.
     *
     * @exception java.lang.IllegalArgumentException if variables need to be evaluated and the resolver is null.
     * @exception com.novaordis.em.ec2.expression.EvaluationException
     */
    Object evaluate(VariableResolver resolver) throws EvaluationException;

}