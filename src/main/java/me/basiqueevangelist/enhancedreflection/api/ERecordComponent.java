package me.basiqueevangelist.enhancedreflection.api;

import me.basiqueevangelist.enhancedreflection.impl.ERecordComponentImpl;

import java.lang.reflect.RecordComponent;

public interface ERecordComponent extends EAnnotated {
    static ERecordComponent fromJava(RecordComponent component) {
        return new ERecordComponentImpl(EClass.fromJava(component.getDeclaringRecord()), component);
    }

    EMethod accessor();
    EType componentType();
    EClass<?> declaringRecord();

    RecordComponent raw();
}
