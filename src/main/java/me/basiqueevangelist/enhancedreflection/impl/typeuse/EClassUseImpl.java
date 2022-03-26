package me.basiqueevangelist.enhancedreflection.impl.typeuse;

import me.basiqueevangelist.enhancedreflection.api.EClass;
import me.basiqueevangelist.enhancedreflection.api.EncounteredTypes;
import me.basiqueevangelist.enhancedreflection.api.GenericTypeContext;
import me.basiqueevangelist.enhancedreflection.api.typeuse.EClassUse;
import me.basiqueevangelist.enhancedreflection.api.typeuse.ETypeUse;
import me.basiqueevangelist.enhancedreflection.impl.*;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.reflect.AnnotatedType;
import java.util.List;

public class EClassUseImpl<T> extends EAnnotatedImpl<AnnotatedType> implements EClassUse<T> {
    private final EClass<T> klass;
    private final List<ETypeUse> typeVarValues;

    public EClassUseImpl(AnnotatedType raw, EClass<T> klass) {
        super(raw);

        this.klass = klass;
        this.typeVarValues = new MappedImmutableList<>(klass.typeVariables(), x -> new ETypeVariableUseImpl(EmptyAnnotatedType.INSTANCE, x.raw(), x.annotatedBounds()));
    }

    @Override
    public @Unmodifiable List<ETypeUse> typeVariableValues() {
        return typeVarValues;
    }

    @Override
    public ETypeUse tryResolve(GenericTypeContext ctx, EncounteredTypes encounteredTypes) {
        if (!encounteredTypes.addTypeUse(this))
            return this;

        List<ETypeUse> typeParams = typeVariableValues();
        ETypeUse[] newParamValues = new ETypeUse[typeParams.size()];
        boolean changed = false;
        for (int i = 0; i < newParamValues.length; i++) {
            ETypeUse newValue = typeParams.get(i).tryResolve(ctx, encounteredTypes);
            if (newValue != typeParams.get(i)) {
                changed = true;
            }
            newParamValues[i] = newValue;
        }

        try {
            if (changed)
                return new GenericEClassUseImpl<>(raw, klass.raw(), List.of(newParamValues));
            else
                return this;
        } finally {
            encounteredTypes.removeTypeUse(this);
        }
    }

    @Override
    public EClass<T> type() {
        return klass;
    }
}
