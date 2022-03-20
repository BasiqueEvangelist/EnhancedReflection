package me.basiqueevangelist.enhancedreflection;

import me.basiqueevangelist.enhancedreflection.api.ClassType;
import me.basiqueevangelist.enhancedreflection.api.EClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecordTest {
    public record ExampleRecord<T> (
        String based1,
        int based2,
        T based3
    ) {}

    public void based(ExampleRecord<Short> record) {}

    @Test
    public void readRecord() {
        var klass = EClass.fromJava(RecordTest.class);
        var record = klass.method("based", ExampleRecord.class).parameters().get(0).parameterType().upperBound();
        assertEquals(ClassType.RECORD, record.type());
        assertEquals(3, record.recordComponents().size());
        var component1 = record.recordComponents().get(0);
        assertEquals("based1", component1.name());
        assertEquals(EClass.fromJava(String.class), component1.componentType());
        var component3 = record.recordComponents().get(2);
        assertEquals("based3", component3.name());
        assertEquals(EClass.fromJava(Short.class), component3.componentType());
    }
}
