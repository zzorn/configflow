package org.configflow;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Information about a configurable property of some class.
 */
public final class Prop<T> {
    private final String name;
    private final String desc;
    private final Class<T> type;
    private final Method getter;
    private final Method setter;
    private Map<String, Object> metadata = null;

    public Prop(String name, Class<T> type, Method getter, Method setter) {
        this(name, type, getter, setter, null);
    }

    public Prop(String name, Class<T> type, Method getter, Method setter, Map<String, Object> metadata) {
        this.name = name;
        this.type = type;
        this.getter = getter;
        this.setter = setter;
        this.metadata = metadata;

        final Desc descAnnotation = getter.getAnnotation(Desc.class);
        if (descAnnotation != null) {
            desc = descAnnotation.value();
        }
        else {
            desc = null;
        }
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public Class<T> getType() {
        return type;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setValue(Object conf, T value) {
        try {
            setter.invoke(conf, value);
        } catch (Exception e) {
            throw new ConfigurationException("Problem when setting value '"+value+"' to property '"+name+"' of object '"+conf+"': " + e.getMessage(), e);
        }
    }

    public T getValue(Object conf) {
        try {
            return (T) getter.invoke(conf);
        } catch (Exception e) {
            throw new ConfigurationException("Problem when getting value of property '"+name+"' of object '"+conf+"': " + e.getMessage(), e);
        }
    }
}
