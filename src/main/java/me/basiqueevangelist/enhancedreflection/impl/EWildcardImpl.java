package me.basiqueevangelist.enhancedreflection.impl;

import me.basiqueevangelist.enhancedreflection.api.*;
import me.basiqueevangelist.enhancedreflection.api.typeuse.EWildcardUse;
import me.basiqueevangelist.enhancedreflection.impl.typeuse.EWildcardUseImpl;
import me.basiqueevangelist.enhancedreflection.impl.typeuse.EmptyAnnotatedType;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.reflect.AnnotatedType;
import java.util.List;

public class EWildcardImpl implements EWildcard, ETypeInternal<EWildcardUse> {
    private List<EType> lowerBounds;
    private List<EType> upperBounds;

    public EWildcardImpl(List<EType> lowerBounds, List<EType> upperBounds) {
        this.lowerBounds = lowerBounds;
        this.upperBounds = upperBounds;
    }

    public EWildcardImpl() {
        this.lowerBounds = null;
        this.upperBounds = null;
    }

    public void init(List<EType> lowerBounds, List<EType> upperBounds) {
        if (this.lowerBounds != null) throw new IllegalStateException("Tried to initialize twice!");

        this.lowerBounds = lowerBounds;
        this.upperBounds = upperBounds;
    }

    @Override
    public @Unmodifiable List<EType> upperBounds() {
        return upperBounds;
    }

    @Override
    public @Unmodifiable List<EType> lowerBounds() {
        return lowerBounds;
    }

    @Override
    public EUnboundArray arrayOf() {
        return new EUnboundArrayImpl(this);
    }

    @Override
    public EWildcardUse asUseWith(AnnotatedType data) {
        return new EWildcardUseImpl(data, this);
    }

    @Override
    public EWildcardUse asEmptyUse() {
        return asUseWith(EmptyAnnotatedType.INSTANCE);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("?");

        if (lowerBounds.size() > 0) {
            sb.append(" super ");

            for (int i = 0; i < lowerBounds.size(); i++) {
                if (i > 0) sb.append(" & ");

                sb.append(lowerBounds.get(i));
            }
        } else if (upperBounds.size() > 0 && !upperBounds.get(0).equals(CommonTypes.OBJECT)) {
            sb.append(" extends ");

            for (int i = 0; i < upperBounds.size(); i++) {
                if (i > 0) sb.append(" & ");

                sb.append(upperBounds.get(i));
            }
        }

        return sb.toString();
    }

    @Override
    public EType tryResolve(GenericTypeContext ctx, EncounteredTypes encounteredTypes) {
        if (!encounteredTypes.addType(this))
            return this;

        EType[] upperBounds = new EType[upperBounds().size()];
        EType[] lowerBounds = new EType[lowerBounds().size()];
        boolean changed = false;

        for (int i = 0; i < upperBounds().size(); i++) {
            EType oldType = upperBounds().get(i);
            EType newType = upperBounds[i] = oldType.tryResolve(ctx, encounteredTypes);

            if (oldType != newType)
                changed = true;
        }

        for (int i = 0; i < lowerBounds().size(); i++) {
            EType oldType = lowerBounds().get(i);
            EType newType = lowerBounds[i] = oldType.tryResolve(ctx, encounteredTypes);

            if (oldType != newType)
                changed = true;
        }

        try {
            if (changed)
                return new EWildcardImpl(List.of(lowerBounds), List.of(upperBounds));
            else
                return this;
        } finally {
            encounteredTypes.removeType(this);
        }
    }

    @Override
    public EClass<?> upperBound() {
        if (upperBounds().size() > 0)
            return upperBounds().get(0).lowerBound();
        else
            return CommonTypes.OBJECT;
    }

    @Override
    public EClass<?> lowerBound() {
        if (lowerBounds().size() > 0)
            return lowerBounds().get(0).upperBound();
        else
            return CommonTypes.OBJECT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EWildcardImpl eWildcard = (EWildcardImpl) o;

        if (!lowerBounds.equals(eWildcard.lowerBounds)) return false;
        return upperBounds.equals(eWildcard.upperBounds);
    }

    @Override
    public int hashCode() {
        int result = lowerBounds.hashCode();
        result = 31 * result + upperBounds.hashCode();
        return result;
    }
}
