package me.basiqueevangelist.enhancedreflection.api.typeuse;

import me.basiqueevangelist.enhancedreflection.api.ETypeVariable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public interface ETypeVariableUse extends ETypeUse {
    @Unmodifiable List<ETypeUse> bounds();

    @Override
    ETypeVariable type();
}
