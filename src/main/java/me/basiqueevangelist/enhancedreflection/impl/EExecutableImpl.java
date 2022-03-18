package me.basiqueevangelist.enhancedreflection.impl;

import me.basiqueevangelist.enhancedreflection.api.*;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

public abstract class EExecutableImpl<T extends Executable> extends AnnotatedImpl<T> implements EExecutable {
    protected final EClass<?> parent;
    private final Lazy<List<EClass<?>>> exceptionTypes;
    private final Lazy<List<EParameter>> parameters;
    private final Lazy<List<ETypeVariable>> typeParams;

    public EExecutableImpl(EClass<?> parent, T raw) {
        super(raw);

        this.parent = parent;

        this.exceptionTypes = new Lazy<>(() -> {
            Class<?>[] jClasses = raw.getExceptionTypes();
            EClass<?>[] classes = new EClass[jClasses.length];

            for (int i = 0; i < classes.length; i++) {
                classes[i] = EClass.fromJava(jClasses[i]);
            }

            return List.of(classes);
        });

        this.parameters = new Lazy<>(() -> {
            Parameter[] jParameters = raw.getParameters();
            EParameter[] parameters = new EParameter[jParameters.length];

            for (int i = 0; i < parameters.length; i++) {
                parameters[i] = new EParameterImpl(this, jParameters[i]);
            }

            return List.of(parameters);
        });

        this.typeParams = new Lazy<>(() -> {
            Type[] typeParams = raw.getTypeParameters();
            ETypeVariable[] eTypeParams = new ETypeVariable[typeParams.length];
            for (int i = 0; i < typeParams.length; i++) {
                eTypeParams[i] = (ETypeVariable) EType.fromJava(typeParams[i]);
            }
            return List.of(eTypeParams);
        });
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
    public boolean isVarArgs() {
        return raw.isVarArgs();
    }

    @Override
    public EClass<?> declaringClass() {
        return parent;
    }

    @Override
    public @Unmodifiable List<EClass<?>> exceptionTypes() {
        return exceptionTypes.get();
    }

    @Override
    public @Unmodifiable List<EParameter> parameters() {
        return parameters.get();
    }

    @Override
    public @Unmodifiable List<ETypeVariable> typeVariables() {
        return typeParams.get();
    }

    @Override
    public EType resolveTypeVariable(ETypeVariable typeVar) {
        if (parent != null) return parent.resolveTypeVariable(typeVar);
        else return typeVar;
    }

    @Override
    public T raw() {
        return raw;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EExecutableImpl<?> that = (EExecutableImpl<?>) o;

        if (!Objects.equals(parent, that.parent)) return false;
        return raw.equals(that.raw);
    }

    @Override
    public int hashCode() {
        return 31 * parent.hashCode() + raw.hashCode();
    }
}
