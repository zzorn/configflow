package org.configflow.ui;

import org.configflow.utils.NumberField;
import org.configflow.utils.NumberFieldListener;

import javax.swing.*;
import java.util.Map;

/**
 *
 */
public class FloatEditor extends EditorBase<Float> implements NumberFieldListener {

    private NumberField numberField;

    @Override protected JComponent createUi(Map<String, Object> metadata) {
        numberField = new NumberField(getValue(), false, true);
        numberField.addListener(this);
        onUpdate();
        return numberField;
    }

    @Override protected void onUpdate() {
        numberField.setValue(getValue());
    }

    @Override public void onChanged(NumberField field, double oldValue, double newValue) {
        setValue((float) numberField.getValue());
    }
}
