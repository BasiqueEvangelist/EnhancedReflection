package me.basiqueevangelist.enhancedreflection.impl;

import me.basiqueevangelist.enhancedreflection.api.*;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Set;

public class EWildcardImpl implements EWildcard {
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

    public void setLowerBounds(List<EType> lowerBounds) {
        if (this.lowerBounds != null) throw new IllegalStateException("Lower bounds have already been set!");

        this.lowerBounds = lowerBounds;
    }

    public void setUpperBounds(List<EType> upperBounds) {
        if (this.upperBounds != null) throw new IllegalStateException("Upper bounds have already been set!");

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
    public EType tryResolve(GenericTypeContext ctx, Set<EType> encounteredTypes) {
        if (!encounteredTypes.add(this))
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
            encounteredTypes.remove(this);
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
