package me.basiqueevangelist.enhancedreflection;

import me.basiqueevangelist.enhancedreflection.api.EClass;
import org.junit.jupiter.api.Test;

public class EnumTest {
    public enum TestEnum {
        A, B, C
    }

    @Test
    public void readEnumData() {
        var klass = EClass.fromJava(TestEnum.class);

        klass.allSuperclasses();
        klass.allInterfaces();
    }
}
