package org.int13h.cradle.services;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.int13h.cradle.registries.management.DeferredRegistry;

public interface RegistryService {

    <T> DeferredRegistry<T> create(String ID, ResourceKey<? extends Registry<T>> key);

    DeferredRegistry.Blocks createBlocks(String ID);

    DeferredRegistry.Items createItems(String ID);

    void register();
}