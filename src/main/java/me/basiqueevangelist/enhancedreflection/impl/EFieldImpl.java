package me.basiqueevangelist.enhancedreflection.impl;

import me.basiqueevangelist.enhancedreflection.api.*;

import java.lang.reflect.Field;

public record EFieldImpl(EClassImpl<?> parent,
                         Field raw) implements EField {

    @Override
    public EClass<?> declaringClass() {
        return parent;
    }

    @Override
    public String name() {
        return raw.getName();
    }

    @Override
    public int modifiers() {
        return raw.getModifiers();
    }

    @Override
    public EType fieldType() {
        return EType.fromJava(raw.getGenericType()).tryResolve(parent);
    }

    @Override
    public Object get(Object receiver) throws IllegalAccessException {
        return raw.get(receiver);
    }

    @Override
    public void set(Object receiver, Object to) throws IllegalAccessException {
        raw.set(receiver, to);
    }

    @Override
    public Field raw() {
        return raw;
    }
}
