package org.int13h.cradle.registries.management;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface RegistrySupplier<T> extends Supplier<T> {
    DeferredRegistry<T> getRegistry();

    boolean isPresent();

    @Nullable
    default T getOrNull() {
        if(isPresent()) {
            return get();
        }
        return null;
    }

    default Optional<T> toOptional() {
        return Optional.ofNullable(getOrNull());
    }

    default void ifPresent(Consumer<? super T> consumer) {
        if(isPresent()) {
            consumer.accept(get());
        }
    }

    default void ifPresentOrElse(Consumer<? super T> consumer, Runnable runnable) {
        if(isPresent()) {
            consumer.accept(get());
        } else {
            runnable.run();
        }
    }

    default Stream<T> stream() {
        if(!isPresent()) {
            return Stream.empty();
        } else {
            return Stream.of(get());
        }
    }

    default T orElse(T other) {
        return isPresent() ? get() : other;
    }

    default T orElseGet(Supplier<? extends T> other) {
        return isPresent() ? get() : other.get();
    }

}