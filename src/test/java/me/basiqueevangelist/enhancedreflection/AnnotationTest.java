package me.basiqueevangelist.enhancedreflection;

import me.basiqueevangelist.enhancedreflection.api.EClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@AnnotationTest.TestAnnotation(10)
public class AnnotationTest {
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestAnnotation {
        int value();
    }

    @Test
    public void getAnnotation() {
        var klass = EClass.fromJava(AnnotationTest.class);
        Assertions.assertTrue(klass.hasAnnotation(TestAnnotation.class));
        TestAnnotation annotation = klass.annotation(TestAnnotation.class);
        Assertions.assertEquals(10, annotation.value());
    }
}
