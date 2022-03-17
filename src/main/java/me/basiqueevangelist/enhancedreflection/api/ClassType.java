package me.basiqueevangelist.enhancedreflection.api;

public enum ClassType {
    CLASS, PRIMITIVE, ARRAY, INTERFACE, ENUM, RECORD, ANNOTATION;

    boolean isInterface() {
        return this == INTERFACE || this == ANNOTATION;
    }
}
