package me.basiqueevangelist.enhancedreflection.api;

import me.basiqueevangelist.enhancedreflection.api.typeuse.ETypeUse;
import me.basiqueevangelist.enhancedreflection.api.typeuse.EWildcardUse;
import me.basiqueevangelist.enhancedreflection.impl.TypeConverter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.reflect.WildcardType;
import java.util.List;

@ApiStatus.NonExtendable
public interface EWildcard extends EType {
    static EWildcard fromJava(WildcardType wildcard) {
        return (EWildcard) TypeConverter.fromJava(wildcard);
    }

    @Unmodifiable List<EType> upperBounds();
    @Unmodifiable List<EType> lowerBounds();

    EUnboundArray arrayOf();

    @Override
    EWildcardUse asEmptyUse();
}
