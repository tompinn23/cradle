package org.int13h.cradle.neoforge.registries.management;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.int13h.cradle.neoforge.CradleMod;
import org.int13h.cradle.registries.management.DeferredRegistry;
import org.int13h.cradle.registries.management.RegistrySupplier;

import java.util.function.Function;

public class NeoforgeDeferredRegistry<T> extends DeferredRegistry<T> {

    public NeoforgeDeferredRegistry(String ID, ResourceKey<? extends Registry<T>> KEY) {
        super(ID, KEY);
        CradleMod.getModEventBus().addListener(this::_register);
    }

    public void _register(RegisterEvent event) {
        if(!event.getRegistryKey().equals(this.KEY)) {
            return;
        }
        forEach((location, entry) -> {
            event.register(this.KEY, location, entry);
        });
    }

    public static class Blocks extends DeferredRegistry.Blocks {
        public Blocks(String ID, ResourceKey<? extends Registry<Block>> KEY) {
            super(ID, KEY);
            CradleMod.getModEventBus().addListener(this::_register);
        }

        @Override
        public RegistrySupplier<Block> register(String name, Function<BlockBehaviour.Properties, Block> fac, BlockBehaviour.Properties props) {
            return register(name, () -> fac.apply(props.setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(ID, name)))));
        }

        public void _register(RegisterEvent event) {
            if(!event.getRegistryKey().equals(this.KEY)) {
                return;
            }
            forEach((location, entry) -> {
                event.register(this.KEY, location, entry);
            });
        }
    }
}
