package me.basiqueevangelist.enhancedreflection.impl;

import me.basiqueevangelist.enhancedreflection.api.EMethod;
import me.basiqueevangelist.enhancedreflection.api.EParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodCollector {
    private final Map<Signature, EMethod> map = new HashMap<>();

    public void add(EMethod method) {
        if (!method.isPublic()) return;

        Signature signature = new Signature(method);
        EMethod existing = map.get(signature);

        if (existing != null) {
            if (method.rawReturnType().isAssignableFrom(existing.rawReturnType())) return;
        }

        map.put(signature, method);
    }

    public List<EMethod> toList() {
        return new ArrayList<>(map.values());
    }

    private static class Signature {
        private final EMethod method;
        private int hashCode;

        public Signature(EMethod method) {
            this.method = method;

            this.hashCode = method.name().hashCode();
            for (EParameter param : method.parameters()) {
                hashCode = 31 * hashCode + System.identityHashCode(param.rawParameterType().raw());
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Signature signature = (Signature) o;

            if (!method.name().equals(method.name())) return false;
            if (method.parameters().size() != signature.method.parameters().size()) return false;

            for (int i = 0; i < method.parameters().size(); i++) {
                if (method.parameters().get(i).rawParameterType().raw() != signature.method.parameters().get(i).rawParameterType().raw()) return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            return hashCode;
        }
    }
}
