package org.configflow.ui;

import org.configflow.Prop;

/**
 * Listens to edits.
 */
public interface EditorListener {

    /**
     * Called when the value of the specified property was changed from the editor.
     * @param confObject the object that was edited.
     * @param property the property that was edited.
     */
    void onEdited(Object confObject, Prop property);

}
