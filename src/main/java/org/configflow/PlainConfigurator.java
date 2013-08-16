package org.configflow;

import org.configflow.library.Library;
import org.configflow.library.LibraryEntry;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Configurator implementation that works with plain old java objects.
 */
public class PlainConfigurator implements Configurator {
    public PlainConfigurator() {
        registerType(Library.class);
        registerType(LibraryEntry.class);

        // TODO: Register primitive wrapper types
        // TODO: Register collection types?
    }

    @Override public void registerTypes(Class... configurationObjectType) {
        for (Class type : configurationObjectType) {
            registerType(type);
        }
    }

    @Override public void registerType(Class configurationObjectType) {
        // TODO: Implement

    }

    @Override public List<Prop> getProperties(Object conf) {
        // TODO: Implement
        return null;
    }

    @Override public List<String> getPropertyNames(Object conf) {
        // TODO: Implement
        return new ArrayList<String>();
    }

    @Override public void setProperty(Object conf, String name, Object value) {
        // TODO: Implement

    }

    @Override public Object getProperty(Object conf, String name) {
        // TODO: Implement
        return null;
    }

    @Override public Object deepCopy(Object conf) {
        // TODO: Implement
        return null;
    }

    @Override public <T> T loadConf(InputStream inputStream, Class<T> expectedType) {
        // TODO: Implement
        return null;
    }

    @Override public void saveConf(OutputStream outputStream, Object conf) {
        // TODO: Implement

    }
}
