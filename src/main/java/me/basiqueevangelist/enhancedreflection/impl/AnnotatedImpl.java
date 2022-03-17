package me.basiqueevangelist.enhancedreflection.impl;

import me.basiqueevangelist.enhancedreflection.api.EAnnotated;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.List;

public abstract class AnnotatedImpl<T extends AnnotatedElement> implements EAnnotated {
    protected final T raw;
    private final Lazy<List<Annotation>> annotations;
    private final Lazy<List<Annotation>> declaredAnnotations;

    public AnnotatedImpl(T raw) {
        this.raw = raw;

        this.annotations = new Lazy<>(() -> List.of(raw.getAnnotations()));
        this.declaredAnnotations = new Lazy<>(() -> List.of(raw.getDeclaredAnnotations()));
    }

    @Override
    public boolean hasAnnotation(Class<? extends Annotation> type) {
        return raw.isAnnotationPresent(type);
    }

    @Override
    public <A extends Annotation> @Nullable A annotation(Class<A> type) {
        return raw.getAnnotation(type);
    }

    @Override
    public <A extends Annotation> A[] annotations(Class<A> type) {
        return raw.getAnnotationsByType(type);
    }

    @Override
    public <A extends Annotation> @Nullable A declaredAnnotation(Class<A> type) {
        return raw.getDeclaredAnnotation(type);
    }

    @Override
    public <A extends Annotation> A[] declaredAnnotations(Class<A> type) {
        return raw.getDeclaredAnnotationsByType(type);
    }

    @Override
    public @Unmodifiable List<Annotation> allAnnotations() {
        return annotations.get();
    }

    @Override
    public @Unmodifiable List<Annotation> allDeclaredAnnotations() {
        return declaredAnnotations.get();
    }
}
