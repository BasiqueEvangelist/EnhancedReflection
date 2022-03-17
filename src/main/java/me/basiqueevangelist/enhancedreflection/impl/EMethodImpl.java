package me.basiqueevangelist.enhancedreflection.impl;

import me.basiqueevangelist.enhancedreflection.api.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EMethodImpl extends EExecutableImpl<Method> implements EMethod {
    public EMethodImpl(EClass<?> parent, Method method) {
        super(parent, method);
    }

    @Override
    public Object invoke(Object receiver, Object... args) throws InvocationTargetException, IllegalAccessException {
        return raw.invoke(receiver, args);
    }

    @Override
    public EType returnType() {
        return EType.fromJava(raw.getGenericReturnType()).tryResolve(parent);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(returnType()).append(" ").append(name()).append("(");

        for (int i = 0; i < parameters().size(); i++) {
            if (i > 0) sb.append(", ");

            sb.append(parameters().get(i));
        }

        sb.append(")");
        return sb.toString();
    }
}
