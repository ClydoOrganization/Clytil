package net.clydo.clytil.iface;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@FunctionalInterface
public interface CloseableScope extends AutoCloseable {

    CloseableScope EMPTY = () -> {
    };

    @Override
    void close();

    default boolean closesNothing() {
        return this == EMPTY;
    }

    default void runAndClose(
            @NotNull final Runnable body
    ) {
        try (this) {
            body.run();
        }
    }

    default <T> T supplyAndClose(
            @NotNull final Supplier<T> body
    ) {
        try (this) {
            return body.get();
        }
    }

    default @NotNull CloseableScope closeThen(
            @NotNull final CloseableScope next
    ) {
        if (next.closesNothing()) {
            return this;
        }
        if (this.closesNothing()) {
            return next;
        }

        return () -> CloseableScope.closeBoth(this, next);
    }

    default @NotNull CloseableScope closeAfter(
            @NotNull final CloseableScope first
    ) {
        return first.closeThen(this);
    }

    default @NotNull CloseableScope closeOnce() {
        if (this.closesNothing()) {
            return this;
        }

        return new CloseableScope() {
            private boolean closed;

            @Override
            public void close() {
                if (this.closed) {
                    return;
                }
                this.closed = true;
                CloseableScope.this.close();
            }
        };
    }

    static @NotNull CloseableScope closeAll(
            @NotNull final CloseableScope @NotNull ... scopes
    ) {
        return switch (scopes.length) {
            case 0 -> EMPTY;
            case 1 -> scopes[0];
            default -> {
                final CloseableScope[] copy = scopes.clone();
                yield () -> {
                    RuntimeException failure = null;
                    for (int i = copy.length - 1; i >= 0; i--) {
                        try {
                            copy[i].close();
                        } catch (final RuntimeException exception) {
                            if (failure == null) {
                                failure = exception;
                            } else {
                                failure.addSuppressed(exception);
                            }
                        }
                    }
                    if (failure != null) {
                        throw failure;
                    }
                };
            }
        };
    }

    private static void closeBoth(
            @NotNull final CloseableScope first,
            @NotNull final CloseableScope second
    ) {
        try {
            first.close();
        } catch (final RuntimeException exception) {
            try {
                second.close();
            } catch (final RuntimeException suppressed) {
                exception.addSuppressed(suppressed);
            }
            throw exception;
        }
        second.close();
    }

}