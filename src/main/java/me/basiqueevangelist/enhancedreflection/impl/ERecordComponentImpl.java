package me.basiqueevangelist.enhancedreflection.impl;

import me.basiqueevangelist.enhancedreflection.api.*;

import java.lang.reflect.RecordComponent;

public class ERecordComponentImpl extends AnnotatedImpl<RecordComponent> implements ERecordComponent {
    private final EClass<?> parent;
    private final Lazy<EMethod> accessor;

    public ERecordComponentImpl(EClass<?> parent, RecordComponent raw) {
        super(raw);

        this.parent = parent;
        this.accessor = new Lazy<>(() -> new EMethodImpl(parent, raw.getAccessor()));
    }

    @Override
    public EMethod accessor() {
        return accessor.get();
    }

    @Override
    public EType componentType() {
        return EType.fromJava(raw.getGenericType()).tryResolve(parent);
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
