package me.basiqueevangelist.enhancedreflection.impl.typeuse;

import me.basiqueevangelist.enhancedreflection.api.EUnboundArray;
import me.basiqueevangelist.enhancedreflection.api.EncounteredTypes;
import me.basiqueevangelist.enhancedreflection.api.GenericTypeContext;
import me.basiqueevangelist.enhancedreflection.api.typeuse.EClassUse;
import me.basiqueevangelist.enhancedreflection.api.typeuse.ETypeUse;
import me.basiqueevangelist.enhancedreflection.api.typeuse.EUnboundArrayUse;
import me.basiqueevangelist.enhancedreflection.impl.EAnnotatedImpl;
import me.basiqueevangelist.enhancedreflection.impl.EUnboundArrayImpl;

import java.lang.reflect.AnnotatedType;

public class EUnboundArrayUseImpl extends EAnnotatedImpl<AnnotatedType> implements EUnboundArrayUse {
    private final ETypeUse component;
    private final EUnboundArray type;

    public EUnboundArrayUseImpl(AnnotatedType raw, ETypeUse component) {
        super(raw);

        this.component = component;
        this.type = new EUnboundArrayImpl(component.type());
    }


    @Override
    public ETypeUse tryResolve(GenericTypeContext ctx, EncounteredTypes encounteredTypes) {
        ETypeUse newComponent = component.tryResolve(ctx, encounteredTypes);

        if (newComponent instanceof EClassUse<?> klass) return new GenericArrayEClassUseImpl<>(raw, klass);
        else if (newComponent != component) return new EUnboundArrayUseImpl(raw, newComponent);
        else return this;
    }

    @Override
    public ETypeUse arrayComponent() {
        return component;
    }

    @Override
    public EUnboundArray type() {
        return type;
    }
}
