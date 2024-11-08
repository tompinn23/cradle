package org.int13h.cradle.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.int13h.cradle.registries.management.DeferredRegistry;
import org.int13h.cradle.registries.management.RegistrySupplier;

public class Blocks {
    public static final DeferredRegistry.Blocks BLOCKS = DeferredRegistry.createBlocks("cradle");

    public static final RegistrySupplier<Block> TEST = BLOCKS.register("test2", Block::new, BlockBehaviour.Properties.of());

    public static void init() {
    }
}
