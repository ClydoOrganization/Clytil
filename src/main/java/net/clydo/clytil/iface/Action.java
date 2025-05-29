package net.clydo.clytil.iface;

import net.clydo.clytil.reflect.FieldValue;
import net.clydo.clytil.reflect.MethodInvoker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface Action {

    void call();

    static @NotNull <O> Action fromMethod(
            @NotNull final MethodInvoker<O, Action> methodInvoker,
            @Nullable final O owner
    ) {
        return () -> methodInvoker.invoke(owner, new Object[0]);
    }

    static @NotNull <O> Action fromField(
            @NotNull final FieldValue<O, Action> fieldValue,
            @Nullable final O owner
    ) {
        return fieldValue.get(owner);
    }

    static Action of(
            @NotNull final Action action
    ) {
        return action;
    }

}
