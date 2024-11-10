package org.int13h.cradle.registries.management;


import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.int13h.cradle.services.PlatformService;
import org.int13h.cradle.services.RegistryService;
import org.int13h.cradle.services.ServiceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredRegistry<T> {
    protected final Map<RegistryHolder<T, ? extends T>, Supplier<? extends T>> entries = new LinkedHashMap<>();
    protected final String ID;
    protected final ResourceKey<? extends Registry<T>> KEY;

    protected static final PlatformService PLATFORM = ServiceLoader.get(PlatformService.class);


    private boolean frozen;

    private static final RegistryService SERVICE = ServiceLoader.get(RegistryService.class);

    public static void register() {
//        try(var resource = DeferredRegistry.class.getResourceAsStream("META-INF/qntm/registries")) {
//            var reader = new BufferedReader(new InputStreamReader(resource));
//            String line;
//            while((line = reader.readLine()) != null) {
//                DeferredRegistry.class.getClassLoader().loadClass(line);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        SERVICE.register();
    }

    public static <T> DeferredRegistry<T> create(final String ID, ResourceKey<? extends Registry<T>> key) {
        return SERVICE.create(ID, key);
    }

    public static DeferredRegistry.Blocks createBlocks(final String ID) {
        return SERVICE.createBlocks(ID);
    }

    public static DeferredRegistry.Items createItems(final String ID) {
        return SERVICE.createItems(ID);
    }

    protected DeferredRegistry(String ID, ResourceKey<? extends Registry<T>> KEY) {
        this.ID = ID;
        this.KEY = KEY;
    }

    public <I extends T> RegistryHolder<T, I> register(String name, Supplier<? extends I> value) {
        return this.internalRegister(name, key -> value.get());
    }

    public <I extends T> RegistryHolder<T, I> register(String name, Function<ResourceLocation, ? extends I> value) {
        return this.internalRegister(name, value);
    }

    protected <I extends T> RegistryHolder<T, I> internalRegister(String name, Function<ResourceLocation, ? extends I> value) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(value);

        final ResourceLocation key = ResourceLocation.tryBuild(ID, name);

        RegistryHolder<T, I> holder = createHolder(this.KEY, key);
        if(entries.putIfAbsent(holder, () -> value.apply(key)) != null) {
            throw new IllegalStateException("Duplicate registry "+ name);
        }

        return holder;
    }

    protected <I extends T> RegistryHolder<T, I> createHolder(ResourceKey<? extends Registry<T>> key, ResourceLocation location) {
        return RegistryHolder.create(key, location);
    }

    public void forEach(BiConsumer<? super RegistryHolder<T, ? extends T>, ? super Supplier<? extends T>> consumer) {
        this.entries.forEach(consumer);
    }

    public static class Blocks extends DeferredRegistry<Block> {

        private static final Logger LOG = LoggerFactory.getLogger(Blocks.class);

        protected Blocks(String ID) {
            super(ID, Registries.BLOCK);
        }

        public <I extends Block> RegistryHolder<Block, I> register(String name, Function<BlockBehaviour.Properties, ? extends I> fact, BlockBehaviour.Properties props) {
            return this.internalRegister(name, key -> fact.apply(props.setId(ResourceKey.create(Registries.BLOCK, key))));
        }

    }

    public static class Items extends DeferredRegistry<Item> {

        private static final Logger LOG = LoggerFactory.getLogger(Items.class);

        protected Items(String ID) {
            super(ID, Registries.ITEM);
        }

        public <I extends Item> RegistryHolder<Item, I> registerSimple(String name, Function<Item.Properties, ? extends I> fact) {
            return this.register(name, fact, new Item.Properties());
        }

        public RegistryHolder<Item, BlockItem> registerBlockItem(Holder<Block> holder, Item.Properties properties) {
            return this.register(holder.unwrapKey().get().location().getPath(), p -> new BlockItem(holder.value(), p), properties);
        }

        public <I extends Item> RegistryHolder<Item, I> register(String name, Function<Item.Properties, ? extends I> fact, Item.Properties properties) {
            return this.internalRegister(name, key -> fact.apply(properties.setId(ResourceKey.create(Registries.ITEM, key))));
        }


    }

}