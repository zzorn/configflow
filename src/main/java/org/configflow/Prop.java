package org.configflow;

import java.util.List;
import java.util.Map;

/**
 * Information about a configurable property.
 */
public final class Prop {
    private final String name;
    private final Class type;
    private Map<String, Object> metadata = null;

    public Prop(String name, Class type) {
        this.name = name;
        this.type = type;
    }

    public Prop(String name, Class type, Map<String, Object> metadata) {
        this.name = name;
        this.type = type;
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
}
