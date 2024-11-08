package org.int13h.cradle.neoforge;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.int13h.cradle.Constants;
import org.int13h.cradle.Cradle;

@Mod(Constants.MODID)
public class CradleMod {

//    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, Constants.MODID);
//
//    public static final DeferredHolder<Block, Block> TEST = BLOCKS.register("test", () -> new Block(BlockBehaviour.Properties.of()));

    private static IEventBus MOD_BUS = null;

    public static IEventBus getModEventBus() {
        if(MOD_BUS == null) {
            throw new IllegalStateException("Mod Bus has not been initialized");
        }
        return MOD_BUS;
    }

    public CradleMod(IEventBus bus) {
        MOD_BUS = bus;
        Cradle.LOG.info("Hello NeoForge!");
        Cradle.init();
     //   BLOCKS.register(bus);
    }
}
