package me.basiqueevangelist.enhancedreflection.api;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface EUnboundArray extends EType {
    EType componentType();

    EUnboundArray arrayOf();
}
