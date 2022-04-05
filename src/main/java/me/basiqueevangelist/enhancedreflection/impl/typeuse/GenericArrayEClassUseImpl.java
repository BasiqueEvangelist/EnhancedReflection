package me.basiqueevangelist.enhancedreflection.impl.typeuse;

import me.basiqueevangelist.enhancedreflection.api.EClass;
import me.basiqueevangelist.enhancedreflection.api.typeuse.EClassUse;
import me.basiqueevangelist.enhancedreflection.impl.GenericArrayEClassImpl;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.AnnotatedType;

public class GenericArrayEClassUseImpl<T> extends EClassUseImpl<T[]> {
    private final EClassUse<T> component;

    public GenericArrayEClassUseImpl(AnnotatedType raw, EClassUse<T> component) {
        super(raw, new GenericArrayEClassImpl<>(component.type()));
        this.component = component;
    }

    @SuppressWarnings("unchecked")
    public GenericArrayEClassUseImpl(AnnotatedType raw, EClass<T[]> type) {
        super(raw, type);

        this.component = (EClassUse<T>) type.arrayComponent().asEmptyUse();
    }

    @Override
    public EClassUse<?> arrayComponent() {
        return component;
    }
}
