package me.basiqueevangelist.enhancedreflection.impl;

import me.basiqueevangelist.enhancedreflection.api.*;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.reflect.*;
import java.util.*;

public class EClassImpl<T> extends EAnnotatedImpl<Class<T>> implements EClass<T> {
    private final Lazy<List<ETypeVariable>> typeParams;
    private final Lazy<List<EField>> fields;
    private final Lazy<Map<String, EField>> fieldsMap;
    private final Lazy<Map<String, EField>> declaredFieldsMap;
    private final Lazy<List<EMethod>> methods;
    private final Lazy<List<EMethod>> declaredMethods;
    private final Lazy<List<EConstructor<T>>> constructors;
    private final Lazy<List<EClass<? super T>>> interfaces;
    private final Lazy<List<ERecordComponent>> recordComponents;
    private final Lazy<List<T>> enumConstants;

    @SuppressWarnings("unchecked")
    public static <T> EClassImpl<T> fromJava(Class<T> klass) {
        if (klass == null)
            return null;

        var clazz = TypeStore.TYPES.get(klass);

        if (clazz == null) clazz = new EClassImpl<>(klass);

        return (EClassImpl<T>) clazz;
    }

    @SuppressWarnings("unchecked")
    public EClassImpl(Class<T> raw) {
        super(raw);

        this.typeParams = new Lazy<>(() -> {
            Type[] typeParams = raw.getTypeParameters();
            ETypeVariable[] eTypeParams = new ETypeVariable[typeParams.length];
            for (int i = 0; i < typeParams.length; i++) {
                eTypeParams[i] = (ETypeVariable) EType.fromJava(typeParams[i]);
            }
            return List.of(eTypeParams);
        });

        this.fields = new Lazy<>(() -> {
            List<EField> allFields = new ArrayList<>(declaredFields());
            EClass<?> superclass = superclass();

            allFields.removeIf(x -> !x.isPublic());

            if (superclass != null)
                superclass().fields().forEach(x -> {
                    for (EField allField : allFields) {
                        if (allField.raw() == x.raw())
                            return;
                    }

                    allFields.add(x);
                });

            interfaces().forEach(y -> y.fields().forEach(x -> {
                for (EField allField : allFields) {
                    if (allField.raw() == x.raw())
                        return;
                }

                allFields.add(x);
            }));

            return Collections.unmodifiableList(allFields);
        });

        this.fieldsMap = new Lazy<>(() -> {
            Map<String, EField> fields = new HashMap<>();

            for (EField field : fields()) {
                fields.putIfAbsent(field.name(), field);
            }

            return Collections.unmodifiableMap(fields);
        });

        this.declaredFieldsMap = new Lazy<>(() -> {
            Field[] jFields = raw.getDeclaredFields();
            Map<String, EField> fields = new HashMap<>();

            for (Field jField : jFields) {
                fields.put(jField.getName(), new EFieldImpl(this, jField));
            }

            return Collections.unmodifiableMap(fields);
        });

        this.methods = new Lazy<>(() -> {
            var collector = new MethodCollector();

            declaredMethods().forEach(collector::add);

            var superclass = superclass();

            if (superclass != null)
                superclass.methods().forEach(collector::add);

            for (var iface : interfaces())
                iface.methods().forEach(collector::add);

            return Collections.unmodifiableList(collector.toList());
        });

        this.declaredMethods = new Lazy<>(() -> {
            Method[] jMethods = raw.getDeclaredMethods();
            EMethod[] methods = new EMethod[jMethods.length];

            for (int i = 0; i < jMethods.length; i++) {
                methods[i] = new EMethodImpl(this, jMethods[i]);
            }

            return List.of(methods);
        });

        this.constructors = new Lazy<>(() -> {
            Constructor<T>[] jConstructors = (Constructor<T>[]) raw.getConstructors();
            EConstructor<T>[] constructors = new EConstructor[jConstructors.length];

            for (int i = 0; i < jConstructors.length; i++) {
                constructors[i] = new EConstructorImpl<>(this, jConstructors[i]);
            }

            return List.of(constructors);
        });

        this.interfaces = new Lazy<>(() -> {
            Type[] jInterfaces = raw.getGenericInterfaces();
            EClass<? super T>[] interfaces = new EClass[jInterfaces.length];

            for (int i = 0; i < interfaces.length; i++) {
                interfaces[i] = (EClass<? super T>) EType.fromJava(jInterfaces[i]).tryResolve(this).upperBound();
            }

            return List.of(interfaces);
        });

        this.recordComponents = new Lazy<>(() -> {
            RecordComponent[] jComponents = raw.getRecordComponents();
            ERecordComponent[] components = new ERecordComponent[jComponents.length];

            for (int i = 0; i < components.length; i++) {
                components[i] = new ERecordComponentImpl(this, jComponents[i]);
            }

            return List.of(components);
        });

        this.enumConstants = new Lazy<>(() -> {
            var arr = raw.getEnumConstants();

            if (arr == null) return null;
            else return List.of(arr);
        });
    }

    @Override
    public ClassType type() {
        if (raw.isAnnotation())
            return ClassType.ANNOTATION;
        else if (raw.isRecord())
            return ClassType.RECORD;
        else if (raw.isEnum())
            return ClassType.ENUM;
        else if (raw.isInterface())
            return ClassType.INTERFACE;
        else if (raw.isArray())
            return ClassType.ARRAY;
        else if (raw.isPrimitive())
            return ClassType.PRIMITIVE;
        else
            return ClassType.CLASS;
    }

    @Override
    public ClassScope scope() {
        if (raw.isHidden())
            return ClassScope.HIDDEN;
        else if (raw.isAnonymousClass())
            return ClassScope.ANONYMOUS;
        else if (raw.isLocalClass())
            return ClassScope.LOCAL;
        else if (raw.isMemberClass())
            return ClassScope.MEMBER;
        else
            return ClassScope.OUTER;
    }

    @Override
    public @Nullable ClassContainer enclosedIn() {
        Class<?> enclosingClass = raw.getEnclosingClass();

        if (enclosingClass != null)
            return EClass.fromJava(enclosingClass);

        Constructor<?> enclosingCtor = raw.getEnclosingConstructor();

        if (enclosingCtor != null)
            return EConstructor.fromJava(enclosingCtor);

        Method enclosingMethod = raw.getEnclosingMethod();

        if (enclosingMethod != null)
            return EMethod.fromJava(enclosingMethod);

        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @Nullable EClass<? super T> superclass() {
        var superclass = raw.getGenericSuperclass();
        if (superclass == null) return null;
        return (EClass<? super T>) EType.fromJava(superclass).tryResolve(this).upperBound();
    }

    @Override
    public @Unmodifiable List<EClass<? super T>> interfaces() {
        return interfaces.get();
    }

    @Override
    public boolean isInstance(Object obj) {
        return raw.isInstance(obj);
    }

    @Override
    public boolean isAssignableFrom(EClass<?> klass) {
        return raw.isAssignableFrom(klass.raw());
    }

    @Override
    public boolean isAssignableFrom(Class<?> klass) {
        return raw.isAssignableFrom(klass);
    }

    @Override
    public String name() {
        return raw.getName();
    }

    @Override
    public String simpleName() {
        return raw.getSimpleName();
    }

    @Override
    public @Nullable EPackage getPackage() {
        return EPackage.fromJava(raw.getPackage());
    }

    @Override
    public String packageName() {
        return raw.getPackageName();
    }

    @Override
    public boolean isGeneric() {
        return typeVariables().size() > 0;
    }

    @Override
    public boolean isGenericInstance() {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public @Unmodifiable List<EType> typeVariableValues() {
        return (List<EType>)(Object) typeVariables();
    }

    @Override
    public EClass<T> instantiateWith(List<EType> typeVarValues) {
        if (typeVarValues.size() != typeVariables().size())
            throw new IllegalArgumentException("Type variable array size doesn't match!");

        return new GenericEClassImpl<>(List.of(typeVarValues.toArray(EType[]::new)), this.raw);
    }

    @Override
    public EClass<T> instantiateWith(Class<?>... typeVarValues) {
        if (typeVarValues.length != typeVariables().size())
            throw new IllegalArgumentException("Type variable array size doesn't match!");

        // A little streams can't hurt
        return new GenericEClassImpl<>(List.of(Arrays.stream(typeVarValues).map(EClass::fromJava).toArray(EType[]::new)), this.raw);
    }

    @Override
    public @Unmodifiable List<ETypeVariable> typeVariables() {
        return typeParams.get();
    }

    @Override
    public EType resolveTypeVariable(ETypeVariable typeVar) {
        return typeVar;
    }

    @Override
    public @Unmodifiable Collection<EField> fields() {
        return fields.get();
    }

    @Override
    public @Unmodifiable Collection<EField> declaredFields() {
        return declaredFieldsMap.get().values();
    }

    @Override
    public @Nullable EField field(String name) {
        return fieldsMap.get().get(name);
    }

    @Override
    public @Nullable EField declaredField(String name) {
        return declaredFieldsMap.get().get(name);
    }

    @Override
    public @Unmodifiable List<EMethod> methods() {
        return methods.get();
    }

    @Override
    public @Unmodifiable List<EMethod> declaredMethods() {
        return declaredMethods.get();
    }

    @Override
    public @Nullable EMethod method(String name, Class<?>... parameterTypes) {
        return findMethod(methods(), name, parameterTypes);
    }

    @Override
    public @Nullable EMethod declaredMethod(String name, Class<?>... parameterTypes) {
        return findMethod(declaredMethods(), name, parameterTypes);
    }

    private EMethod findMethod(List<EMethod> methods, String name, Class<?>[] parameterTypes) {
        EMethod result = null;

        outer: for (EMethod method : methods) {
            if (!method.name().equals(name)) continue;
            if (method.parameters().size() != parameterTypes.length) continue;

            for (int i = 0; i < parameterTypes.length; i++) {
                if (!parameterTypes[i].isAssignableFrom(method.parameters().get(i).rawParameterType().raw()))
                    continue outer;
            }

            if (result != null) {
                for (int i = 0; i < parameterTypes.length; i++) {
                    if (!result.parameters().get(i).rawParameterType().isAssignableFrom(method.parameters().get(i).rawParameterType()))
                        continue outer;
                }
            }

            result = method;
        }

        return result;
    }

    @Override
    public @Unmodifiable List<EConstructor<T>> constructors() {
        return constructors.get();
    }

    @Override
    public @Nullable EConstructor<T> constructor(Class<?>... parameterTypes) {
        try {
            return new EConstructorImpl<>(this, raw.getConstructor(parameterTypes));
        } catch (NoSuchMethodException unused) {
            return null;
        } catch (SecurityException se) {
            throw new RuntimeException(se);
        }
    }

    @Override
    public @Unmodifiable List<ERecordComponent> recordComponents() {
        return recordComponents.get();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <O> EClass<? extends O> assertSubclass(EClass<O> clazz) {
        if (clazz.isAssignableFrom(this))
            return (EClass<? extends O>) this;
        else
            throw new IllegalStateException("Class " + name() + " is not a subclass of " + clazz.name());
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public EClass<T[]> arrayOf() {
        return new EClassImpl<>((Class) raw.arrayType());
    }

    @Override
    public EClass<?> arrayComponent() {
        return EClass.fromJava(raw.componentType());
    }

    @Override
    public int modifiers() {
        return raw.getModifiers();
    }

    @Override
    public Class<T> raw() {
        return raw;
    }

    @Override
    public EClass<?> unwrapPrimitive() {
        return TypeStore.WRAPPER_TO_PRIMITIVE.getOrDefault(this, this);
    }

    @Override
    public EClass<?> wrapPrimitive() {
        return TypeStore.PRIMITIVE_TO_WRAPPER.getOrDefault(this, this);
    }

    @Override
    public Set<EClass<? super T>> allSuperclasses() {
        Set<EClass<? super T>> superclasses = new HashSet<>();

        for (EClass<? super T> superclass = superclass(); superclass != null; superclass = superclass.superclass())
            superclasses.add(superclass);

        return superclasses;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public Set<EClass<? super T>> allInterfaces() {
        Set<EClass<?>> interfaces = new HashSet<>();
        addInterfaces(interfaces);
        return (Set) interfaces;
    }

    @Override
    public @Nullable @Unmodifiable List<T> enumConstants() {
        return enumConstants.get();
    }

    @Override
    public ClassLoader classLoader() {
        return raw.getClassLoader();
    }

    private void addInterfaces(Set<EClass<?>> interfaces) {
        for (EClass<?> iface : interfaces()) {
            if (interfaces.add(iface))
                ((EClassImpl<?>)iface).addInterfaces(interfaces);
        }
    }

    @Override
    public String toString() {
        if (type() == ClassType.ARRAY)
            return arrayComponent().toString() + "[]";
        else
            return raw.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EClassImpl<?> eClass = (EClassImpl<?>) o;

        return Objects.equals(raw, eClass.raw);
    }

    @Override
    public int hashCode() {
        return raw.hashCode();
    }

    @Override
    public EType tryResolve(GenericTypeContext ctx) {
        List<ETypeVariable> typeParams = typeVariables();
        EType[] newParamValues = new EType[typeParams.size()];
        boolean changed = false;
        for (int i = 0; i < newParamValues.length; i++) {
            EType newValue = typeParams.get(i).tryResolve(ctx);
            if (newValue != typeParams.get(i)) {
                changed = true;
            }
            newParamValues[i] = newValue;
        }

        if (changed)
            return new GenericEClassImpl<>(List.of(newParamValues), raw);
        else
            return this;
    }
}
