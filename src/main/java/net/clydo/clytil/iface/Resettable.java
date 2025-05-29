package net.clydo.clytil.iface;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Resettable {

    void reset();

    static Resettable of(@NotNull final Resettable resettable) {
        return resettable;
    }

}
