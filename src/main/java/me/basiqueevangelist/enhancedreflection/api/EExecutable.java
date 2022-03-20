package me.basiqueevangelist.enhancedreflection.api;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.List;

@ApiStatus.NonExtendable
public interface EExecutable extends GenericTypeContext, ModifierHolder, ClassContainer, EAnnotated {
    static EExecutable fromJava(Executable executable) {
        if (executable instanceof Method m)
            return EMethod.fromJava(m);
        else if (executable instanceof Constructor<?> c)
            return EConstructor.fromJava(c);
        else
            throw new IllegalStateException("Unknown Executable subclass " + executable.getClass().getName());
    }

    String name();
    boolean isVarArgs();
    EClass<?> declaringClass();
    @Unmodifiable List<EClass<?>> exceptionTypes();
    @Unmodifiable List<EParameter> parameters();

    Executable raw();
}
