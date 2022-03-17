package me.basiqueevangelist.enhancedreflection.api;

import me.basiqueevangelist.enhancedreflection.impl.EParameterImpl;

import java.lang.reflect.Parameter;

public interface EParameter extends EAnnotated {
    static EParameter fromJava(Parameter parameter) {
        return new EParameterImpl(null, parameter);
    }

    String name();
    EType parameterType();
    boolean isVarArgs();
    EExecutable declaringExecutable();

    Parameter raw();
}
