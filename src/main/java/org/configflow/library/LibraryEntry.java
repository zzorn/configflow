package org.configflow.library;

import java.util.ArrayList;
import java.util.List;

/**
 * Entry in a configuration object library.
 */
public final class LibraryEntry {

    private String name;
    private String desc;
    private final List<LibraryEntry> children;
    private Object confObject;

    /**
     * Creates a new configuration object entry.
     * @param name name of object
     * @param desc longer description
     * @param confObject the configuration object
     */
    public LibraryEntry(String name, String desc, Object confObject) {
        this.name = name;
        this.desc = desc;
        this.confObject = confObject;
        children = null;
    }

    /**
     * Creates a new configuration object category.
     * @param name name of the category
     * @param desc description of the category
     */
    public LibraryEntry(String name, String desc) {
        this.name = name;
        this.desc = desc;
        this.children = new ArrayList<LibraryEntry>();
    }

    public boolean hasChildren() {
        return children != null;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public List<LibraryEntry> getChildren() {
        return children;
    }

    public Object getConfObject() {
        return confObject;
    }

    public void addChild(LibraryEntry entry) {
        if (!hasChildren()) throw new IllegalStateException("Can not add a child object '"+entry+"' to a non-category object '"+name+"'");

        children.add(entry);
    }

    public void removeChild(LibraryEntry entry) {
        if (!hasChildren()) throw new IllegalStateException("Can not remove a child object '"+entry+"' from a non-category object '"+name+"'");

        children.remove(entry);
    }
}
