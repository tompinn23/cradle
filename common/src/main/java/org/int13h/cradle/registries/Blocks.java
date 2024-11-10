package org.int13h.cradle.registries;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.int13h.cradle.registries.management.DeferredRegistry;
import org.int13h.cradle.registries.management.RegistryHolder;

public class Blocks {
    public static final DeferredRegistry.Blocks BLOCKS = DeferredRegistry.createBlocks("cradle");

    public static final RegistryHolder<Block, Block> TEST = BLOCKS.register("test2", Block::new, BlockBehaviour.Properties.of());

    public static void init() {
    }
}
