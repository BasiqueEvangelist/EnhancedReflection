package me.basiqueevangelist.enhancedreflection.api;

import me.basiqueevangelist.enhancedreflection.impl.EPackageImpl;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public interface EPackage extends EAnnotated {
    @Contract("null -> null; !null -> new")
    static EPackage fromJava(Package pkg) {
        if (pkg == null) return null;
        else return new EPackageImpl(pkg);
    }

    String name();

    @Nullable String specTitle();
    @Nullable String specVendor();
    @Nullable String specVersion();

    @Nullable String implTitle();
    @Nullable String implVendor();
    @Nullable String implVersion();

    Package raw();
}
