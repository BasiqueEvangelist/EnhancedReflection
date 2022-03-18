package me.basiqueevangelist.enhancedreflection.api;

import me.basiqueevangelist.enhancedreflection.impl.EWildcardImpl;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.reflect.WildcardType;
import java.util.List;

@ApiStatus.NonExtendable
public interface EWildcard extends EType {
    static EWildcard fromJava(WildcardType wildcard) {
        return new EWildcardImpl(wildcard);
    }

    @Unmodifiable List<EType> upperBounds();
    @Unmodifiable List<EType> lowerBounds();

    WildcardType raw();

    EUnboundArray arrayOf();
}
