package me.basiqueevangelist.enhancedreflection.impl;

import me.basiqueevangelist.enhancedreflection.api.EClass;
import me.basiqueevangelist.enhancedreflection.api.EType;
import me.basiqueevangelist.enhancedreflection.api.EUnboundArray;
import me.basiqueevangelist.enhancedreflection.api.GenericTypeContext;

public record EUnboundArrayImpl(EType componentType) implements EUnboundArray {
    @Override
    public String toString() {
        return componentType.toString() + "[]";
    }

    @Override
    public EType tryResolve(GenericTypeContext ctx) {
        EType newType = componentType.tryResolve(ctx);

        if (newType instanceof EClass<?> klass) {
            return new GenericArrayEClassImpl<>(klass);
        } else if (newType != componentType) {
            return new EUnboundArrayImpl(newType);
        } else {
            return this;
        }
    }

    @Override
    public EUnboundArray arrayOf() {
        return new EUnboundArrayImpl(this);
    }
}
