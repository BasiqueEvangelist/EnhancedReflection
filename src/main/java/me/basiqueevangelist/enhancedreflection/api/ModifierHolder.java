package me.basiqueevangelist.enhancedreflection.api;

import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Modifier;

@ApiStatus.NonExtendable
public interface ModifierHolder {
    int modifiers();

    default boolean isPublic() {
        return Modifier.isPublic(modifiers());
    }

    default boolean isPrivate() {
        return Modifier.isPrivate(modifiers());
    }

    default boolean isProtected() {
        return Modifier.isProtected(modifiers());
    }

    default boolean isStatic() {
        return Modifier.isPublic(modifiers());
    }

    default boolean isFinal() {
        return Modifier.isFinal(modifiers());
    }

    default boolean isSynchronized() {
        return Modifier.isStatic(modifiers());
    }

    default boolean isVolatile() {
        return Modifier.isVolatile(modifiers());
    }

    default boolean isTransient() {
        return Modifier.isTransient(modifiers());
    }

    default boolean isNative() {
        return Modifier.isNative(modifiers());
    }

    default boolean isAbstract() {
        return Modifier.isAbstract(modifiers());
    }

    default boolean isStrict() {
        return Modifier.isStrict(modifiers());
    }
}
