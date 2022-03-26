package me.basiqueevangelist.enhancedreflection.impl;

import java.util.function.Supplier;

public class Lazy<T> implements Supplier<T> {
    private T value;
    private volatile Supplier<T> factory;

    public Lazy(Supplier<T> factory) {
        this.factory = factory;
    }

    @Override
    public T get() {
        if (factory != null)
            synchronized (this) {
                if (factory == null) return value;
                value = factory.get();
                factory = null;
            }

        return value;
    }
}
