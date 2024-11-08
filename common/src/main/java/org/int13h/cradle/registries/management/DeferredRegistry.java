package org.int13h.cradle.registries.management;


import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.int13h.cradle.services.PlatformService;
import org.int13h.cradle.services.RegistryService;
import org.int13h.cradle.services.ServiceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredRegistry<T> {
    protected final Object2ObjectMap<ResourceLocation, RegistryObject<T>> registry = new Object2ObjectOpenHashMap<>();
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

    protected DeferredRegistry(String ID, ResourceKey<? extends Registry<T>> KEY) {
        this.ID = ID;
        this.KEY = KEY;
    }

    public RegistrySupplier<T> register(String name, Supplier<T> value) {
        return register(ResourceLocation.tryBuild(ID, name), value);
    }

    public RegistrySupplier<T> register(ResourceLocation location, Supplier<T> value) {
        if(this.frozen)
            throw new IllegalStateException(String.format("Attempted registry of %s after registry %s has been frozen", location, KEY));
        if(this.registry.containsKey(location))
            throw new IllegalStateException(String.format("Attempted double registration of %s in registry", location, KEY));
        var object = new RegistryObject<>(location, value, this);
        this.registry.put(location, object);
        return object;
    }

    public boolean contains(String name) {
        return contains(ResourceLocation.tryBuild(ID, name));
    }

    public boolean contains(ResourceLocation location) {
        return this.registry.containsKey(location);
    }

    public RegistrySupplier<T> get(ResourceLocation location) {
        return this.registry.get(location);
    }

    public void forEach(BiConsumer<ResourceLocation, RegistrySupplier<T>> consumer) {
        this.registry.forEach(consumer);
    }

    public static class Blocks extends DeferredRegistry<Block> {

        private static final Logger LOG = LoggerFactory.getLogger(Blocks.class);

        protected Blocks(String ID) {
            super(ID, Registries.BLOCK);
        }

        @Override
        public RegistrySupplier<Block> register(String name, Supplier<Block> value) {
            if(PLATFORM.getLoader() == PlatformService.Loader.NEOFORGE) {
                LOG.warn("Using register directly will cause error: BlockBehaviour.properties requires setId() to be called.");
            }
            return super.register(name, value);
        }

        @Override
        public RegistrySupplier<Block> register(ResourceLocation location, Supplier<Block> value) {
            if(PLATFORM.getLoader() == PlatformService.Loader.NEOFORGE) {
                LOG.warn("Using register directly will cause error: BlockBehaviour.properties requires setId() to be called.");
            }
            return super.register(location, value);
        }

        public RegistrySupplier<Block> register(String name, Function<BlockBehaviour.Properties, Block> fac, BlockBehaviour.Properties props) {
            throw new IllegalStateException("Should've been replaced");
        }

    }

    public static class Items extends DeferredRegistry<Item> {

        private static final Logger LOG = LoggerFactory.getLogger(Items.class);

        protected Items(String ID) {
            super(ID, Registries.ITEM);
        }

        @Override
        public RegistrySupplier<Item> register(String name, Supplier<Item> value) {
            if(PLATFORM.getLoader() == PlatformService.Loader.NEOFORGE) {
                LOG.warn("Using register directly will cause error: Item.properties requires setId() to be called.");
            }
            return super.register(name, value);
        }

        @Override
        public RegistrySupplier<Item> register(ResourceLocation location, Supplier<Item> value) {
            if(PLATFORM.getLoader() == PlatformService.Loader.NEOFORGE) {
                LOG.warn("Using register directly will cause error: Item.properties requires setId() to be called.");
            }
            return super.register(location, value);
        }

        public RegistrySupplier<Item> register(String name, Function<Item.Properties, Item> fac, Item.Properties props) {
            throw new IllegalStateException("Should've been replaced");
        }

    }

}