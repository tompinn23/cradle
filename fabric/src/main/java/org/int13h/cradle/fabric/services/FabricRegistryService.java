package org.int13h.cradle.fabric.services;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.int13h.cradle.fabric.registries.management.FabricDeferredRegistry;
import org.int13h.cradle.registries.management.DeferredRegistry;
import org.int13h.cradle.services.RegistryService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FabricRegistryService implements RegistryService {

    private final ResourceLocation registryOrder[];

    private final Map<ResourceKey<? extends Registry>, DeferredRegistry<?>> registries = new LinkedHashMap<>();

    public FabricRegistryService() {
        ArrayList<ResourceLocation> registries = BuiltInRegistries.REGISTRY.keySet()
                .stream()
                .filter(location -> Registries.BLOCK.location() != location && Registries.ITEM.location() != location)
                .sorted().collect(Collectors.toCollection(ArrayList::new));
        registries.add(0, Registries.ITEM.location());
        registries.add(0, Registries.BLOCK.location());
        registryOrder = registries.toArray(ResourceLocation[]::new);
    }

    @Override
    public <T> DeferredRegistry<T> create(String ID, ResourceKey<? extends Registry<T>> key) {
        var registry = new FabricDeferredRegistry<>(ID, key);
        registries.put(key, registry);
        return registry;
    }

    @Override
    public DeferredRegistry.Blocks createBlocks(String ID) {
        var registry = new FabricDeferredRegistry.Blocks(ID);
        registries.put(Registries.BLOCK, registry);
        return registry;
    }

    @Override
    public DeferredRegistry.Items createItems(String ID) {
        var registry = new FabricDeferredRegistry.Items(ID);
        registries.put(Registries.ITEM, registry);
        return registry;
    }

    @Override
    public void register() {
        for(var location : registryOrder) {
            var registry = BuiltInRegistries.REGISTRY.get(location).map(Holder.Reference::key).orElse(null);
            if(registry != null && registries.containsKey(registry)) {
                if(registry.equals(Registries.BLOCK)) {
                    ((FabricDeferredRegistry.Blocks) registries.get(registry))._register();
                } else if(registry.equals(Registries.ITEM)) {
                    ((FabricDeferredRegistry.Items) registries.get(registry))._register();
                } else {
                    ((FabricDeferredRegistry) registries.get(registry))._register();
                }
            }
        }
    }
}
