package org.int13h.cradle.fabric.registries.management;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import org.int13h.cradle.registries.management.DeferredRegistry;

public class FabricDeferredRegistry<T> extends DeferredRegistry<T> {

    private final Registry<T> registry;

    @SuppressWarnings("unchecked")
    public FabricDeferredRegistry(String ID, ResourceKey<? extends Registry<T>> KEY) {
        super(ID, KEY);
        registry = (Registry<T>) BuiltInRegistries.REGISTRY.get(this.KEY.location()).orElseThrow(() -> new IllegalStateException("Registry: " + this.KEY.location() + " not found"));
    }
    
    public void _register() {
        for(var entry : entries.entrySet()) {
            Registry.register(registry, entry.getKey().getId(), entry.getValue().get());
            entry.getKey().bind(false);
        }
    }

    public static class Blocks extends DeferredRegistry.Blocks {

        public Blocks(String ID) {
            super(ID);
        }

        public void _register() {
            for(var entry : entries.entrySet()) {
                Registry.register(BuiltInRegistries.BLOCK, entry.getKey().getId(), entry.getValue().get());
                entry.getKey().bind(false);
            }
        }

    }

    public static class Items extends DeferredRegistry.Items {
        public Items(String ID) {
            super(ID);
        }

        public void _register() {
            for(var entry : entries.entrySet()) {
                Registry.register(BuiltInRegistries.ITEM, entry.getKey().getId(), entry.getValue().get());
                entry.getKey().bind(false);
            }
        }
    }
}
