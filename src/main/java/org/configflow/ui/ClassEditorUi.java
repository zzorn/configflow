package org.configflow.ui;

import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.IRowCreator;
import org.configflow.Configurator;
import org.configflow.Prop;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private JButton createButton(String title, String tooltip, ActionListener handler) {
        final JButton button = new JButton(title);
        button.setToolTipText(tooltip);
        button.addActionListener(handler);
        return button;
    }

    public void onDispose() {
        clear();
    }

    public void onUpdate(final Configurator configurator, Object editedObj) {
        // Remove old
        clear();

        if (editedObj != null) {
            layout = new DesignGridLayout(panel);
            // Add properties
            final Collection<Prop> properties = configurator.getProperties(editedObj);
            for (Prop property : properties) {
                final Editor editor = configurator.createEditor(editedObj, property);
                if (editor != null) {
                    propertyEditors.add(editor);
                    layout.row().grid()
                          .add(makeLabel(property))
                     .addMulti(
                            createButton("C", "Copy this element", new ActionListener() {
                              @Override public void actionPerformed(ActionEvent e) {
                                  configurator.copyToClipboard(editor.getValue());
                              }
                            }),
                            createButton("P", "Overwrite this element with the clipboard", new ActionListener() {
                              @Override public void actionPerformed(ActionEvent e) {
                                  final Object value = configurator.copyFromClipboard();
                                  if (value != null) editor.setValue(value);
                              }
                            }));
                    layout.row().grid().add(editor.getUi());

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

