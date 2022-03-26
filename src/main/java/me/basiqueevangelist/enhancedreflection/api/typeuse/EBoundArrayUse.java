package me.basiqueevangelist.enhancedreflection.api.typeuse;

import me.basiqueevangelist.enhancedreflection.api.EClass;

public interface EBoundArrayUse<T> extends EClassUse<T[]> {
    EClassUse<T> arrayComponent();
}
