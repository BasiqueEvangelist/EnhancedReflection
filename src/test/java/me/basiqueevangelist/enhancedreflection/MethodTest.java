package me.basiqueevangelist.enhancedreflection;

import me.basiqueevangelist.enhancedreflection.api.EClass;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

public class MethodTest {
    @Test
    public void invokeMethod() throws InvocationTargetException, IllegalAccessException {
        var inst = new TestClass();
        var klass = EClass.fromJava(TestClass.class);
        var method = klass.method("testMethod");
        assertNotNull(method);
        assertEquals(method.name(), "testMethod");
        method.invoke(inst);
        assertTrue(TestClass.methodRan);
    }

    @Test
    public void invokeConstructor() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        var klass = EClass.fromJava(TestClass.class);
        var ctor = klass.constructor(int.class);
        assertNotNull(ctor);
        assertEquals(ctor.name(), "me.basiqueevangelist.enhancedreflection.MethodTest$TestClass");

        TestClass inst = ctor.invoke(42);
        assertNotNull(inst);
        assertTrue(TestClass.constructorRan);
    }

    public static class TestClass {
        private static boolean methodRan = false;
        private static boolean constructorRan = false;

        public TestClass() {

        }

        public TestClass(int passed) {
            assertEquals(42, passed);
            constructorRan = true;
        }

        public void testMethod() {
            methodRan = true;
        }
    }
}
