package org.configflow;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Information about a configurable property of some class.
 */
public final class Prop {
    private final String name;
    private final Class type;
    private final Method getter;
    private final Method setter;
    private Map<String, Object> metadata = null;

    public Prop(String name, Class type, Method getter, Method setter) {
        this.name = name;
        this.type = type;
        this.getter = getter;
        this.setter = setter;
    }

    public Prop(String name, Class type, Method getter, Method setter, Map<String, Object> metadata) {
        this.name = name;
        this.type = type;
        this.getter = getter;
        this.setter = setter;
        this.metadata = metadata;
    }

    public String getName() {
        return name;
    }

    public Class getType() {
        return type;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setValue(Object conf, Object value) {
        try {
            setter.invoke(conf, value);
        } catch (Exception e) {
            throw new ConfigurationException("Problem when setting value '"+value+"' to property '"+name+"' of object '"+conf+"': " + e.getMessage(), e);
        }
    }

    public <T> T getValue(Object conf) {
        try {
            return (T) getter.invoke(conf);
        } catch (Exception e) {
            throw new ConfigurationException("Problem when getting value of property '"+name+"' of object '"+conf+"': " + e.getMessage(), e);
        }
    }
}
