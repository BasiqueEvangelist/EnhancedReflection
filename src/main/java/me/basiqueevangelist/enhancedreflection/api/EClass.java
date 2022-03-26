package me.basiqueevangelist.enhancedreflection.api;

import me.basiqueevangelist.enhancedreflection.api.typeuse.EClassUse;
import me.basiqueevangelist.enhancedreflection.api.typeuse.ETypeUse;
import me.basiqueevangelist.enhancedreflection.impl.EClassImpl;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@ApiStatus.NonExtendable
public interface EClass<T> extends EType, ModifierHolder, GenericTypeContext, EAnnotated, ClassContainer {
    @Contract("null -> null; !null -> new")
    static <T> EClass<T> fromJava(Class<T> klass) {
        return EClassImpl.fromJava(klass);
    }

    ClassType type();
    ClassScope scope();
    @Nullable ClassContainer enclosedIn();
    @Nullable EClass<? super T> superclass();
    @Nullable EClassUse<? super T> superclassUse();
    @Unmodifiable List<EClass<? super T>> interfaces();
    @Unmodifiable List<EClassUse<? super T>> interfaceUses();
    boolean isInstance(Object obj);
    boolean isAssignableFrom(EClass<?> klass);
    boolean isAssignableFrom(Class<?> klass);
    String name();
    String simpleName();
    @Nullable EPackage getPackage();
    String packageName();

    boolean isGeneric();
    boolean isGenericInstance();
    @Unmodifiable List<EType> typeVariableValues();
    EClass<T> instantiateWith(List<EType> typeVarValues);
    EClass<T> instantiateWith(Class<?>... typeVarValues);

    @Unmodifiable Collection<EField> fields();
    @Unmodifiable Collection<EField> declaredFields();
    @Nullable EField field(String name);
    @Nullable EField declaredField(String name);

    @Unmodifiable List<EMethod> methods();
    @Unmodifiable List<EMethod> declaredMethods();
    @Nullable EMethod method(String name, Class<?>... parameterTypes);
    @Nullable EMethod method(String name, List<EClass<?>> parameterTypes);
    @Nullable EMethod declaredMethod(String name, Class<?>... parameterTypes);
    @Nullable EMethod declaredMethod(String name, List<EClass<?>> parameterTypes);

    @Unmodifiable List<EConstructor<T>> constructors();
    @Nullable EConstructor<T> constructor(Class<?>... parameterTypes);
    @Nullable EConstructor<T> constructor(List<EClass<?>> parameterTypes);

    @Unmodifiable List<ERecordComponent> recordComponents();

    <O> EClass<? extends O> assertSubclass(EClass<O> clazz);

    EClass<T[]> arrayOf();
    @Nullable EClass<?> arrayComponent();

    Class<T> raw();

    EClass<?> unwrapPrimitive();
    EClass<?> wrapPrimitive();

    Set<EClass<? super T>> allSuperclasses();
    Set<EClass<? super T>> allInterfaces();

    @Nullable @Unmodifiable List<T> enumConstants();

    ClassLoader classLoader();

    @SuppressWarnings("unchecked")
    default T cast(Object obj) {
        if (!isInstance(obj)) throw new ClassCastException();

        return (T) obj;
    }

    @Override
    default EClass<?> upperBound() {
        return this;
    }

    @Override
    default EClass<?> lowerBound() {
        return this;
    }

    @Override
    EClassUse<T> asEmptyUse();
}
