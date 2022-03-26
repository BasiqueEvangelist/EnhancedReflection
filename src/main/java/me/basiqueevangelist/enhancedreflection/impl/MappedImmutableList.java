package me.basiqueevangelist.enhancedreflection.impl;

import java.util.*;
import java.util.function.Function;

public class MappedImmutableList<T, S> extends AbstractList<T> implements RandomAccess {
    private final List<S> source;
    private final Function<S, T> mapper;

    public MappedImmutableList(List<S> source, Function<S, T> mapper) {
        this.source = source;
        this.mapper = mapper;
    }

    @Override
    public T get(int index) {
        return mapper.apply(source.get(index));
    }

    @Override
    public int size() {
        return source.size();
    }
}
