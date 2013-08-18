package org.configflow.ui;

import org.configflow.Configurator;
import org.configflow.Prop;
import org.flowutils.Check;

import javax.swing.*;
import java.util.Map;

/**
 *
 */
public abstract class EditorBase<T> implements Editor<T> {

    private JComponent ui = null;
    private Configurator configurator;
    private Prop<T> property;
    private Object confObject;
    private EditorListener listener;

    @Override public final void init(Configurator configurator, Object confObject, Prop<T> property) {
        Check.notNull(configurator, "configurator");
        Check.notNull(confObject, "confObject");
        Check.notNull(property, "property");

        this.confObject = confObject;
        this.configurator = configurator;
        this.property = property;

        onInit(property);
    }

    @Override public final void update() {
        if (configurator == null) throw new IllegalStateException("Editor has not yet been initialized, need to call init before update");

        // Only update if we have an ui
        if (ui != null) {
            onUpdate();
        }
    }

    @Override public final JComponent getUi() {
        if (configurator == null) throw new IllegalStateException("Editor has not yet been initialized, need to call init before getUi");

        if (ui == null) {
            ui = createUi(property.getMetadata());
            ui.setToolTipText(property.getDesc());
        }

        return ui;
    }

    public final void dispose() {
        onDispose();
    }

    @Override public void setListener(EditorListener listener) {
        this.listener = listener;
    }

    protected final EditorListener getListener() {
        return listener;
    }

    protected void onDispose() {
    }

    protected final Prop<T> getProperty() {
        checkInitialized();
        return property;
    }

    protected final Configurator getConfigurator() {
        checkInitialized();
        return configurator;
    }

    protected final T getValue() {
        checkInitialized();
        return property.getValue(confObject);
    }

    protected final void setValue(T value) {
        checkInitialized();
        property.setValue(confObject, value);

        // Notify listener
        notifyEdit();
    }

    /**
     * Notify listeners that the property was changed.
     */
    private void notifyEdit() {
        if (listener != null) {
            listener.onEdited(confObject, property);
        }
    }

    protected abstract JComponent createUi(Map<String, Object> metadata);

    protected void onInit(Prop<T> property) {}

    protected abstract void onUpdate();

    private void checkInitialized() {
        if (property == null) throw new IllegalStateException("Editor not yet initialized, need to call init first");
    }

}
