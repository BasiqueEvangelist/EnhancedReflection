package me.basiqueevangelist.enhancedreflection.impl;

import me.basiqueevangelist.enhancedreflection.api.EType;
import me.basiqueevangelist.enhancedreflection.api.typeuse.ETypeUse;
import me.basiqueevangelist.enhancedreflection.impl.typeuse.EmptyAnnotatedType;

import java.lang.reflect.AnnotatedType;

public interface ETypeInternal<U extends ETypeUse> extends EType {
    U asUseWith(AnnotatedType data);
}
