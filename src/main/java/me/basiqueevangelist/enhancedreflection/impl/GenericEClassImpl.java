package me.basiqueevangelist.enhancedreflection.impl;

import me.basiqueevangelist.enhancedreflection.api.*;
import me.basiqueevangelist.enhancedreflection.api.typeuse.EClassUse;
import me.basiqueevangelist.enhancedreflection.api.typeuse.ETypeUse;
import me.basiqueevangelist.enhancedreflection.impl.typeuse.GenericEClassUseImpl;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.reflect.AnnotatedType;
import java.util.List;

public class GenericEClassImpl<T> extends EClassImpl<T> {
    private List<EType> typeParamValues;

    public GenericEClassImpl(List<EType> typeParamValues, Class<T> klass) {
        super(klass);
        this.typeParamValues = typeParamValues;
    }

    public GenericEClassImpl(Class<T> klass) {
        super(klass);
        this.typeParamValues = null;
    }

    public void init(List<EType> typeParamValues) {
        if (this.typeParamValues != null) throw new IllegalStateException("Tried to initialize twice!");

        this.typeParamValues = typeParamValues;
    }

    @Override
    public boolean isGeneric() {
        return true;
    }

    @Override
    public boolean isGenericInstance() {
        return true;
    }

    @Override
    public @Unmodifiable List<EType> typeVariableValues() {
        return typeParamValues;
    }

    @Override
    public EType resolveTypeVariable(ETypeVariable typeVar) {
        int idx = typeVariables().indexOf(typeVar);
        if (idx != -1)
            return typeParamValues.get(idx);
        else
            return typeVar;
    }

    @Override
    public EType tryResolve(GenericTypeContext ctx, EncounteredTypes encounteredTypes) {
        if (!encounteredTypes.addType(this))
            return this;

        EType[] newParamValues = new EType[typeParamValues.size()];
        boolean changed = false;
        for (int i = 0; i < typeParamValues.size(); i++) {
            EType newValue = typeParamValues.get(i).tryResolve(ctx, encounteredTypes);
            if (newValue != typeParamValues.get(i)) {
                changed = true;
            }
            newParamValues[i] = newValue;
        }

        try {
            if (changed)
                return new GenericEClassImpl<>(List.of(newParamValues), raw);
            else
                return this;
        } finally {
            encounteredTypes.removeType(this);
        }
    }

    @Override
    public EClass<T[]> arrayOf() {
        return new GenericArrayEClassImpl<>(this);
    }

    @Override
    public EClassUse<T> asUseWith(AnnotatedType data) {
        return new GenericEClassUseImpl<>(data, this);
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append(super.toString());
        out.append('<');

        boolean isFirst = true;
        for (EType typeParam : typeParamValues) {
            if (!isFirst) out.append(", ");

            isFirst = false;
            out.append(typeParam.toString());
        }

        out.append('>');
        return out.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        GenericEClassImpl<?> that = (GenericEClassImpl<?>) o;

        return typeParamValues.equals(that.typeParamValues);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + typeParamValues.hashCode();
        return result;
    }
}
