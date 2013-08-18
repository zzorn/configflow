package org.configflow.ui;

/**
 * Ui that can edit configuration files.
 */
public interface EditorFrame {

    /**
     * Changes the top level edited object.
     */
    void edit(Object conf);

    /**
     * @return the edited object.
     */
    Object getConf();

    /**
     * Call to update the UI if the config object was changed.
     */
    void update();

    /**
     * Shows the editor UI.
     */
    void open();

    /**
     * Closes the editor UI.
     */
    void close();

    /**
     * @param listener a listener that will be notified about edits.
     */
    void addListener(EditorListener listener);

    /**
     * @param listener listener to remove.
     */
    void removeListener(EditorListener listener);
}
