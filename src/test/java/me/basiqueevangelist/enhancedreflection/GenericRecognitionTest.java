package me.basiqueevangelist.enhancedreflection;

import me.basiqueevangelist.enhancedreflection.api.EClass;
import me.basiqueevangelist.enhancedreflection.api.ModifierHolder;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GenericRecognitionTest {
    public void based(Function<Integer, Long> handler) {
        handler.apply(10);
    }

    @Test
    public void readGenerics() {
        var klass = EClass.fromJava(GenericRecognitionTest.class);
        var method = klass.method("based", Function.class);
        assertNotNull(method);
        var param = method.parameters().get(0);
        var pType = param.parameterType().toClass();
        var method2 = pType.methods().stream().filter(ModifierHolder::isAbstract).findAny().get();
        assertEquals("java.lang.Integer arg0", method2.parameters().get(0).toString());
    }
}
