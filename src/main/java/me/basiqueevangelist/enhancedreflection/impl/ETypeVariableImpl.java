package me.basiqueevangelist.enhancedreflection.impl;

import me.basiqueevangelist.enhancedreflection.api.*;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;

public class ETypeVariableImpl implements ETypeVariable {
    private final TypeVariable<?> raw;
    private List<EType> bounds;

    public ETypeVariableImpl(TypeVariable<?> raw, List<EType> bounds) {
        this.raw = raw;
        this.bounds = bounds;
    }

    public ETypeVariableImpl(TypeVariable<?> raw) {
        this.raw = raw;
        this.bounds = null;
    }

    public void setBounds(List<EType> bounds) {
        if (this.bounds != null) throw new IllegalStateException("Bounds have already been set!");

        this.bounds = bounds;
    }

    @Override
    public String name() {
        return raw.getName();
    }

    @Override
    public @Unmodifiable List<EType> bounds() {
        return bounds;
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
        EType newType = ctx.resolveTypeVariable(this);

        if (newType != this) return newType;

        boolean changed = false;
        EType[] newBounds = new EType[bounds.size()];
        for (int i = 0; i < bounds.size(); i++) {
            newBounds[i] = bounds.get(i).tryResolve(ctx);

            if (newBounds[i] != bounds.get(i)) changed = true;
        }

        if (changed) return new ETypeVariableImpl(raw, List.of(newBounds));
        else return this;
    }

    @Override
    public EClass<?> upperBound() {
        if (bounds().size() > 0)
            return bounds().get(0).lowerBound();
        else
            return CommonTypes.OBJECT;
    }

    @Override
    public int hashCode() {
        return raw.hashCode();
    }
}
