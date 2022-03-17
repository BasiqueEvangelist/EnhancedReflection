package me.basiqueevangelist.enhancedreflection.impl;

import me.basiqueevangelist.enhancedreflection.api.EClass;
import me.basiqueevangelist.enhancedreflection.api.EConstructor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class EConstructorImpl<T> extends EExecutableImpl<Constructor<T>> implements EConstructor<T> {
    public EConstructorImpl(EClass<T> parent, Constructor<T> constructor) {
        super(parent, constructor);
    }

    @Override
    public T invoke(Object... args) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        return raw.newInstance(args);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(declaringClass().simpleName()).append("(");

        for (int i = 0; i < parameters().size(); i++) {
            if (i > 0) sb.append(", ");

            sb.append(parameters().get(i));
        }

        sb.append(")");
        return sb.toString();
    }
}
