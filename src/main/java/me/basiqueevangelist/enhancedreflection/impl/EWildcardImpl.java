package me.basiqueevangelist.enhancedreflection.impl;

import me.basiqueevangelist.enhancedreflection.api.*;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.List;

public class EWildcardImpl implements EWildcard {
    private final WildcardType raw;
    private final Lazy<List<EType>> lowerBounds;
    private final Lazy<List<EType>> upperBounds;

    public EWildcardImpl(WildcardType raw) {
        this.raw = raw;

        this.lowerBounds = new Lazy<>(() -> {
            Type[] jTypes = raw.getLowerBounds();
            EType[] types = new EType[jTypes.length];

            for (int i = 0; i < types.length; i++) {
                types[i] = EType.fromJava(jTypes[i]);
            }

            return List.of(types);
        });

        this.upperBounds = new Lazy<>(() -> {
            Type[] jTypes = raw.getUpperBounds();
            EType[] types = new EType[jTypes.length];

            for (int i = 0; i < types.length; i++) {
                types[i] = EType.fromJava(jTypes[i]);
            }

            return List.of(types);
        });
    }

    @Override
    public @Unmodifiable List<EType> upperBounds() {
        return upperBounds.get();
    }

    @Override
    public @Unmodifiable List<EType> lowerBounds() {
        return lowerBounds.get();
    }

    @Override
    public WildcardType raw() {
        return raw;
    }

    @Override
    public String toString() {
        return raw.toString();
    }

    @Override
    public EType tryResolve(GenericTypeContext ctx) {
        return this;
    }

    @Override
    public EClass<?> toClass() {
        if (upperBounds().size() > 0)
            return upperBounds().get(0).toClass();
        else
            return CommonTypes.OBJECT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EWildcardImpl eWildcard = (EWildcardImpl) o;

        return raw.equals(eWildcard.raw);
    }

    @Override
    public int hashCode() {
        return raw.hashCode();
    }
}
