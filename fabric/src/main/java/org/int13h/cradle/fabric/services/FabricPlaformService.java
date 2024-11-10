package org.int13h.cradle.fabric.services;

import org.int13h.cradle.services.PlatformService;

public class FabricPlaformService implements PlatformService {
    @Override
    public String getPlatform() {
        return "Fabric";
    }

    @Override
    public Loader getLoader() {
        return Loader.FABRIC;
    }
}
