package org.configflow.library;

import org.configflow.Configurator;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Library of various configuration classes, can typically have conf objects added or removed.
 */
public final class Library {

    private final LibraryEntry root;

    public Library(final String name, final String desc) {
        this.root = new LibraryEntry(name, desc);
    }


    /**
     * @return root level of the library
     */
    public LibraryEntry getRoot() {
        return root;
    }

    /**
     * Load a library from the specified input stream.
     * @param configurator configurator to use to load the library.  Should have necessary config classes registered.
     * @return loaded library
     */
    public static Library load(Configurator configurator, InputStream inputStream) {
        return configurator.loadConf(inputStream, Library.class);
    }

    /**
     * Save a library to the specified stream
     * @param configurator configurator to use to save the library.
     */
    public void save(Configurator configurator, OutputStream outputStream) {
        configurator.saveConf(outputStream, this);
    }
}
