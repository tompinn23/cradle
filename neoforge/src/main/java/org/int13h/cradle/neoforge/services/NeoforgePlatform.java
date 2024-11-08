package org.int13h.cradle.neoforge.services;

import net.neoforged.bus.api.IEventBus;
import org.int13h.cradle.services.PlatformService;

public class NeoforgePlatform implements PlatformService {

    @Override
    public String getPlatform() {
        return "Neoforge";
    }

    @Override
    public Loader getLoader() {
        return Loader.NEOFORGE;
    }
}
