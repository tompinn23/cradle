package org.int13h.cradle.neoforge.services;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import org.int13h.cradle.neoforge.registries.management.NeoforgeDeferredRegistry;
import org.int13h.cradle.registries.management.DeferredRegistry;
import org.int13h.cradle.services.RegistryService;

public class NeoforgeRegistryService implements RegistryService {
    @Override
    public <T> DeferredRegistry<T> create(String ID, ResourceKey<? extends Registry<T>> key) {
        return new NeoforgeDeferredRegistry<>(ID, key);
    }

    @Override
    public DeferredRegistry.Blocks createBlocks(String ID) {
        return new NeoforgeDeferredRegistry.Blocks(ID);
    }

    @Override
    public DeferredRegistry.Items createItems(String ID) {
        return new NeoforgeDeferredRegistry.Items(ID);
    }

    @Override
    public void register() {

    }
}
