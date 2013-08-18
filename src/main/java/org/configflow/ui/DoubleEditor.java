package org.configflow.ui;

import org.configflow.utils.NumberField;
import org.configflow.utils.NumberFieldListener;

import javax.swing.*;
import java.util.Map;

/**
 *
 */
public class DoubleEditor extends EditorBase<Double> implements NumberFieldListener {

    private NumberField numberField;

    @Override protected JComponent createUi(Map<String, Object> metadata) {
        numberField = new NumberField(getValue());
        numberField.addListener(this);
        onUpdate();
        return numberField;
    }

    @Override protected void onUpdate() {
        numberField.setValue(getValue());
    }

    @Override public void onChanged(NumberField field, double oldValue, double newValue) {
        setValue(numberField.getValue());
    }
}
