package me.basiqueevangelist.enhancedreflection;

import me.basiqueevangelist.enhancedreflection.api.EClass;
import me.basiqueevangelist.enhancedreflection.api.EField;
import me.basiqueevangelist.enhancedreflection.api.EMethod;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GenericTest extends ExampleGenericClass<Long> {
    @Test
    public void superclassTypeParam() {
        EClass<GenericTest> klass = EClass.fromJava(GenericTest.class);
        EClass<?> superklass = klass.superclass();
        assertTrue(superklass.isGeneric());
        assertEquals(EClass.fromJava(Long.class), superklass.resolveTypeVariable(superklass.typeVariables().get(0)));
    }

    @Test
    public void superclassField() throws IllegalAccessException {
        EClass<GenericTest> klass = EClass.fromJava(GenericTest.class);
        EClass<?> superklass = klass.superclass();
        EField field = superklass.field("testField");
        assertNotNull(field);
        assertEquals("testField", field.name());
        assertEquals(EClass.fromJava(Long.class), field.fieldType());

        assertNull(testField);
        field.set(this, 20L);
        assertEquals(20L, testField);
    }

    @Test
    public void superclassFieldHere() throws IllegalAccessException {
        EClass<GenericTest> klass = EClass.fromJava(GenericTest.class);
        EField field = klass.field("testField");
        assertNotNull(field);
        assertEquals("testField", field.name());
        assertEquals(EClass.fromJava(Long.class), field.fieldType());

        assertNull(testField);
        field.set(this, 20L);
        assertEquals(20L, testField);
    }

    @Test
    public void superclassMethodHere() {
        EClass<GenericTest> klass = EClass.fromJava(GenericTest.class);
        EMethod method = klass.method("testMethod", Object.class);
        assertNotNull(method);
        assertEquals("testMethod", method.name());
        assertEquals(EClass.fromJava(Long.class), method.parameters().get(0).parameterType());
    }

    @Test
    public void instantiateGenerics() {
        var klass = EClass.fromJava(Iterable.class);
        var inst = klass.instantiateWith(Character.class);
        var iter = inst.method("iterator").returnType().toClass();
        var ret = iter.method("next").returnType().toClass();
        assertEquals(EClass.fromJava(Character.class), ret);
    }
}
