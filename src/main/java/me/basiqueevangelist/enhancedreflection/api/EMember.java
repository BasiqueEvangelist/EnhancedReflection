package me.basiqueevangelist.enhancedreflection.api;

public interface EMember extends ModifierHolder, EAnnotated {
    String name();
    EClass<?> declaringClass();
}
