package me.basiqueevangelist.enhancedreflection.api.typeuse;

import me.basiqueevangelist.enhancedreflection.api.EUnboundArray;

public interface EUnboundArrayUse extends ETypeUse
{
    ETypeUse arrayComponent();

    @Override
    EUnboundArray type();
}
