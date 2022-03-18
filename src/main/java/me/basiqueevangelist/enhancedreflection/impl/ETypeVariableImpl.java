package me.basiqueevangelist.enhancedreflection.impl;

import me.basiqueevangelist.enhancedreflection.api.*;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;

public class ETypeVariableImpl implements ETypeVariable {
    private final TypeVariable<?> raw;
    private final Lazy<List<EType>> bounds;

    public ETypeVariableImpl(TypeVariable<?> raw) {
        this.raw = raw;

        this.bounds = new Lazy<>(() -> {
            Type[] jBounds = raw.getBounds();
            EType[] bounds = new EType[jBounds.length];

            for (int i = 0; i < bounds.length; i++) {
                bounds[i] = EType.fromJava(jBounds[i]);
            }

            return List.of(bounds);
        });
    }

    @Override
    public String name() {
        return raw.getName();
    }

    @Override
    public @Unmodifiable List<EType> bounds() {
        return bounds.get();
    }

    @Override
    public TypeVariable<?> raw() {
        return raw;
    }

    @Override
    public EUnboundArray arrayOf() {
        return new EUnboundArrayImpl(this);
    }

    @Override
    public String toString() {
        return raw.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ETypeVariableImpl that = (ETypeVariableImpl) o;

        return raw.equals(that.raw);
    }

    @Override
    public EType tryResolve(GenericTypeContext ctx) {
        return ctx.resolveTypeVariable(this);
    }

    @Override
    public EClass<?> toClass() {
        if (bounds().size() > 0)
            return bounds().get(0).toClass();
        else
            return ETypeVariable.super.toClass();
    }

    @Override
    public int hashCode() {
        return raw.hashCode();
    }
}
