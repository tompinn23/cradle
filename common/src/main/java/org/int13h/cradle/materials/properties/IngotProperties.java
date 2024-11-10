package org.int13h.cradle.materials.properties;

import lombok.Getter;
import lombok.Setter;
import org.int13h.cradle.materials.Material;

public class IngotProperties implements MaterialProperty<IngotProperties> {

    @Getter
    @Setter
    private Material smeltingResult;

    @Override
    public void verify(MaterialProperties properties) {
    }
}
