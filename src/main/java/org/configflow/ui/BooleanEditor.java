package org.configflow.ui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * Checkbox
 */
public class BooleanEditor extends EditorBase<Boolean> {

    private JCheckBox checkBox;

    @Override protected JComponent createUi(Map<String, Object> metadata) {
        checkBox = new JCheckBox(getProperty().getName());
        checkBox.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                final boolean selected = checkBox.isSelected();
                if (getValue() != selected) {
                    setValue(selected);
                }
            }
        });
        return checkBox;
    }

    @Override protected void onUpdate() {
        checkBox.setSelected(getValue());
    }
}
