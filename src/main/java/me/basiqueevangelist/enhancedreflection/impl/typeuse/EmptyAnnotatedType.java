package me.basiqueevangelist.enhancedreflection.impl.typeuse;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;

public class EmptyAnnotatedType implements AnnotatedType {
    public static final EmptyAnnotatedType INSTANCE = new EmptyAnnotatedType();

    @Override
    public Type getType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return null;
    }

    @Override
    public Annotation[] getAnnotations() {
        return new Annotation[0];
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
        return new Annotation[0];
    }
}
