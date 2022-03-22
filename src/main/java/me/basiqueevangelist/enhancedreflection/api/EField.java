package me.basiqueevangelist.enhancedreflection.api;

import me.basiqueevangelist.enhancedreflection.impl.EClassImpl;
import me.basiqueevangelist.enhancedreflection.impl.EFieldImpl;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Field;

@ApiStatus.NonExtendable
public interface EField extends EMember {
    static EField fromJava(Field field) {
        return new EFieldImpl((EClassImpl<?>) EClass.fromJava(field.getDeclaringClass()), field);
    }

    EType fieldType();
    EClass<?> rawFieldType();
    Object get(Object receiver) throws IllegalAccessException;
    void set(Object receiver, Object to) throws IllegalAccessException;

    Field raw();
}
