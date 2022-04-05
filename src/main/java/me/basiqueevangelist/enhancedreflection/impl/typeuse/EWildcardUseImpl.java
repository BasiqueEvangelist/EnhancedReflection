package me.basiqueevangelist.enhancedreflection.impl.typeuse;

import me.basiqueevangelist.enhancedreflection.api.EType;
import me.basiqueevangelist.enhancedreflection.api.EWildcard;
import me.basiqueevangelist.enhancedreflection.api.EncounteredTypes;
import me.basiqueevangelist.enhancedreflection.api.GenericTypeContext;
import me.basiqueevangelist.enhancedreflection.api.typeuse.ETypeUse;
import me.basiqueevangelist.enhancedreflection.api.typeuse.EWildcardUse;
import me.basiqueevangelist.enhancedreflection.impl.EAnnotatedImpl;
import me.basiqueevangelist.enhancedreflection.impl.EWildcardImpl;
import me.basiqueevangelist.enhancedreflection.impl.MappedImmutableList;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.reflect.AnnotatedType;
import java.util.List;

public class EWildcardUseImpl extends EAnnotatedImpl<AnnotatedType> implements EWildcardUse {
    private List<ETypeUse> lowerBounds;
    private List<ETypeUse> upperBounds;
    private EWildcard type;

    public EWildcardUseImpl(AnnotatedType raw, List<ETypeUse> lowerBounds, List<ETypeUse> upperBounds) {
        super(raw);

        init(lowerBounds, upperBounds);
    }

    public EWildcardUseImpl(AnnotatedType raw, EWildcard type) {
        super(raw);

        this.lowerBounds = new MappedImmutableList<>(type.lowerBounds(), EType::asEmptyUse);
        this.upperBounds = new MappedImmutableList<>(type.upperBounds(), EType::asEmptyUse);
        this.type = type;
    }

    public EWildcardUseImpl(AnnotatedType raw) {
        super(raw);

        this.lowerBounds = null;
        this.upperBounds = null;
        this.type = null;
    }

    public void init(List<ETypeUse> lowerBounds, List<ETypeUse> upperBounds) {
        if (this.lowerBounds != null) throw new IllegalStateException("Tried to initialize twice!");

        this.lowerBounds = lowerBounds;
        this.upperBounds = upperBounds;

        this.type = new EWildcardImpl(new MappedImmutableList<>(lowerBounds, ETypeUse::type), new MappedImmutableList<>(upperBounds, ETypeUse::type));
    }

    @Override
    public ETypeUse tryResolve(GenericTypeContext ctx, EncounteredTypes encounteredTypes) {
        if (!encounteredTypes.addTypeUse(this))
            return this;

        ETypeUse[] upperBounds = new ETypeUse[upperBounds().size()];
        ETypeUse[] lowerBounds = new ETypeUse[lowerBounds().size()];
        boolean changed = false;

        for (int i = 0; i < upperBounds().size(); i++) {
            ETypeUse oldType = upperBounds().get(i);
            ETypeUse newType = upperBounds[i] = oldType.tryResolve(ctx, encounteredTypes);

            if (oldType != newType)
                changed = true;
        }

        for (int i = 0; i < lowerBounds().size(); i++) {
            ETypeUse oldType = lowerBounds().get(i);
            ETypeUse newType = lowerBounds[i] = oldType.tryResolve(ctx, encounteredTypes);

            if (oldType != newType)
                changed = true;
        }

        try {
            if (changed)
                return new EWildcardUseImpl(raw, List.of(lowerBounds), List.of(upperBounds));
            else
                return this;
        } finally {
            encounteredTypes.removeTypeUse(this);
        }
    }

    @Override
    public @Unmodifiable List<ETypeUse> upperBounds() {
        return upperBounds;
    }

    @Override
    public @Unmodifiable List<ETypeUse> lowerBounds() {
        return lowerBounds;
    }

    @Override
    public EWildcard type() {
        return type;
    }

    @Override
    public AnnotatedType raw() {
        return raw;
    }
}
