package net.clydo.clytil.iface.value;

import lombok.val;
import net.clydo.clytil.Numbers;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public interface DefaultValue<V> {

    V def();

    default <U> V safeCast(U value) {
        val def = this.def();
        if (value != null) {
            if (value instanceof Number valueNumber && def instanceof Number defaultNumber) {
                value = (U) Numbers.cast(valueNumber, defaultNumber);
            }

            try {
                return (V) value;
            } catch (ClassCastException e) {
                //LOGGER.warn("Cannot cast {} to {}", value, def);
            }
        }
        return def;
    }

    default <C extends V> C defAs(final @NotNull Class<C> type) {
        val value = this.def();
        return value != null ? type.cast(value) : null;
    }

    default <C extends V> C defAs() {
        val value = this.def();
        return value != null ? (C) value : null;
    }

    default <C> C unsafeDefAs(final @NotNull Class<C> type) {
        val value = this.def();
        return value != null ? type.cast(value) : null;
    }

    default <C> C unsafeDefAs() {
        val value = this.def();
        return value != null ? (C) value : null;
    }

}
