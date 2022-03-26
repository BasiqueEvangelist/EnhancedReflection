package me.basiqueevangelist.enhancedreflection.impl.typeuse;

import me.basiqueevangelist.enhancedreflection.api.EType;
import me.basiqueevangelist.enhancedreflection.api.ETypeVariable;
import me.basiqueevangelist.enhancedreflection.api.EncounteredTypes;
import me.basiqueevangelist.enhancedreflection.api.GenericTypeContext;
import me.basiqueevangelist.enhancedreflection.api.typeuse.ETypeUse;
import me.basiqueevangelist.enhancedreflection.api.typeuse.ETypeVariableUse;
import me.basiqueevangelist.enhancedreflection.impl.EAnnotatedImpl;
import me.basiqueevangelist.enhancedreflection.impl.ETypeInternal;
import me.basiqueevangelist.enhancedreflection.impl.ETypeVariableImpl;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.TypeVariable;
import java.util.List;

public class ETypeVariableUseImpl extends EAnnotatedImpl<AnnotatedType> implements ETypeVariableUse {
    private final TypeVariable<?> rawVar;
    private List<ETypeUse> bounds;
    private ETypeVariable type;

    public ETypeVariableUseImpl(AnnotatedType raw, TypeVariable<?> rawVar, List<ETypeUse> bounds) {
        super(raw);

        this.rawVar = rawVar;
        init(bounds);
    }

    public ETypeVariableUseImpl(AnnotatedType raw, TypeVariable<?> rawVar) {
        super(raw);

        this.rawVar = rawVar;
        this.bounds = null;
    }

    public ETypeVariableUseImpl(AnnotatedType raw, ETypeVariable type) {
        super(raw);

        this.rawVar = type.raw();
        this.bounds = type.annotatedBounds();
        this.type = type;
    }

    public void init(List<ETypeUse> bounds) {
        if (type != null) throw new IllegalStateException("Tried to initialize twice!");

        this.bounds = bounds;
        this.type = new ETypeVariableImpl(rawVar, bounds);
    }

    @Override
    public ETypeUse tryResolve(GenericTypeContext ctx, EncounteredTypes encounteredTypes) {
        EType newVar = ctx.resolveTypeVariable(type);

        if (newVar != type) return ((ETypeInternal<? extends ETypeUse>) newVar).asUseWith(raw);

        if (!encounteredTypes.addTypeUse(this))
            return this;

        boolean changed = false;
        ETypeUse[] newBounds = new ETypeUse[bounds.size()];
        for (int i = 0; i < bounds.size(); i++) {
            newBounds[i] = bounds.get(i).tryResolve(ctx, encounteredTypes);

            if (newBounds[i] != bounds.get(i)) changed = true;
        }

        try {
            if (changed) return new ETypeVariableUseImpl(raw, rawVar, List.of(newBounds));
            else return this;
        } finally {
            encounteredTypes.removeTypeUse(this);
        }
    }

    @Override
    public @Unmodifiable List<ETypeUse> bounds() {
        return null;
    }

    @Override
    public ETypeVariable type() {
        return null;
    }
}
