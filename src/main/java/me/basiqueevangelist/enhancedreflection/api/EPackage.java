package me.basiqueevangelist.enhancedreflection.api;

import me.basiqueevangelist.enhancedreflection.impl.EPackageImpl;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

@ApiStatus.NonExtendable
public interface EPackage extends EAnnotated {
    @Contract("null -> null; !null -> new")
    static EPackage fromJava(Package pkg) {
        if (pkg == null) return null;
        else return new EPackageImpl(pkg);
    }

    String name();

    String specTitle();
    String specVendor();
    String specVersion();

    String implTitle();
    String implVendor();
    String implVersion();

    Package raw();
}
