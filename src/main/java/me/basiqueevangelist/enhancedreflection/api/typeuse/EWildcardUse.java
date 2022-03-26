package me.basiqueevangelist.enhancedreflection.api.typeuse;

import me.basiqueevangelist.enhancedreflection.api.EType;
import me.basiqueevangelist.enhancedreflection.api.EWildcard;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public interface EWildcardUse extends ETypeUse {
    @Unmodifiable List<ETypeUse> upperBounds();
    @Unmodifiable List<ETypeUse> lowerBounds();

    @Override
    EWildcard type();
}
