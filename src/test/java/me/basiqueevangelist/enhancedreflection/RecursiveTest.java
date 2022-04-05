package me.basiqueevangelist.enhancedreflection;

import me.basiqueevangelist.enhancedreflection.api.EClass;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RecursiveTest {
    public enum TestEnum {
        A, B, C
    }

    private <E extends Enum<E>> void testMethod(Enum<E> theEnum) {

    }

    @Test
    public void recursiveResolve() {
        var klass = EClass.fromJava(RecursiveTest.class);
        var method = klass.declaredMethod("testMethod", Enum.class);
        var paramClass = method.parameters().get(0).parameterType().upperBound();

        assertEquals(Enum.class, paramClass.raw());
    }
}
