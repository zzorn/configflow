package org.configflow;

/**
 * Thrown if there was some problem with the configuration.
 */
public class ConfigurationException extends Error {

    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
