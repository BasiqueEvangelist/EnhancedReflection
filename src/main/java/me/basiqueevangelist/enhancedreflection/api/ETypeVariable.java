package me.basiqueevangelist.enhancedreflection.api;

import me.basiqueevangelist.enhancedreflection.api.typeuse.ETypeUse;
import me.basiqueevangelist.enhancedreflection.api.typeuse.ETypeVariableUse;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.reflect.TypeVariable;
import java.util.List;

@ApiStatus.NonExtendable
public interface ETypeVariable extends EType {
    String name();
    @Unmodifiable List<EType> bounds();
    @Unmodifiable List<ETypeUse> annotatedBounds();

    TypeVariable<?> raw();

    EUnboundArray arrayOf();

    @Override
    ETypeVariableUse asEmptyUse();
}
