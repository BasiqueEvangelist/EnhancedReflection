package me.basiqueevangelist.enhancedreflection.impl;

import me.basiqueevangelist.enhancedreflection.api.EPackage;
import org.jetbrains.annotations.Nullable;

public class EPackageImpl extends EAnnotatedImpl<Package> implements EPackage {
    public EPackageImpl(Package raw) {
        super(raw);
    }

    @Override
    public String name() {
        return raw.getName();
    }

    @Override
    public String specTitle() {
        return raw.getSpecificationTitle();
    }

    @Override
    public String specVendor() {
        return raw.getSpecificationVendor();
    }

    @Override
    public String specVersion() {
        return raw.getSpecificationVersion();
    }

    @Override
    public String implTitle() {
        return raw.getImplementationTitle();
    }

    @Override
    public String implVendor() {
        return raw.getImplementationVendor();
    }

    @Override
    public String implVersion() {
        return raw.getImplementationVersion();
    }

    @Override
    public Package raw() {
        return raw;
    }

    @Override
    public String toString() {
        return raw.toString();
    }

    @Override
    public int hashCode() {
        return raw.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != getClass()) return false;

        return raw == ((EPackageImpl)obj).raw;
    }
}
