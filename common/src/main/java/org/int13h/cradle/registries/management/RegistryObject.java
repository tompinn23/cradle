package org.int13h.cradle.registries.management;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;
import java.util.function.Supplier;

@ApiStatus.Internal
public class RegistryObject<T> implements RegistrySupplier<T> {

    protected Supplier<T> object;
    protected Consumer<RegistrySupplier<T>> listener;
    private DeferredRegistry<T> registry;
    protected boolean isRegistered;
    protected ResourceLocation identifier;

    public RegistryObject(ResourceLocation identifier, Supplier<T> object, DeferredRegistry<T> registry) {
        this.identifier = identifier;
        this.object = memoize(object);
        this.registry = registry;
    }

    @Override
    public T get() {
        return object.get();
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }

    @Override
    public String toString() {
        return identifier.toString();
    }

    public void setRegistered() {
        this.isRegistered = true;
        if(this.listener != null) {
            this.listener.accept(this);
        }
    }

    public boolean isRegistered() {
        return this.isRegistered;
    }

    public ResourceLocation getIdentifier() {
        return identifier;
    }

    private static <T> Supplier<T> memoize(Supplier<T> original) {
        return new Supplier<T>() {
            Supplier<T> delegate = this::firstTime;
            boolean initialized;
            @Override
            public T get() {
                return delegate.get();
            }

            private synchronized T firstTime() {
                if(!initialized) {
                    T value = original.get();
                    delegate=() -> value;
                    initialized=true;
                }
                return delegate.get();
            }
        };
    }


    @Override
    public DeferredRegistry<T> getRegistry() {
        return this.registry;
    }

    @Override
    public boolean isPresent() {
        return this.isRegistered;
    }
}