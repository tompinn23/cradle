package org.int13h.cradle;

import org.int13h.cradle.registries.Blocks;
import org.int13h.cradle.registries.Items;
import org.int13h.cradle.registries.management.DeferredRegistry;
import org.int13h.cradle.services.PlatformService;
import org.int13h.cradle.services.ServiceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cradle {

    public static final Logger LOG = LoggerFactory.getLogger(Cradle.class);


    public static void init() {
        LOG.info("Initializing Cradle");

        LOG.info("Platform: {}", ServiceLoader.get(PlatformService.class).getPlatform());


        Blocks.init();
        Items.init();

        /** make sure all deferred registries have been classloaded */
        DeferredRegistry.register();
    }
}


