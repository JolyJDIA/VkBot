package api.utils.cache;

import java.util.AbstractMap;

@FunctionalInterface
public interface RemovalListener<K, V> {
    void onRemoval(AbstractMap.SimpleImmutableEntry<K, V> var1);
}