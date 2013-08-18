package org.configflow.ui;

import net.java.dev.designgridlayout.DesignGridLayout;
import org.configflow.Configurator;
import org.configflow.Prop;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class ClassEditorUi {

    private JPanel panel;
    private List<Editor> propertyEditors = new ArrayList<Editor>();
    private DesignGridLayout layout;
    private EditorListener listener;

    public JComponent createUi(Map<String, Object> metadata, Configurator configurator, Object editedObj) {
        panel = new JPanel();
        onUpdate(configurator, editedObj);
        return panel;
    }

    public void onDispose() {
        clear();
    }

    public void onUpdate(Configurator configurator, Object editedObj) {
        // Remove old
        clear();

        if (editedObj != null) {
            // Add properties
            layout = new DesignGridLayout(panel);
            final Collection<Prop> properties = configurator.getProperties(editedObj);
            for (Prop property : properties) {
                final Editor editor = configurator.createEditor(editedObj, property);
                if (editor != null) {
                    propertyEditors.add(editor);
                    layout.row().grid(makeLabel(property)).add(editor.getUi());
                    editor.setListener(listener);
                } else {
                    layout.row().grid(makeLabel(property));
                }
            }
        }

        panel.repaint();
    }

    public void setListener(EditorListener listener) {
        this.listener = listener;
        for (Editor propertyEditor : propertyEditors) {
            propertyEditor.setListener(listener);
        }
    }


    private JLabel makeLabel(Prop property) {
        final JLabel label = new JLabel(property.getName());
        label.setToolTipText(property.getDesc());
        return label;
    }

    private void clear() {
        panel.removeAll();
        for (Editor propertyEditor : propertyEditors) {
            propertyEditor.dispose();
            propertyEditor.setListener(null);
        }
        propertyEditors.clear();
        layout = null;
    }
}

