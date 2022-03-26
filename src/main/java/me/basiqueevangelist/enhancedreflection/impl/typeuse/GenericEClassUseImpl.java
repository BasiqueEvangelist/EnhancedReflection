package me.basiqueevangelist.enhancedreflection.impl.typeuse;

import me.basiqueevangelist.enhancedreflection.api.EClass;
import me.basiqueevangelist.enhancedreflection.api.EType;
import me.basiqueevangelist.enhancedreflection.api.EncounteredTypes;
import me.basiqueevangelist.enhancedreflection.api.GenericTypeContext;
import me.basiqueevangelist.enhancedreflection.api.typeuse.EClassUse;
import me.basiqueevangelist.enhancedreflection.api.typeuse.ETypeUse;
import me.basiqueevangelist.enhancedreflection.impl.EAnnotatedImpl;
import me.basiqueevangelist.enhancedreflection.impl.GenericEClassImpl;
import me.basiqueevangelist.enhancedreflection.impl.MappedImmutableList;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.reflect.AnnotatedType;
import java.util.List;

public class GenericEClassUseImpl<T> extends EAnnotatedImpl<AnnotatedType> implements EClassUse<T> {
    final Class<T> sourceClass;
    EClass<T> klass;
    List<ETypeUse> typeVarValues;

    public GenericEClassUseImpl(AnnotatedType raw, Class<T> sourceClass, List<ETypeUse> typeVarValues) {
        super(raw);

        this.sourceClass = sourceClass;
        init(typeVarValues);
    }

    public GenericEClassUseImpl(AnnotatedType raw, Class<T> sourceClass) {
        super(raw);

        this.sourceClass = sourceClass;
        this.klass = null;
        this.typeVarValues = null;
    }

    public GenericEClassUseImpl(AnnotatedType raw, EClass<T> type) {
        super(raw);

        this.sourceClass = type.raw();
        this.klass = type;
        this.typeVarValues = new MappedImmutableList<>(type.typeVariableValues(), EType::asEmptyUse);
    }

    public void init(List<ETypeUse> typeVarValues) {
        if (klass != null) throw new IllegalStateException("Tried to initialize twice!");

        this.klass = new GenericEClassImpl<>(new MappedImmutableList<>(typeVarValues, ETypeUse::type), sourceClass);
        this.typeVarValues = typeVarValues;
    }

    @Override
    public @Unmodifiable List<ETypeUse> typeVariableValues() {
        return typeVarValues;
    }

    @Override
    public ETypeUse tryResolve(GenericTypeContext ctx, EncounteredTypes encounteredTypes) {
        if (!encounteredTypes.addTypeUse(this))
            return this;

        ETypeUse[] newParamValues = new ETypeUse[typeVarValues.size()];
        boolean changed = false;
        for (int i = 0; i < typeVarValues.size(); i++) {
            ETypeUse newValue = typeVarValues.get(i).tryResolve(ctx, encounteredTypes);
            if (newValue != typeVarValues.get(i)) {
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
