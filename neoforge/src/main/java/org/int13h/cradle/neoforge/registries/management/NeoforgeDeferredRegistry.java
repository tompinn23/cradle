package org.int13h.cradle.neoforge.registries.management;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.int13h.cradle.neoforge.CradleMod;
import org.int13h.cradle.registries.management.DeferredRegistry;
import org.int13h.cradle.registries.management.RegistryHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;
import java.util.function.Supplier;

public class NeoforgeDeferredRegistry<T> extends DeferredRegistry<T> {


    public NeoforgeDeferredRegistry(String ID, ResourceKey<? extends Registry<T>> KEY) {
        super(ID, KEY);
        CradleMod.getModEventBus().addListener(this::_register);
    }

    public void _register(RegisterEvent event) {
        if (!event.getRegistryKey().equals(this.KEY)) {
            return;
        }
        for (var e : entries.entrySet()) {
            event.register(this.KEY, e.getKey().getId(), () -> e.getValue().get());
            e.getKey().bind(false);
        }
    }

    public static class Blocks extends DeferredRegistry.Blocks {

        private static final Logger LOG = LoggerFactory.getLogger(NeoforgeDeferredRegistry.Blocks.class);


        public Blocks(String ID) {
            super(ID);
            CradleMod.getModEventBus().addListener(this::_register);
        }

        @Override
        public <I extends Block> RegistryHolder<Block, I> register(String name, Supplier<? extends I> value) {
            LOG.warn("Using register directly will cause error: BlockBehaviour.properties requires setId() to be called.");
            return super.register(name, value);
        }

        @Override
        protected <I extends Block> RegistryHolder<Block, I> internalRegister(String name, Function<ResourceLocation, ? extends I> value) {
            LOG.warn("Using register directly will cause error: BlockBehaviour.properties requires setId() to be called.");
            return super.internalRegister(name, value);
        }

        @Override
        public <I extends Block> RegistryHolder<Block, I> register(String name, Function<BlockBehaviour.Properties, ? extends I> fact, BlockBehaviour.Properties props) {
            return super.register(name, () -> fact.apply(props.setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(ID, name)))));
        }

        public void _register(RegisterEvent event) {
            if (!event.getRegistryKey().equals(this.KEY)) {
                return;
            }
            for (var e : entries.entrySet()) {
                event.register(this.KEY, e.getKey().getId(), () -> e.getValue().get());
                e.getKey().bind(false);
            }
        }
    }

    public static class Items extends DeferredRegistry.Items {

        private static final Logger LOG = LoggerFactory.getLogger(NeoforgeDeferredRegistry.Items.class);


        public Items(String ID) {
            super(ID);
            CradleMod.getModEventBus().addListener(this::_register);
        }

        @Override
        public <I extends Item> RegistryHolder<Item, I> register(String name, Supplier<? extends I> value) {
            LOG.warn("Using register directly will cause error: Item.Properties requires setId() to be called.");
            return super.register(name, value);
        }

        @Override
        protected <I extends Item> RegistryHolder<Item, I> internalRegister(String name, Function<ResourceLocation, ? extends I> value) {
            LOG.warn("Using register directly will cause error: Item.Properties requires setId() to be called.");
            return super.internalRegister(name, value);
        }

        @Override
        public <I extends Item> RegistryHolder<Item, I> register(String name, Function<Item.Properties, ? extends I> fact, Item.Properties props) {
            return super.register(name, () -> fact.apply(props.setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(ID, name)))));
        }

        public void _register(RegisterEvent event) {
            if (!event.getRegistryKey().equals(this.KEY)) {
                return;
            }
            for (var e : entries.entrySet()) {
                event.register(this.KEY, e.getKey().getId(), () -> e.getValue().get());
                e.getKey().bind(false);
            }
        }
    }
}
