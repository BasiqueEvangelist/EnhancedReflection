package me.basiqueevangelist.enhancedreflection.api;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.reflect.TypeVariable;
import java.util.List;

@ApiStatus.NonExtendable
public interface ETypeVariable extends EType {
    String name();
    @Unmodifiable List<EType> bounds();

    TypeVariable<?> raw();

    EUnboundArray arrayOf();
}
