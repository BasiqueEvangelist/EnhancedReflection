package me.basiqueevangelist.enhancedreflection.impl;

import me.basiqueevangelist.enhancedreflection.api.*;
import me.basiqueevangelist.enhancedreflection.api.typeuse.ETypeUse;

import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.Objects;

public class EParameterImpl extends EAnnotatedImpl<Parameter> implements EParameter {
    private final EExecutable parent;
    private final Lazy<ETypeUse> parameterTypeUse;

    public EParameterImpl(EExecutable parent, Parameter raw) {
        super(raw);

        this.parent = parent;
        this.parameterTypeUse = new Lazy<>(() -> ETypeUse.fromJava(raw.getAnnotatedType()).tryResolve(parent, EncounteredTypes.create()));
    }

    @Override
    public String name() {
        return raw.getName();
    }

    @Override
    public EClass<?> rawParameterType() {
        return EClass.fromJava(raw.getType());
    }

    @Override
    public EType parameterType() {
        return parameterTypeUse.get().type();
    }

    @Override
    public ETypeUse parameterTypeUse() {
        return parameterTypeUse.get();
    }

    @Override
    public boolean isVarArgs() {
        return raw.isVarArgs();
    }

    @Override
    public EExecutable declaringExecutable() {
        return parent;
    }

    public Parameter raw() {
        return raw;
    }

    @Override
    public String toString() {
        return parameterType().toString() + " " + name();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (EParameterImpl) obj;
        return Objects.equals(this.parent, that.parent) &&
            Objects.equals(this.raw, that.raw);
    }

    @Override
    public int hashCode() {
        return 31 * parent.hashCode() + raw.hashCode();
    }
}
