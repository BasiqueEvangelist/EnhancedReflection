package me.basiqueevangelist.enhancedreflection.api.typeuse;

import me.basiqueevangelist.enhancedreflection.api.EAnnotated;
import me.basiqueevangelist.enhancedreflection.api.EType;
import me.basiqueevangelist.enhancedreflection.api.EncounteredTypes;
import me.basiqueevangelist.enhancedreflection.api.GenericTypeContext;
import me.basiqueevangelist.enhancedreflection.impl.TypeConverter;

import java.lang.reflect.*;

public interface ETypeUse extends EAnnotated {
    static ETypeUse fromJava(AnnotatedType type) {
        return TypeConverter.fromJava(type);
    }

    ETypeUse tryResolve(GenericTypeContext ctx, EncounteredTypes encounteredTypes);
    EType type();
}
