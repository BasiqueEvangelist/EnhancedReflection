package me.basiqueevangelist.enhancedreflection.impl;

import java.util.HashMap;
import java.util.Map;

public class TypeStore {
    public static final Map<Class<?>, EClassImpl<?>> TYPES = new HashMap<>();
    public static final Map<EClassImpl<?>, EClassImpl<?>> PRIMITIVE_TO_WRAPPER = new HashMap<>();
    public static final Map<EClassImpl<?>, EClassImpl<?>> WRAPPER_TO_PRIMITIVE = new HashMap<>();

    private static EClassImpl<?> addClass(Class<?> klass) {
        return TYPES.computeIfAbsent(klass, EClassImpl::fromJava);
    }

    private static void addPrimitive(Class<?> primitive, Class<?> wrapper) {
        var prim = addClass(primitive);
        var wrap = addClass(wrapper);
        PRIMITIVE_TO_WRAPPER.put(prim, wrap);
        WRAPPER_TO_PRIMITIVE.put(wrap, prim);
    }

    static {
        addClass(Object.class);
        addClass(String.class);

        addPrimitive(boolean.class, Boolean.class);
        addPrimitive(char.class, Character.class);
        addPrimitive(byte.class, Byte.class);
        addPrimitive(short.class, Short.class);
        addPrimitive(int.class, Integer.class);
        addPrimitive(float.class, Float.class);
        addPrimitive(long.class, Long.class);
        addPrimitive(double.class, Double.class);
    }
}
