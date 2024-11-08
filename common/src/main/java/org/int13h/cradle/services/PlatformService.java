package org.int13h.cradle.services;

public interface PlatformService {

    enum Loader {
        NEOFORGE,
        FABRIC
    }

    String getPlatform();
    Loader getLoader();
}
