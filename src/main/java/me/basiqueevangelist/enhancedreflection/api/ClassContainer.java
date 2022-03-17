package me.basiqueevangelist.enhancedreflection.api;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface ClassContainer {
    default EClass<?> assertClass() {
        return (EClass<?>) this;
    }

    default EExecutable assertExecutable() {
        return (EExecutable) this;
    }

    default EConstructor<?> assertConstructor() {
        return (EConstructor<?>) this;
    }

    default EMethod assertMethod() {
        return (EMethod) this;
    }
}
