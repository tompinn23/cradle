package org.int13h.cradle.fabric;

import net.fabricmc.api.ModInitializer;
import org.int13h.cradle.Cradle;

public class CradleFabric implements ModInitializer {


    @Override
    public void onInitialize() {
        Cradle.init();
    }
}
