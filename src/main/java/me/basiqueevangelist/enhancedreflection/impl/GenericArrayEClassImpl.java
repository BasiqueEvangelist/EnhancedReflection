package me.basiqueevangelist.enhancedreflection.impl;

import me.basiqueevangelist.enhancedreflection.api.*;
import me.basiqueevangelist.enhancedreflection.api.typeuse.EClassUse;
import me.basiqueevangelist.enhancedreflection.impl.typeuse.EmptyAnnotatedType;
import me.basiqueevangelist.enhancedreflection.impl.typeuse.GenericArrayEClassUseImpl;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class GenericArrayEClassImpl<T> implements EClass<T[]>, ETypeInternal<EClassUse<T[]>> {
    private final EClass<T> elementType;

    public GenericArrayEClassImpl(EClass<T> elementType) {
        this.elementType = elementType;
    }

    @Override
    public boolean hasAnnotation(Class<? extends Annotation> type) {
        return false;
    }

    @Override
    public <A extends Annotation> A annotation(Class<A> type) {
        return null;
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @Override
    public <A extends Annotation> A[] annotations(Class<A> type) {
        return (A[]) new Object[0];
    }

    @Override
    public <A extends Annotation> A declaredAnnotation(Class<A> type) {
        return null;
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @Override
    public <A extends Annotation> A[] declaredAnnotations(Class<A> type) {
        return (A[]) new Object[0];
    }

    @Override
    public @Unmodifiable List<Annotation> allAnnotations() {
        return Collections.emptyList();
    }

    @Override
    public @Unmodifiable List<Annotation> allDeclaredAnnotations() {
        return Collections.emptyList();
    }

    @Override
    public ClassType type() {
        return ClassType.ARRAY;
    }

    @Override
    public ClassScope scope() {
        return ClassScope.OUTER;
    }

    @Override
    public ClassContainer enclosedIn() {
        return null;
    }

    @Override
    public EClass<? super T[]> superclass() {
        return EClass.fromJava(Object.class);
    }

    @Override
    public EClassUse<? super T[]> superclassUse() {
        return superclass().asEmptyUse();
    }

    @Override
    public @Unmodifiable List<EClass<? super T[]>> interfaces() {
        return Collections.emptyList();
    }

    @Override
    public @Unmodifiable List<EClassUse<? super T[]>> interfaceUses() {
        return Collections.emptyList();
    }

    @Override
    public boolean isInstance(Object obj) {
        return raw().isInstance(obj);
    }

    @Override
    public boolean isAssignableFrom(EClass<?> klass) {
        if (klass.type() != ClassType.ARRAY) return false;

        return elementType.lowerBound().isAssignableFrom(klass.arrayComponent());
    }

    @Override
    public boolean isAssignableFrom(Class<?> klass) {
        if (!klass.isArray()) return false;

        return elementType.lowerBound().isAssignableFrom(klass.componentType());
    }

    @Override
    public String name() {
        return "[" + elementType.toString();
    }

    @Override
    public String simpleName() {
        return elementType.simpleName() + "[]";
    }

    @Override
    public EPackage getPackage() {
        return null;
    }

    @Override
    public String packageName() {
        return "";
    }

    @Override
    public boolean isGeneric() {
        return false;
    }

    @Override
    public boolean isGenericInstance() {
        return true;
    }

    @Override
    public @Unmodifiable List<EType> typeVariableValues() {
        return List.of();
    }

    @Override
    public EClass<T[]> instantiateWith(List<EType> typeVarValues) {
        throw new IllegalArgumentException("Type variable array size doesn't match!");
    }

    @Override
    public EClass<T[]> instantiateWith(Class<?>... typeVarValues) {
        throw new IllegalArgumentException("Type variable array size doesn't match!");
    }

    @Override
    public @Unmodifiable Collection<EField> fields() {
        return Collections.emptyList();
    }

    @Override
    public @Unmodifiable Collection<EField> declaredFields() {
        return Collections.emptyList();
    }

    @Override
    public EField field(String name) {
        return null;
    }

    @Override
    public EField declaredField(String name) {
        return null;
    }

    @Override
    public @Unmodifiable List<EMethod> methods() {
        return CommonTypes.OBJECT.methods();
    }

    @Override
    public @Unmodifiable List<EMethod> declaredMethods() {
        return Collections.emptyList();
    }

    @Override
    public EMethod method(String name, Class<?>... parameterTypes) {
        return CommonTypes.OBJECT.method(name, parameterTypes);
    }

    @Override
    public EMethod method(String name, List<EClass<?>> parameterTypes) {
        return CommonTypes.OBJECT.method(name, parameterTypes);
    }

    @Override
    public EMethod declaredMethod(String name, Class<?>... parameterTypes) {
        return null;
    }

    @Override
    public EMethod declaredMethod(String name, List<EClass<?>> parameterTypes) {
        return null;
    }

    @Override
    public @Unmodifiable List<EConstructor<T[]>> constructors() {
        return Collections.emptyList();
    }

    @Override
    public EConstructor<T[]> constructor(Class<?>... parameterTypes) {
        return null;
    }

    @Override
    public EConstructor<T[]> constructor(List<EClass<?>> parameterTypes) {
        return null;
    }

    @Override
    public @Unmodifiable List<ERecordComponent> recordComponents() {
        return Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <O> EClass<? extends O> assertSubclass(EClass<O> clazz) {
        if (clazz.type() != ClassType.ARRAY || !arrayComponent().isAssignableFrom(clazz.arrayComponent()))
            throw new IllegalStateException("Class " + name() + " is not a subclass of " + clazz.name());

        return (EClass<? extends O>) this;
    }

    @Override
    public EClass<T[][]> arrayOf() {
        return new GenericArrayEClassImpl<>(this);
    }

    @Override
    public EClass<?> arrayComponent() {
        return elementType;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<T[]> raw() {
        return (Class<T[]>) arrayComponent().raw().arrayType();
    }

    @Override
    public EClass<?> unwrapPrimitive() {
        return this;
    }

    @Override
    public EClass<?> wrapPrimitive() {
        return this;
    }

    @Override
    public Set<EClass<? super T[]>> allSuperclasses() {
        return Collections.singleton(EClass.fromJava(Object.class));
    }

    @Override
    public Set<EClass<? super T[]>> allInterfaces() {
        return Collections.emptySet();
    }

    @Override
    public @Unmodifiable List<T[]> enumConstants() {
        return null;
    }

    @Override
    public ClassLoader classLoader() {
        return raw().getClassLoader();
    }

    @Override
    public EClassUse<T[]> asUseWith(AnnotatedType data) {
        return new GenericArrayEClassUseImpl<>(data, this);
    }

    @Override
    public EClassUse<T[]> asEmptyUse() {
        return asUseWith(EmptyAnnotatedType.INSTANCE);
    }

    @Override
    public int modifiers() {
        return 1041;
    }

    @Override
    public @Unmodifiable List<ETypeVariable> typeVariables() {
        return List.of();
    }

    @Override
    public EType resolveTypeVariable(ETypeVariable typeVar) {
        return arrayComponent().resolveTypeVariable(typeVar);
    }

    @Override
    public EType tryResolve(GenericTypeContext ctx, EncounteredTypes encounteredTypes) {
        EType newType = elementType.tryResolve(ctx, encounteredTypes);
        if (newType != elementType) {
            return new GenericArrayEClassImpl<>((EClass<?>) newType);
        } else {
            return this;
        }
    }

    @Override
    public String toString() {
        return elementType.toString() + "[]";
    }
}
