package org.int13h.cradle.materials;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.int13h.cradle.items.GearItem;
import org.int13h.cradle.materials.generation.MaterialFlags;
import org.int13h.cradle.materials.properties.MaterialProperties;
import org.int13h.cradle.materials.properties.MaterialProperty;
import org.int13h.cradle.materials.properties.PropertyKey;
import org.int13h.cradle.registries.management.DeferredRegistry;
import org.int13h.cradle.registries.management.RegistryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Material {

    @NotNull
    @Getter
    private final MaterialInfo materialInfo;

    @NotNull
    @Getter
    private final MaterialProperties properties;

    @NotNull
    private final MaterialFlags flags;


    public <T extends MaterialProperty<T>> boolean hasProperty(PropertyKey<T> key) {
        return properties.has(key);
    }


    @Accessors(chain = true)
    private static class MaterialInfo {
        private final ResourceLocation location;

        @Getter
        @Setter
        private IntList colour = new IntArrayList(List.of(-1, -1));

        @Getter
        @Setter
        private ImmutableList<MaterialStack> componentList;

        @Getter
        @Setter
        private Element element;

        private MaterialInfo(ResourceLocation location) {
            this.location = location;
        }

        private void verify(MaterialProperties p, boolean avgRGB) {
            if(colour.getInt(0) == -1) {
                if(!avgRGB || componentList.isEmpty()) {
                    colour.set(0, 0xFFFFFF);
                } else {
                    long colourTemp = 0;
                    int divisor = 0;
                    for(MaterialStack stack : componentList) {
                        colourTemp + stack.getMaterial().getMaterialARGB() * stack.amount();
                        divisor += stack.amount();
                    }
                    colour.set(0, (int)((colourTemp / divisor)));
                }
            }
        }

        public MaterialInfo setComponents(MaterialStack... components) {
            this.componentList = ImmutableList.copyOf(Arrays.asList(components));
            return this;
        }
    }
}
