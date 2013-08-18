package org.configflow.ui;

import org.configflow.Configurator;
import org.configflow.Prop;

import javax.swing.*;

/**
 * Editor for some type.
 */
public interface Editor<T> {

    /**
     * Called once after the editor has been created.
     * @param configurator configurator.
     * @param property the property to edit.
     * @param confObject the object whose property we are editing.
     */
    void init(Configurator configurator, Object confObject, Prop<T> property);

    /**
     * @return editor ui component.
     */
    JComponent getUi();

    /**
     * Should be called if the configuration object has been changed.
     */
    void update();

    /**
     * Can be called when an editor is no longer needed.  Can unregister listeners etc.
     */
    void dispose();

    /**
     * @param listener a listener that is notified about edits, or null.
     */
    void setListener(EditorListener listener);

    T getValue();

    void setValue(T value);
}
