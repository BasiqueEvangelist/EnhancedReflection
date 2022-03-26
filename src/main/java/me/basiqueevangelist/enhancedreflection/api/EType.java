package me.basiqueevangelist.enhancedreflection.api;

import me.basiqueevangelist.enhancedreflection.api.typeuse.ETypeUse;
import me.basiqueevangelist.enhancedreflection.impl.*;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.*;

@ApiStatus.NonExtendable
public interface EType {
    static EType fromJava(Type type) {
        return TypeConverter.fromJava(type);
    }

    EType tryResolve(GenericTypeContext ctx, EncounteredTypes encounteredTypes);
    EType arrayOf();

    default EClass<?> upperBound() {
        return CommonTypes.OBJECT;
    }

    default EClass<?> lowerBound() {
        return CommonTypes.OBJECT;
    }

    ETypeUse asEmptyUse();
}
