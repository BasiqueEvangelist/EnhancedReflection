package me.basiqueevangelist.enhancedreflection;

import me.basiqueevangelist.enhancedreflection.api.EClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

@TestAnnotation(10)
public class AnnotationTest implements @TestAnnotation(20) Iterable<@TestAnnotation(30) Long> {

    @Test
    public void getAnnotation() {
        var klass = EClass.fromJava(AnnotationTest.class);
        Assertions.assertTrue(klass.hasAnnotation(TestAnnotation.class));
        TestAnnotation annotation = klass.annotation(TestAnnotation.class);
        Assertions.assertEquals(10, annotation.value());
    }

    @Test
    public void typeUseInInterface() {
        var klass = EClass.fromJava(AnnotationTest.class);
        var superclass = klass.interfaceUses().get(0);
        Assertions.assertTrue(superclass.hasAnnotation(TestAnnotation.class));
        Assertions.assertEquals(20, superclass.annotation(TestAnnotation.class).value());

        var typeValue = superclass.typeVariableValues().get(0);
        Assertions.assertTrue(typeValue.hasAnnotation(TestAnnotation.class));
        Assertions.assertEquals(30, typeValue.annotation(TestAnnotation.class).value());
    }

    @Override
    public Iterator<@TestAnnotation(20) Long> iterator() {
        throw new IllegalStateException("mald about it");
    }
}
