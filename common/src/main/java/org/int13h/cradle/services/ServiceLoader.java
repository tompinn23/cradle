package org.int13h.cradle.services;

import com.google.common.base.MoreObjects;
import com.google.common.reflect.ClassPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceLoader.class);

    private static ConcurrentHashMap<Class<?>, Class<?>> implementationCache = new ConcurrentHashMap<>();

    private static final Path DIR = Path.of("META-INF/services");


    @SuppressWarnings("unchecked")
    private static <T> Class<T> findServiceImplementation(Class<T> clazz) throws ClassNotFoundException {
        if (implementationCache.containsKey(clazz)) {
            return (Class<T>) implementationCache.get(clazz);
        }
        InputStream resource = Thread.currentThread().getContextClassLoader().getResourceAsStream(DIR.resolve(clazz.getCanonicalName()).toString());
        if (resource == null) {
            LOGGER.warn("Resource not found: {}", clazz.getCanonicalName());
            return null;
        }
        try {
            var clazzName = new BufferedReader(new InputStreamReader(resource)).readLine().trim();
            var impl = MoreObjects.firstNonNull(
                            Thread.currentThread().getContextClassLoader(),
                            ServiceLoader.class.getClassLoader())
                    .loadClass(clazzName);
            if(clazz.isAssignableFrom(impl)) {
                implementationCache.put(clazz, impl);
                return (Class<T>) impl;
            }
        } catch (Exception e) {
            LOGGER.warn("failed to read service descriptor", e);
            return null;
        }
        return null;
    }


    public static <T> T get(Class<T> clazz) {
        try {
            Class<T> impl = findServiceImplementation(clazz);
            if(impl != null) {
                return impl.getConstructor().newInstance();
            }
        } catch (Exception e) {
            throw new ServiceException(clazz, e);
        }
        throw new ServiceException(clazz);
    }

    public static <T> T get(Class<T> clazz, Object... args) {
        Class<?>[] types = Arrays.stream(args).map(Object::getClass).toArray(Class[]::new);
        try {
            Class<T> impl = findServiceImplementation(clazz);
            if(impl != null) {
                return impl.getConstructor(types).newInstance(args);
            }        } catch (Throwable t) {
            throw new ServiceException(clazz, t);
        }
        throw new ServiceException(clazz);
    }

}
