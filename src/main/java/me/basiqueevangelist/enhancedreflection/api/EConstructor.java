package me.basiqueevangelist.enhancedreflection.api;

import me.basiqueevangelist.enhancedreflection.impl.EConstructorImpl;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@ApiStatus.NonExtendable
public interface EConstructor<T> extends EExecutable {
    static <T> EConstructor<T> fromJava(Constructor<T> constructor) {
        return new EConstructorImpl<>(EClass.fromJava(constructor.getDeclaringClass()), constructor);
    }

    T invoke(Object... args) throws InvocationTargetException, InstantiationException, IllegalAccessException;

    Constructor<T> raw();
}
