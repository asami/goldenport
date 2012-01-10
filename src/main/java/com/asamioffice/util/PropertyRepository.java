package com.asamioffice.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * PropertyRepository
 *
 * @since   Jul.  4, 2006
 * @version Jul.  4, 2006
 * @author  ASAMI, Tomoharu (asami@relaxer.org)
 */
public class PropertyRepository {
    private final Object source_;
    private Map<String, Object> properties_ = new HashMap<String, Object>();
    private PropertyChangeSupport propertyChangeSupport_ = null;
    
    public PropertyRepository(Object source) {
        source_ = source;
    }

    public void put(String name, Object value) {
        Object oldValue = properties_.get(name);
        properties_.put(name, value);
        if (propertyChangeSupport_ != null) {
            propertyChangeSupport_.firePropertyChange(name, oldValue, value);
        }
    }

    public void putAll(Map<String, Object> properties) {
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    public Object get(String name) {
        return properties_.get(name);
    }

    public Map<String, Object> getProperties() {
        return Collections.unmodifiableMap(properties_);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (propertyChangeSupport_ == null) {
            propertyChangeSupport_ = new PropertyChangeSupport(source_);
        }
        propertyChangeSupport_.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        if (propertyChangeSupport_ == null) {
            return;
        }
        propertyChangeSupport_.removePropertyChangeListener(listener);
    }
}
