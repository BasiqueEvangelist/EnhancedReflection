package me.basiqueevangelist.enhancedreflection;

import me.basiqueevangelist.enhancedreflection.api.EClass;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

public class HidingTest {
    private void methodShouldBeHidden() {

    }

    private int fieldShouldBeHidden;

    @Test
    public void privateMethodsHidden() {
        var klass = EClass.fromJava(MethodTest.class);
        var method = klass.method("methodShouldBeHidden");
        assertNull(method);

        var methods = klass.methods();
        assertFalse(methods.stream().anyMatch(x -> x.name().equals("methodShouldBeHidden")));
    }

    @Test
    public void privateFieldsHidden() {
        var klass = EClass.fromJava(MethodTest.class);
        var field = klass.field("fieldShouldBeHidden");
        assertNull(field);

        var fields = klass.fields();
        assertFalse(fields.stream().anyMatch(x -> x.name().equals("fieldShouldBeHidden")));
    }
}
