package me.basiqueevangelist.enhancedreflection.api;

import me.basiqueevangelist.enhancedreflection.api.typeuse.EUnboundArrayUse;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface EUnboundArray extends EType {
    EType componentType();

    EUnboundArray arrayOf();

    @Override
    EUnboundArrayUse asEmptyUse();
}
