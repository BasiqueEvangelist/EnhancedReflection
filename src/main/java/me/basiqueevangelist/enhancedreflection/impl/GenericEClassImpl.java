package me.basiqueevangelist.enhancedreflection.impl;

import me.basiqueevangelist.enhancedreflection.api.EClass;
import me.basiqueevangelist.enhancedreflection.api.EType;
import me.basiqueevangelist.enhancedreflection.api.ETypeVariable;
import me.basiqueevangelist.enhancedreflection.api.GenericTypeContext;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Set;

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

    public void setTypeParamValues(List<EType> typeParamValues) {
        if (this.typeParamValues != null) throw new IllegalStateException("Type param values have already been set!");

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
    public EType tryResolve(GenericTypeContext ctx, Set<EType> encounteredTypes) {
        if (!encounteredTypes.add(this))
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
            encounteredTypes.remove(this);
        }
    }

    @Override
    public EClass<T[]> arrayOf() {
        return new GenericArrayEClassImpl<>(this);
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
