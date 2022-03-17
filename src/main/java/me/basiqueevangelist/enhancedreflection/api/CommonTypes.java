package me.basiqueevangelist.enhancedreflection.api;

public final class CommonTypes {
    private CommonTypes() {

    }

    public static final EClass<Object> OBJECT = EClass.fromJava(Object.class);
    public static final EClass<String> STRING = EClass.fromJava(String.class);

    public static final EClass<?> BOOLEAN = EClass.fromJava(boolean.class);
    public static final EClass<?> CHAR = EClass.fromJava(char.class);
    public static final EClass<?> BYTE = EClass.fromJava(byte.class);
    public static final EClass<?> SHORT = EClass.fromJava(short.class);
    public static final EClass<?> INT = EClass.fromJava(int.class);
    public static final EClass<?> FLOAT = EClass.fromJava(float.class);
    public static final EClass<?> LONG = EClass.fromJava(long.class);
    public static final EClass<?> DOUBLE = EClass.fromJava(double.class);
}
