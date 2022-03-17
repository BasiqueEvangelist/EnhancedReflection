package me.basiqueevangelist.enhancedreflection.impl;

import me.basiqueevangelist.enhancedreflection.api.*;

import java.lang.reflect.Parameter;
import java.util.Objects;

public class EParameterImpl extends AnnotatedImpl<Parameter> implements EParameter {
    private final EExecutable parent;

    public EParameterImpl(EExecutable parent, Parameter raw) {
        super(raw);

        this.parent = parent != null ? parent : EExecutable.fromJava(raw.getDeclaringExecutable());
    }

    @Override
    public String name() {
        return raw.getName();
    }

    @Override
    public EType parameterType() {
        return EType.fromJava(raw.getParameterizedType()).tryResolve(parent);
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
