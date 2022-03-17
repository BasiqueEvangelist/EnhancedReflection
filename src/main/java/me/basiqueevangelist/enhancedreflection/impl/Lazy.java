package me.basiqueevangelist.enhancedreflection.impl;

import java.util.function.Supplier;

public class Lazy<T> implements Supplier<T> {
    private volatile T value;
    private final Supplier<T> factory;

    public Lazy(Supplier<T> factory) {
        this.factory = factory;
    }

    @Override
    public T get() {
        if (value == null)
            synchronized (this) {
                if (value != null) return value;
                value = factory.get();
            }

        return value;
    }
}
