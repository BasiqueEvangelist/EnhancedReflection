package me.basiqueevangelist.enhancedreflection;

import me.basiqueevangelist.enhancedreflection.api.EClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

public class SuperclassTest<T> extends BasedClass<T> implements Iterable<T> {
    public void based(SuperclassTest<String> klass) {

    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void readInterfaces() {
        var klass = EClass.fromJava(SuperclassTest.class);
        var based = klass.method("based", SuperclassTest.class);
        var klass2 = based.parameters().get(0).parameterType().toClass();
        var iface = klass2.interfaces().get(0);
        var iteratorMethod = iface.method("iterator");
        var iteratorType = iteratorMethod.returnType().toClass();
        var nextMethod = iteratorType.method("next");
        Assertions.assertEquals(String.class, nextMethod.returnType().toClass().raw());
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void readSuperclass() {
        var klass = EClass.fromJava(SuperclassTest.class);
        var based = klass.method("based", SuperclassTest.class);
        var klass2 = based.parameters().get(0).parameterType().toClass();
        var superklass = klass2.superclass();
        var field = superklass.field("thing");
        Assertions.assertEquals(String.class, field.fieldType().toClass().raw());
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException("mald about it");
    }
}

class BasedClass<T> {
    public T thing;
}