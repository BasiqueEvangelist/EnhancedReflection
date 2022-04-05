package me.basiqueevangelist.enhancedreflection.api.typeuse;

import me.basiqueevangelist.enhancedreflection.api.EClass;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public interface EClassUse<T> extends ETypeUse {
    @Unmodifiable List<ETypeUse> typeVariableValues();

    @Nullable EClassUse<?> arrayComponent();

    @Override
    EClass<T> type();
}
