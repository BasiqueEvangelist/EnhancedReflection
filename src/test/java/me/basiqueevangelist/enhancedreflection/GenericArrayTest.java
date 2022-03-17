package me.basiqueevangelist.enhancedreflection;

import me.basiqueevangelist.enhancedreflection.api.ClassType;
import me.basiqueevangelist.enhancedreflection.api.EClass;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class GenericArrayTest {
    public static class ArrayClass<T> {
        public T[] array;
    }

    public void getMethod(ArrayClass<String> arr) {

    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void getArr() {
        var klass = EClass.fromJava(GenericArrayTest.class);
        var method = klass.method("getMethod", ArrayClass.class);
        var arrClass = method.parameters().get(0).parameterType().toClass();
        var field = arrClass.field("array");
        var type = field.fieldType();
        assertInstanceOf(EClass.class, type);
        assertEquals(ClassType.ARRAY, type.toClass().type());
        assertEquals(EClass.fromJava(String.class), type.toClass().arrayComponent());
    }
}
