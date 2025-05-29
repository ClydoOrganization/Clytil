package net.clydo.clytil.reflect;

import lombok.experimental.UtilityClass;
import lombok.val;
import net.clydo.clytil.Validates;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class MethodInvokers {

    public <O, R> MethodInvoker<O, R> of(
            @NotNull final String clazz,
            @NotNull final String name,
            @NotNull final Class<?>... argTypes
    ) {
        Validates.requireParam(clazz, "class");
        Validates.requireParam(name, "name");
        Validates.requireParam(argTypes, "argTypes");

        try {
            return MethodInvokers.of(
                    Class.forName(clazz),
                    name,
                    argTypes
            );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                    String.format(
                            "Failed to find class %s!",
                            clazz
                    ), e
            );
        }
    }

    public <O, R> MethodInvoker<O, R> of(
            @NotNull final Class<?> clazz,
            @NotNull final String name,
            @NotNull final Class<?>... argTypes
    ) {
        Validates.requireParam(clazz, "class");
        Validates.requireParam(name, "name");
        Validates.requireParam(argTypes, "argTypes");

        val method = Reflects.getMethod(clazz, name, argTypes);

        method.setAccessible(true);

        return (owner, args) -> Methods.get(clazz, method, owner, args);
    }

}
