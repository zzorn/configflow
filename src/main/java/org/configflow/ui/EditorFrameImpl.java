package org.configflow.ui;

import org.configflow.Configurator;
import org.configflow.Prop;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 *
 */
public class EditorFrameImpl implements EditorFrame {
    private final JFrame frame;
    private Object editedObj;
    private final Configurator configurator;

    private ClassEditorUi editorCore;

    private List<EditorListener> listeners = new ArrayList<EditorListener>();
    private final EditorListener listenerDelegate = new EditorListener() {
        @Override public void onEdited(Object confObject, Prop property) {
            for (EditorListener listener : listeners) {
                listener.onEdited(confObject, property);
            }
        }
    };

    public EditorFrameImpl(String title, Configurator configurator) {
        this.configurator = configurator;
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(600, 800));

        editorCore = new ClassEditorUi();
        editorCore.setListener(listenerDelegate);
        frame.setContentPane(editorCore.createUi(new HashMap<String, Object>(), configurator, editedObj));
        frame.pack();
    }

    @Override public void edit(Object conf) {
        if (conf != editedObj) {
            editedObj = conf;
        }
        update();
    }

    @Override public void update() {
        editorCore.onUpdate(configurator, editedObj);

        frame.pack();
    }

    @Override public Object getConf() {
        return editedObj;
    }

    @Override public void open() {
        frame.setVisible(true);
    }

    @Override public void close() {
        frame.setVisible(false);
    }

    @Override public void addListener(EditorListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override public void removeListener(EditorListener listener) {
        listeners.remove(listener);
    }


}
