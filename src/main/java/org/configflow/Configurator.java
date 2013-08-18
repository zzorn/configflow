package org.configflow;


import org.configflow.ui.EditorFrame;
import org.configflow.ui.Editor;
import org.configflow.ui.EditorListener;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;

/**
 * Used to work with config objects.
 */
public interface Configurator {

    /**
     * Register a permitted configuration object type.
     * Only registered types can be edited or loaded/saved.
     */
    void registerType(Class configurationObjectType);

    /**
     * Register permitted configuration object types.
     * Only registered types can be edited or loaded/saved.
     */
    void registerTypes(Class ... configurationObjectType);

    /**
     * @return name, type and any metadata (configuration annotations) for each property.
     */
    Collection<Prop> getProperties(Object conf);

    /**
     * @return names of the properties in the specified object.
     */
    Collection<String> getPropertyNames(Object conf);

    /**
     * Set the value of a property of a configuration object.
     */
    void setProperty(Object conf, String name, Object value);

    /**
     * @return the value of the specified property of the specified configuration object.
     */
    Object getProperty(Object conf, String name);

    /**
     * @return a deep copy of the specified configuration object.
     */
    <T> T deepCopy(T conf);

    /**
     * Loads a configuration object from a stream.
     * @param inputStream stream to load object from
     * @param expectedType load fails if the loaded object is not of this type.
     * @return loaded object.
     * @throws ConfigurationException if the configuration could not be loaded.
     */
    <T> T loadConf(InputStream inputStream, Class<T> expectedType);

    /**
     * Saves a configuration to a stream.
     * @param outputStream stream to save object to
     * @param conf object to save
     * @throws ConfigurationException if the configuration could not be saved.
     */
    void saveConf(OutputStream outputStream, Object conf);

    /**
     * Creates and opens editor for the specified configuration object.
     *
     * @param conf root config object to open.
     * @param listener a listener that will be notified about edits, or null.
     * @return a reference to the editor is returned.
     */
    EditorFrame openEditor(Object conf, EditorListener listener);

    /**
     * @return new editor that can be used to edit the specified property of the specified conf object.
     */
    <T> Editor<T> createEditor(Object confObject, Prop<T> property);

    /**
     * Creates a deep copy of the source object and puts it in the copy buffer.
     */
    void copyToClipboard(Object source);

    /**
     * @return a new deep copy of the current copy buffer.
     */
    Object copyFromClipboard();

}
