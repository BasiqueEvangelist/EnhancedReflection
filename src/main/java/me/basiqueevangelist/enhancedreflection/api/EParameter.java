package me.basiqueevangelist.enhancedreflection.api;

import me.basiqueevangelist.enhancedreflection.impl.EParameterImpl;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Parameter;

@ApiStatus.NonExtendable
public interface EParameter extends EAnnotated {
    static EParameter fromJava(Parameter parameter) {
        return new EParameterImpl(EExecutable.fromJava(parameter.getDeclaringExecutable()), parameter);
    }

    String name();
    EClass<?> rawParameterType();
    EType parameterType();
    boolean isVarArgs();
    EExecutable declaringExecutable();

    Parameter raw();
}
