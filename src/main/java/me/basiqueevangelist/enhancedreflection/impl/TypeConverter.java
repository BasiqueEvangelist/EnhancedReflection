package me.basiqueevangelist.enhancedreflection.impl;

import me.basiqueevangelist.enhancedreflection.api.EClass;
import me.basiqueevangelist.enhancedreflection.api.EType;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeConverter {
    public static EType fromJava(Type type) {
        return fromJavaWith(type, new HashMap<>());
    }

    private static EType fromJavaWith(Type type, Map<Type, EType> seenTypes) {
        EType existingType = seenTypes.get(type);
        if (existingType != null) return existingType;

        if (type instanceof Class<?> klass) {
            EClassImpl<?> impl = new EClassImpl<>(klass);
            seenTypes.put(type, impl);
            return impl;
        } else if (type instanceof ParameterizedType ptype) {
            GenericEClassImpl<?> impl = new GenericEClassImpl<>((Class<?>)ptype.getRawType());
            seenTypes.put(type, impl);

            Type[] jArgs = ptype.getActualTypeArguments();
            EType[] paramValues = new EType[jArgs.length];

            for (int i = 0; i < paramValues.length; i++) {
                paramValues[i] = fromJavaWith(jArgs[i], seenTypes);
            }

            impl.setTypeParamValues(List.of(paramValues));
            return impl;
        }
        else if (type instanceof TypeVariable<?> var) {
            ETypeVariableImpl impl = new ETypeVariableImpl(var);
            seenTypes.put(type, impl);

            Type[] jBounds = var.getBounds();
            EType[] bounds = new EType[jBounds.length];

            for (int i = 0; i < bounds.length; i++) {
                bounds[i] = fromJavaWith(jBounds[i], seenTypes);
            }

            impl.setBounds(List.of(bounds));
            return impl;
        } else if (type instanceof WildcardType wildcard) {
            EWildcardImpl impl = new EWildcardImpl();
            seenTypes.put(type, impl);

            Type[] jUpper = wildcard.getUpperBounds();
            EType[] upper = new EType[jUpper.length];

            for (int i = 0; i < upper.length; i++) {
                upper[i] = fromJavaWith(jUpper[i], seenTypes);
            }

            Type[] jLower = wildcard.getLowerBounds();
            EType[] lower = new EType[jLower.length];

            for (int i = 0; i < lower.length; i++) {
                lower[i] = fromJavaWith(jLower[i], seenTypes);
            }

            impl.setLowerBounds(List.of(lower));
            impl.setUpperBounds(List.of(upper));
            return impl;
        } else if (type instanceof GenericArrayType arr) {
            var component = fromJavaWith(arr.getGenericComponentType(), seenTypes);
            EType impl;

            if (component instanceof EClass<?> klass)
                impl = new GenericArrayEClassImpl<>(klass);
            else
                impl = new EUnboundArrayImpl(component);

            seenTypes.put(type, impl);
            return impl;
        }
        else
            throw new UnsupportedOperationException("Unknown Type subclass " + type.getClass().getName());
    }
}
