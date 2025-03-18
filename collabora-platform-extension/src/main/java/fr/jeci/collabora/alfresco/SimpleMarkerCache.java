package fr.jeci.collabora.alfresco;

import org.alfresco.repo.cache.SimpleCache;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleMarkerCache<K extends Serializable, V> implements SimpleCache<K, V> {
    private final ConcurrentHashMap<K, V> cache = new ConcurrentHashMap<>();

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public boolean contains(K key) {
        return cache.containsKey(key);
    }

    @Override
    public Collection<K> getKeys() {
        return cache.keySet();
    }
}