package me.basiqueevangelist.enhancedreflection.impl;

import me.basiqueevangelist.enhancedreflection.api.*;
import me.basiqueevangelist.enhancedreflection.api.typeuse.ETypeUse;

import java.lang.reflect.Field;
import java.util.Objects;

public final class EFieldImpl extends EAnnotatedImpl<Field> implements EField {
    private final EClassImpl<?> parent;
    private final Lazy<ETypeUse> fieldTypeUse;

    public EFieldImpl(EClassImpl<?> parent,
                      Field raw) {
        super(raw);

        this.parent = parent;

        this.fieldTypeUse = new Lazy<>(() -> ETypeUse.fromJava(raw.getAnnotatedType()).tryResolve(parent, EncounteredTypes.create()));
    }

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
        return fieldTypeUse.get().type();
    }

    @Override
    public ETypeUse fieldTypeUse() {
        return fieldTypeUse.get();
    }

    @Override
    public EClass<?> rawFieldType() {
        return EClass.fromJava(raw.getType());
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (EFieldImpl) obj;
        return Objects.equals(this.parent, that.parent) &&
            Objects.equals(this.raw, that.raw);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, raw);
    }

    @Override
    public String toString() {
        return fieldType() + " " + name();
    }
}
