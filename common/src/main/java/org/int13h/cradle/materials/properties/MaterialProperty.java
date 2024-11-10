package org.int13h.cradle.materials.properties;

@FunctionalInterface
public interface MaterialProperty<T> {
    void verify(MaterialProperties properties);
}
