package io.micronaut.data.r2dbc.operations;

import io.micronaut.context.condition.Condition;
import io.micronaut.context.condition.ConditionContext;

public class EnableR2dbcV1Compatibility implements Condition {
    public static boolean ENABLED = false;

    @Override
    public boolean matches(ConditionContext context) {
        return ENABLED;
    }
}
