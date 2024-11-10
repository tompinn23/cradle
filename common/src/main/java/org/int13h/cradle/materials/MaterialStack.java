package org.int13h.cradle.materials;

import java.util.Map;
import java.util.WeakHashMap;

public record MaterialStack(Material material, long amount) {

    private static final Map<String, MaterialStack> PARSE_CACHE = new WeakHashMap<>();

    public MaterialStack copy(long amount) {
        return new MaterialStack(material, amount);
    }

    public MaterialStack copy() {
        return new MaterialStack(material, amount);
    }

    public static MaterialStack fromString(CharSequence str) {
        String trimmed = str.toString().trim();
        String copy = trimmed;

        var cached = PARSE_CACHE.get(copy);
        if (cached != null) {
            return cached.isEmpty() ? null : cached.copy();
        }

        var count = 1;
        var spaceIndex = copy.indexOf(' ');

        if(spaceIndex >= 2 && copy.indexOf('x') == spaceIndex - 1) {
            count = Integer.parseInt(copy.substring(0, spaceIndex - 1));
            copy = copy.substring(spaceIndex + 1);
        }

        //cached = new MaterialStack(, count);
        //PARSE_CACHE
        //return cached.copy();
        return null;
    }

    public boolean isEmpty() {
        return this.material ==
    }
}
