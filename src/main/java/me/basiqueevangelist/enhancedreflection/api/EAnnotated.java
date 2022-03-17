package me.basiqueevangelist.enhancedreflection.api;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.annotation.Annotation;
import java.util.List;

@ApiStatus.NonExtendable
public interface EAnnotated {
    boolean hasAnnotation(Class<? extends Annotation> type);

    <A extends Annotation> @Nullable A annotation(Class<A> type);
    <A extends Annotation> A[] annotations(Class<A> type);

    <A extends Annotation> @Nullable A declaredAnnotation(Class<A> type);
    <A extends Annotation> A[] declaredAnnotations(Class<A> type);

    @Unmodifiable List<Annotation> allAnnotations();
    @Unmodifiable List<Annotation> allDeclaredAnnotations();
}
