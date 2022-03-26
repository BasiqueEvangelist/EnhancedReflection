package me.basiqueevangelist.enhancedreflection.impl;

import me.basiqueevangelist.enhancedreflection.api.*;
import me.basiqueevangelist.enhancedreflection.api.typeuse.EUnboundArrayUse;
import me.basiqueevangelist.enhancedreflection.impl.typeuse.EUnboundArrayUseImpl;
import me.basiqueevangelist.enhancedreflection.impl.typeuse.EmptyAnnotatedType;

import java.lang.reflect.AnnotatedType;

public record EUnboundArrayImpl(EType componentType) implements EUnboundArray, ETypeInternal<EUnboundArrayUse> {
    @Override
    public String toString() {
        return componentType.toString() + "[]";
    }

    @Override
    public EType tryResolve(GenericTypeContext ctx, EncounteredTypes encounteredTypes) {
        EType newType = componentType.tryResolve(ctx, encounteredTypes);

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

    @Override
    public EUnboundArrayUse asEmptyUse() {
        return asUseWith(EmptyAnnotatedType.INSTANCE);
    }

    @Override
    public EUnboundArrayUse asUseWith(AnnotatedType data) {
        return new EUnboundArrayUseImpl(data, componentType.asEmptyUse());
    }
}
