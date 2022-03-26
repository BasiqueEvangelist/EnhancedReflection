package me.basiqueevangelist.enhancedreflection.impl;

import me.basiqueevangelist.enhancedreflection.api.*;
import me.basiqueevangelist.enhancedreflection.api.typeuse.ETypeUse;
import me.basiqueevangelist.enhancedreflection.api.typeuse.ETypeVariableUse;
import me.basiqueevangelist.enhancedreflection.impl.typeuse.ETypeVariableUseImpl;
import me.basiqueevangelist.enhancedreflection.impl.typeuse.EmptyAnnotatedType;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.TypeVariable;
import java.util.List;

public class ETypeVariableImpl implements ETypeVariable, ETypeInternal<ETypeVariableUse> {
    private final TypeVariable<?> raw;
    private List<ETypeUse> bounds;
    private List<EType> typeBounds;

    public ETypeVariableImpl(TypeVariable<?> raw, List<ETypeUse> bounds) {
        this.raw = raw;
        init(bounds);
    }

    public ETypeVariableImpl(TypeVariable<?> raw) {
        this.raw = raw;
        this.bounds = null;
    }

    public void init(List<ETypeUse> bounds) {
        if (this.bounds != null) throw new IllegalStateException("Tried to initialize twice!");

        this.bounds = bounds;
        this.typeBounds = new MappedImmutableList<>(bounds, ETypeUse::type);
    }

    @Override
    public String name() {
        return raw.getName();
    }

    @Override
    public @Unmodifiable List<EType> bounds() {
        return typeBounds;
    }

    @Override
    public @Unmodifiable List<ETypeUse> annotatedBounds() {
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
    public ETypeVariableUse asUseWith(AnnotatedType data) {
        return new ETypeVariableUseImpl(data, this);
    }

    @Override
    public ETypeVariableUse asEmptyUse() {
        return asUseWith(EmptyAnnotatedType.INSTANCE);
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
    public EType tryResolve(GenericTypeContext ctx, EncounteredTypes encounteredTypes) {
        EType newType = ctx.resolveTypeVariable(this);

        if (newType != this) return newType;

        if (!encounteredTypes.addType(this))
            return this;

        boolean changed = false;
        ETypeUse[] newBounds = new ETypeUse[bounds.size()];
        for (int i = 0; i < bounds.size(); i++) {
            newBounds[i] = bounds.get(i).tryResolve(ctx, encounteredTypes);

            if (newBounds[i] != bounds.get(i)) changed = true;
        }

        try {
            if (changed) return new ETypeVariableImpl(raw, List.of(newBounds));
            else return this;
        } finally {
            encounteredTypes.removeType(this);
        }
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
