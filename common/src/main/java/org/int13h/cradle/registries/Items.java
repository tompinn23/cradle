package org.int13h.cradle.registries;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import org.int13h.cradle.Constants;
import org.int13h.cradle.registries.management.DeferredRegistry;
import org.int13h.cradle.registries.management.RegistryHolder;

public class Items {
    public static final DeferredRegistry.Items ITEMS = DeferredRegistry.createItems(Constants.MODID);

    public static final RegistryHolder<Item, BlockItem> TEST = ITEMS.registerBlockItem(Blocks.TEST, new Item.Properties());


    public static void init() {

    }
}
