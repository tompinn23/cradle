package org.int13h.cradle.registries.management;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class RegistryHolder<R, T extends R> implements Holder<R>, Supplier<T> {

    protected final ResourceKey<R> key;
    private Holder<R> holder = null;

    public static <R, T extends R> RegistryHolder<R, T> create(ResourceKey<? extends Registry<R>> key, ResourceLocation name) {
        return create(ResourceKey.create(key, name));
    }

    public static <R, T extends R> RegistryHolder<R, T> create(ResourceLocation registry, ResourceLocation value) {
        return create(ResourceKey.createRegistryKey(registry), value);
    }

    public static <R, T extends R> RegistryHolder<R, T> create(ResourceKey<R> key) {
        return new RegistryHolder<>(key);
    }

    protected RegistryHolder(ResourceKey<R> key) {
        this.key = Objects.requireNonNull(key);
        this.bind(false);
    }

    @SuppressWarnings("unchecked")
    protected @Nullable Registry<R> getRegistry() {
        return (Registry<R>) BuiltInRegistries.REGISTRY.getValue(this.key.registry());
    }

    public final void bind(boolean throwOnMissing) {
        if(this.holder == null) {
            Registry<R> registry = this.getRegistry();
            if(registry == null && throwOnMissing) {
                throw new IllegalStateException("Registry not found for " + this + ": " + this.key.registry());
            } else if(registry != null) {
                this.holder = registry.get(this.key).orElse(null);
            }
        }
    }

    @Override
    public T get() {
        return this.value();
    }


    @Override
    @SuppressWarnings("unchecked")
    public @NotNull T value() {
        this.bind(true);
        if(this.holder != null) {
            return (T) this.holder.value();
        } else {
            throw new IllegalStateException("Trying to access unbound value: " + this.key);
        }
    }

    public ResourceLocation getId() {
        return this.key.location();
    }

    @Override
    public boolean isBound() {
        this.bind(false);
        return this.holder != null && this.holder.isBound();
    }

    @Override
    public boolean is(ResourceLocation id) {
        return  id.equals(this.key.location());
    }

    @Override
    public boolean is(@NotNull ResourceKey<R> key) {
        return key == this.key;
    }

    @Override
    public boolean is(Predicate<ResourceKey<R>> predicate) {
        return predicate.test(this.key);
    }

    @Override
    public boolean is(@NotNull TagKey<R> tagKey) {
        this.bind(false);
        return this.holder != null && this.holder.is(tagKey);
    }

    @Override
    @Deprecated
    public boolean is(@NotNull Holder<R> holder) {
        this.bind(false);
        return this.holder != null && this.holder.is(holder);
    }

    @Override
    public @NotNull Stream<TagKey<R>> tags() {
        this.bind(false);
        return this.holder != null ? this.holder.tags() : Stream.empty();
    }

    @Override
    public @NotNull Either<ResourceKey<R>, R> unwrap() {
        return Either.left(this.key);
    }

    @Override
    public @NotNull Optional<ResourceKey<R>> unwrapKey() {
        return Optional.of(this.key);
    }

    @Override
    public @NotNull Kind kind() {
        return Kind.REFERENCE;
    }

    @Override
    public boolean canSerializeIn(@NotNull HolderOwner<R> holderOwner) {
        this.bind(false);
        return this.holder != null && this.holder.canSerializeIn(holderOwner);
    }


}
