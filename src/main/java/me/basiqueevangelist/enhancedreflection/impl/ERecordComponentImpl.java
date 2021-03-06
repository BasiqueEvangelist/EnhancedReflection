package me.basiqueevangelist.enhancedreflection.impl;

import me.basiqueevangelist.enhancedreflection.api.*;
import me.basiqueevangelist.enhancedreflection.api.typeuse.ETypeUse;

import java.lang.reflect.RecordComponent;
import java.util.HashSet;

public class ERecordComponentImpl extends EAnnotatedImpl<RecordComponent> implements ERecordComponent {
    private final EClass<?> parent;
    private final Lazy<EMethod> accessor;
    private final Lazy<ETypeUse> componentTypeUse;

    public ERecordComponentImpl(EClass<?> parent, RecordComponent raw) {
        super(raw);

        this.parent = parent;
        this.accessor = new Lazy<>(() -> new EMethodImpl(parent, raw.getAccessor()));
        this.componentTypeUse = new Lazy<>(() -> ETypeUse.fromJava(raw.getAnnotatedType()).tryResolve(parent, EncounteredTypes.create()));
    }

    @Override
    public String name() {
        return raw.getName();
    }

    @Override
    public EMethod accessor() {
        return accessor.get();
    }

    @Override
    public EType componentType() {
        return componentTypeUse.get().type();
    }

    @Override
    public ETypeUse componentTypeUse() {
        return componentTypeUse.get();
    }

    @Override
    public EClass<?> rawComponentType() {
        return EClass.fromJava(raw.getType());
    }

    @Override
    public EClass<?> declaringRecord() {
        return parent;
    }

    @Override
    public RecordComponent raw() {
        return raw;
    }

    @Override
    public String toString() {
        return componentType().toString() + " " + name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ERecordComponentImpl that = (ERecordComponentImpl) o;

        if (!parent.equals(that.parent)) return false;
        return raw.equals(that.raw);
    }

    @Override
    public int hashCode() {
        int result = parent.hashCode();
        result = 31 * result + raw.hashCode();
        return result;
    }
}
