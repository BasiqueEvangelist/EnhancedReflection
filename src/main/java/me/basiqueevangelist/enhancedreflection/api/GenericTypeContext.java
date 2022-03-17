package me.basiqueevangelist.enhancedreflection.api;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

@ApiStatus.NonExtendable
public interface GenericTypeContext {
    @Unmodifiable List<ETypeVariable> typeParameters();
    EType resolveTypeVariable(ETypeVariable typeVar);
}
