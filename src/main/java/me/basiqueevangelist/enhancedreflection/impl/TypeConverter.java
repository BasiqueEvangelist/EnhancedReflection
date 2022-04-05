package me.basiqueevangelist.enhancedreflection.impl;

import me.basiqueevangelist.enhancedreflection.api.EClass;
import me.basiqueevangelist.enhancedreflection.api.EType;
import me.basiqueevangelist.enhancedreflection.api.typeuse.EClassUse;
import me.basiqueevangelist.enhancedreflection.api.typeuse.ETypeUse;
import me.basiqueevangelist.enhancedreflection.impl.typeuse.*;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeConverter {
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static EType fromJava(Type type) {
        HashMap map = new HashMap<>();
        return fromJavaWith(type, map, map);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static ETypeUse fromJava(AnnotatedType type) {
        HashMap map = new HashMap<>();
        return fromJavaWith(type, map, map);
    }

    private static EType fromJavaWith(Type type, Map<Type, EType> seenTypes, Map<AnnotatedType, ETypeUse> seenTypeUses) {
        EType existingType = seenTypes.get(type);
        if (existingType != null) return existingType;

        if (type instanceof Class<?> klass) {
            EClassImpl<?> impl = EClassImpl.fromJava(klass);
            seenTypes.put(type, impl);
            return impl;
        } else if (type instanceof ParameterizedType ptype) {
            GenericEClassImpl<?> impl = new GenericEClassImpl<>((Class<?>)ptype.getRawType());
            seenTypes.put(type, impl);

            Type[] jArgs = ptype.getActualTypeArguments();
            EType[] paramValues = new EType[jArgs.length];

            for (int i = 0; i < paramValues.length; i++) {
                paramValues[i] = fromJavaWith(jArgs[i], seenTypes, seenTypeUses);
            }

            impl.init(List.of(paramValues));
            return impl;
        }
        else if (type instanceof TypeVariable<?> var) {
            ETypeVariableImpl impl = new ETypeVariableImpl(var);
            seenTypes.put(type, impl);

            AnnotatedType[] jBounds = var.getAnnotatedBounds();
            ETypeUse[] bounds = new ETypeUse[jBounds.length];

            for (int i = 0; i < bounds.length; i++) {
                bounds[i] = fromJavaWith(jBounds[i], seenTypes, seenTypeUses);
            }

            impl.init(List.of(bounds));
            return impl;
        } else if (type instanceof WildcardType wildcard) {
            EWildcardImpl impl = new EWildcardImpl();
            seenTypes.put(type, impl);

            Type[] jUpper = wildcard.getUpperBounds();
            EType[] upper = new EType[jUpper.length];

            for (int i = 0; i < upper.length; i++) {
                upper[i] = fromJavaWith(jUpper[i], seenTypes, seenTypeUses);
            }

            Type[] jLower = wildcard.getLowerBounds();
            EType[] lower = new EType[jLower.length];

            for (int i = 0; i < lower.length; i++) {
                lower[i] = fromJavaWith(jLower[i], seenTypes, seenTypeUses);
            }

            impl.init(List.of(lower), List.of(upper));
            return impl;
        } else if (type instanceof GenericArrayType arr) {
            var component = fromJavaWith(arr.getGenericComponentType(), seenTypes, seenTypeUses);
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

    private static ETypeUse fromJavaWith(AnnotatedType type, Map<Type, EType> seenTypes, Map<AnnotatedType, ETypeUse> seenTypeUses) {
        ETypeUse existingTypeUse = seenTypeUses.get(type);
        if (existingTypeUse != null) return existingTypeUse;

        if (type instanceof AnnotatedParameterizedType pType) {
            GenericEClassUseImpl<?> impl = new GenericEClassUseImpl<>(pType, (Class<?>) ((ParameterizedType) pType.getType()).getRawType());
            seenTypeUses.put(type, impl);

            AnnotatedType[] jArgs = pType.getAnnotatedActualTypeArguments();
            ETypeUse[] paramValues = new ETypeUse[jArgs.length];

            for (int i = 0; i < paramValues.length; i++) {
                paramValues[i] = fromJavaWith(jArgs[i], seenTypes, seenTypeUses);
            }

            impl.init(List.of(paramValues));
            return impl;
        } else if (type instanceof AnnotatedArrayType arr) {
            var component = fromJavaWith(arr.getAnnotatedGenericComponentType(), seenTypes, seenTypeUses);
            ETypeUse impl;

            if (component instanceof EClassUse<?> klass)
                impl = new GenericArrayEClassUseImpl<>(arr, klass);
            else
                impl = new EUnboundArrayUseImpl(arr, component);

            seenTypeUses.put(type, impl);
            return impl;
        } else if (type instanceof AnnotatedTypeVariable typeVar) {
            var impl = new ETypeVariableUseImpl(typeVar, (TypeVariable<?>) typeVar.getType());
            seenTypeUses.put(type, impl);

            AnnotatedType[] jBounds = typeVar.getAnnotatedBounds();
            ETypeUse[] bounds = new ETypeUse[jBounds.length];

            for (int i = 0; i < bounds.length; i++) {
                bounds[i] = fromJavaWith(jBounds[i], seenTypes, seenTypeUses);
            }

            impl.init(List.of(bounds));
            return impl;
        } else if (type instanceof AnnotatedWildcardType wildcard) {
            var impl = new EWildcardUseImpl(wildcard);
            seenTypeUses.put(type, impl);

            AnnotatedType[] jUpper = wildcard.getAnnotatedUpperBounds();
            ETypeUse[] upper = new ETypeUse[jUpper.length];

            for (int i = 0; i < upper.length; i++) {
                upper[i] = fromJavaWith(jUpper[i], seenTypes, seenTypeUses);
            }

            AnnotatedType[] jLower = wildcard.getAnnotatedLowerBounds();
            ETypeUse[] lower = new ETypeUse[jLower.length];

            for (int i = 0; i < lower.length; i++) {
                lower[i] = fromJavaWith(jLower[i], seenTypes, seenTypeUses);
            }

            impl.init(List.of(lower), List.of(upper));
            return impl;
        } else {
            var impl = new EClassUseImpl<>(type, (EClass<?>) fromJavaWith(type.getType(), seenTypes, seenTypeUses));
            seenTypeUses.put(type, impl);
            return impl;
        }
    }
}
