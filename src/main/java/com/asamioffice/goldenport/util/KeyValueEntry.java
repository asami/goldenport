package com.asamioffice.goldenport.util;

import java.util.Map;

/**
 * KeyValueEntry
 *
 * @since   Mar. 24, 2006
 * @version Aug. 29, 2008
 * @author  ASAMI, Tomoharu (asami@relaxer.org)
 */
public class KeyValueEntry<K,V> implements Map.Entry<K,V> {
    private K key_;
    private V value_;
    
    public KeyValueEntry(K key) {
        key_ = key;
    }

    public KeyValueEntry(K key, V value) {
        key_ = key;
        value_ = value;
    }

    public K getKey() {
        return key_;
    }

    public V getValue() {
        return value_;
    }

    public V setValue(V value) {
        V old = value_;
        value_ = value;
        return old;
    }
}
