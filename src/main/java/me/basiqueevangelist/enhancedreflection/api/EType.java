package me.basiqueevangelist.enhancedreflection.api;

import me.basiqueevangelist.enhancedreflection.impl.*;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.*;
import java.util.List;

@ApiStatus.NonExtendable
public interface EType {
    @SuppressWarnings({"unchecked", "rawtypes"})
    static EType fromJava(Type type) {
        if (type instanceof Class<?> klass)
            return new EClassImpl<>(klass);
        else if (type instanceof ParameterizedType ptype) {
            Type[] jArgs = ptype.getActualTypeArguments();
            EType[] paramValues = new EType[jArgs.length];

            for (int i = 0; i < paramValues.length; i++) {
                paramValues[i] = fromJava(jArgs[i]);
            }

            return new GenericEClassImpl<>(List.of(paramValues), (Class<?>)ptype.getRawType());
        }
        else if (type instanceof TypeVariable<?> var)
            return new ETypeVariableImpl(var);
        else if (type instanceof WildcardType wildcard)
            return new EWildcardImpl(wildcard);
        else if (type instanceof GenericArrayType arr) {
            var component = EType.fromJava(arr.getGenericComponentType());

            if (component instanceof EClass<?> klass)
                return new GenericArrayEClassImpl(klass);
            else
                return new EUnboundArrayImpl(component);
        }
        else
            throw new UnsupportedOperationException("Unknown Type subclass " + type.getClass().getName());
    }

    EType tryResolve(GenericTypeContext ctx);

    default EClass<?> toClass() {
        return CommonTypes.OBJECT;
    }
}
