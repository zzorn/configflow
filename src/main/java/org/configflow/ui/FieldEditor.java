package org.configflow.ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;

/**
 * Generic formatted editor for numbers and strings.
 */
public class FieldEditor<T> extends EditorBase<T> {
    protected JFormattedTextField field;

    @Override protected JComponent createUi(Map<String, Object> metadata) {
        final T value = getValue();
        field = new JFormattedTextField(value);
        final JFormattedTextField.AbstractFormatter formatter = field.getFormatter();
        /*
        if (NumberFormatter.class.isInstance(formatter)) {
            final NumberFormatter numberFormatter = (NumberFormatter) formatter;
            final NumberFormat numberFormat = (NumberFormat) numberFormatter.getFormat();
            numberFormatter.setCommitsOnValidEdit(true);
            numberFormat.setGroupingUsed(false);
            //numberFormat.setMaximumFractionDigits(6);
            numberFormatter.setFormat(numberFormat);
        }
        */

        field.setToolTipText(getProperty().getDesc());
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) {
                updateValueFromEditor();
            }

            @Override public void removeUpdate(DocumentEvent e) {
                updateValueFromEditor();
            }

            @Override public void changedUpdate(DocumentEvent e) {
                updateValueFromEditor();
            }
        });

        return field;
    }

    protected void updateValueFromEditor() {
        setValue((T) field.getValue());
    }

    @Override protected void onUpdate() {
        field.setValue(getValue());
    }

}
