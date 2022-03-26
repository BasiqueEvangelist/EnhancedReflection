package me.basiqueevangelist.enhancedreflection.api;

import me.basiqueevangelist.enhancedreflection.api.typeuse.ETypeUse;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public interface EncounteredTypes {
    static EncounteredTypes create() {
        HashSet<Object> theSet = new HashSet<>();
        return new EncounteredTypes() {
            @Override
            public boolean addType(EType type) {
                return theSet.add(type);
            }

            @Override
            public void removeType(EType type) {
                theSet.remove(type);
            }

            @Override
            public boolean addTypeUse(ETypeUse use) {
                return theSet.add(use);
            }

            @Override
            public void removeTypeUse(ETypeUse use) {
                theSet.remove(use);
            }
        };
    }

    boolean addType(EType type);
    void removeType(EType type);

    boolean addTypeUse(ETypeUse use);
    void removeTypeUse(ETypeUse use);
}
