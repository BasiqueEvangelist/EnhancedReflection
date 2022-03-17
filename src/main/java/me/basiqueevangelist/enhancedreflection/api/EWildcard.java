package me.basiqueevangelist.enhancedreflection.api;

import me.basiqueevangelist.enhancedreflection.impl.EWildcardImpl;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.reflect.WildcardType;
import java.util.List;

public interface EWildcard extends EType {
    static EWildcard fromJava(WildcardType wildcard) {
        return new EWildcardImpl(wildcard);
    }

    @Unmodifiable List<EType> upperBounds();
    @Unmodifiable List<EType> lowerBounds();

    WildcardType raw();
}
