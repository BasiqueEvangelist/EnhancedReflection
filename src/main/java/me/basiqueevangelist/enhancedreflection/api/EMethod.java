package me.basiqueevangelist.enhancedreflection.api;

import me.basiqueevangelist.enhancedreflection.impl.EMethodImpl;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@ApiStatus.NonExtendable
public interface EMethod extends EExecutable {
    static EMethod fromJava(Method method) {
        return new EMethodImpl(EClass.fromJava(method.getDeclaringClass()), method);
    }

    Object invoke(Object receiver, Object... args) throws InvocationTargetException, IllegalAccessException;
    EType returnType();

    Method raw();
}
