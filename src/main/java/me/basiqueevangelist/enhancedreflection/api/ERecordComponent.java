package me.basiqueevangelist.enhancedreflection.api;

import me.basiqueevangelist.enhancedreflection.impl.ERecordComponentImpl;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.RecordComponent;

@ApiStatus.NonExtendable
public interface ERecordComponent extends EAnnotated {
    static ERecordComponent fromJava(RecordComponent component) {
        return new ERecordComponentImpl(EClass.fromJava(component.getDeclaringRecord()), component);
    }

    String name();
    EMethod accessor();
    EType componentType();
    EClass<?> declaringRecord();

    RecordComponent raw();
}
