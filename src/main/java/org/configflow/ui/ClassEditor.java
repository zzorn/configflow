package org.configflow.ui;

import javax.swing.*;
import java.util.Map;

/**
 *
 */
public class ClassEditor extends EditorBase<Object> {

    private ClassEditorUi editorCore;

    @Override protected JComponent createUi(Map<String, Object> metadata) {
        editorCore = new ClassEditorUi();
        return editorCore.createUi(metadata, getConfigurator(), getValue());
    }

    @Override protected void onDispose() {
        editorCore.onDispose();
    }

    @Override protected void onUpdate() {
        editorCore.onUpdate(getConfigurator(), getValue());
    }

    @Override public void setListener(EditorListener listener) {
        super.setListener(listener);
        editorCore.setListener(listener);
    }

}
