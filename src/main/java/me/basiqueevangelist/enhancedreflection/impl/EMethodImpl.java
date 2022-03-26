package me.basiqueevangelist.enhancedreflection.impl;

import me.basiqueevangelist.enhancedreflection.api.*;
import me.basiqueevangelist.enhancedreflection.api.typeuse.ETypeUse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;

public class EMethodImpl extends EExecutableImpl<Method> implements EMethod {
    private final Lazy<ETypeUse> returnTypeUse;

    public EMethodImpl(EClass<?> parent, Method method) {
        super(parent, method);

        this.returnTypeUse = new Lazy<>(() -> ETypeUse.fromJava(raw.getAnnotatedReturnType()).tryResolve(parent, EncounteredTypes.create()));
    }

    @Override
    public Object invoke(Object receiver, Object... args) throws InvocationTargetException, IllegalAccessException {
        return raw.invoke(receiver, args);
    }

    @Override
    public EType returnType() {
        return returnTypeUse.get().type();
    }

    @Override
    public ETypeUse returnTypeUse() {
        return returnTypeUse.get();
    }

    @Override
    public EClass<?> rawReturnType() {
        return EClass.fromJava(raw.getReturnType());
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
