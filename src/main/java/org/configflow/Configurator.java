package org.configflow;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

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
    List<Prop> getProperties(Object conf);

    /**
     * @return names of the properties in the specified object.
     */
    List<String> getPropertyNames(Object conf);

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
    Object deepCopy(Object conf);

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

}
