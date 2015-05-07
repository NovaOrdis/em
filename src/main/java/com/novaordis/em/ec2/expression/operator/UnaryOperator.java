package com.novaordis.em.ec2.expression.operator;

import com.novaordis.em.ec2.expression.Expression;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2015 Nova Ordis LLC
 */
public interface UnaryOperator extends Operator
{
    Object evaluate(Expression operand);
}
