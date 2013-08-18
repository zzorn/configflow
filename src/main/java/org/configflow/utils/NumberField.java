package org.configflow.utils;

import org.flowutils.MathFlow;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import static java.lang.Math.*;
import static org.flowutils.MathFlow.Tau;
import static org.flowutils.MathFlow.map;

/**
 * Quick to use number editor.
 */
public class NumberField extends JPanel {

    private static final int MAX_UNDO_LEVELS = 10;

    private static final double TUNE_UP_FACTOR = 3;
    private static final double TUNE_DOWN_FACTOR = TUNE_UP_FACTOR/2;

    private final double min;
    private final double max;
    private final double defaultValue;
    private final boolean logScale;
    private final boolean continuousUpdate;
    private final boolean restrictToInteger;
    private final boolean restrictToFloat;

    private static final Font numberFont = new Font("System", Font.PLAIN, 16);

    private final JTextField field;
    private final SlidePanel slider;
    private List<NumberFieldListener> listeners = null;
    private final Deque<Double> undoStack = new ArrayDeque<Double>(MAX_UNDO_LEVELS);

    private double value;
    private double unmodifiedSlideValue;

    private final static ColorGradient magnitudeColoring = createMagnitudeColoring();

    private static ColorGradient createMagnitudeColoring() {
        final ColorGradient gradient = new ColorGradient();

        gradient.addColor(  -6,     new Color(0.40784314f, 0.0f, 1.0f));
        gradient.addColor(  -5,     new Color(0.36078432f, 0.25490198f, 0.90588236f));
        gradient.addColor(  -4,     new Color(0.44313726f, 0.5137255f, 1.0f));
        gradient.addColor(  -3,     new Color(0.6039216f, 0.69803923f, 1.0f));
        gradient.addColor(  -1.5,     new Color(0.15686275f, 0.8784314f, 0.92156863f));
        gradient.addColor(  -0.8,   new Color(0.69411767f, 0.8745098f, 0.89411765f));
        gradient.addColor(  -0.25,  new Color(0.6117647f, 0.87058824f, 0.8f));
        gradient.addColor(  -0.1,  new Color(0.8784314f, 0.9607843f, 0.8627451f));
        gradient.addColor(   0,     new Color(1.0f, 1.0f, 1.0f));
        gradient.addColor(   0.1,  new Color(0.96f,0.94f, 0.8f));
        gradient.addColor(   0.2,  new Color(0.9f, 0.7f, 0.5f));
        gradient.addColor(   0.5,  new Color(0.79607844f, 0.8784314f, 0.19607843f));
        gradient.addColor(   1,   new Color(0.6745098f, 0.84313726f, 0.16862746f));
        gradient.addColor(   2,     new Color(0.16862746f, 0.8666667f, 0.10980392f));
        gradient.addColor(   3,     new Color(0.7529412f, 0.95686275f, 0.2f));
        gradient.addColor(   4,     new Color(0.98039216f, 0.9411765f, 0.06666667f));
        gradient.addColor(   5,     new Color(1.0f, 0.7f, 0.0f));
        gradient.addColor(   6,     new Color(1.0f, 0.0f, 0.0f));
        gradient.addColor(   7,     new Color(0.9098039f, 0.17254902f, 0.45490196f));
        gradient.addColor(   8,     new Color(0.9098039f, 0.20392157f, 0.84705883f));
        gradient.addColor(   9,     new Color(0.7921569f, 0.17254902f, 0.87058824f));

        return gradient;
    }

    public NumberField(double value) {
        this(value, 1, false, false, true, false, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public NumberField(double value, final boolean restrictToInteger, final boolean restrictToFloat) {
        this(value, 1, restrictToInteger, restrictToFloat, true, false, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public NumberField(double value,
                       double defaultValue,
                       boolean restrictToInteger,
                       boolean restrictToFloat,
                       boolean logScale,
                       boolean continuousUpdate,
                       double min,
                       double max) {
        this.min = min;
        this.max = max;
        this.defaultValue = defaultValue;
        this.value = value;
        this.restrictToInteger = restrictToInteger;
        this.logScale = logScale;
        this.continuousUpdate = continuousUpdate;
        this.restrictToFloat = restrictToFloat;

        slider = createSlider();

        field = createField(continuousUpdate);

        setLayout(new BorderLayout());
        add(field, BorderLayout.NORTH);
        add(slider, BorderLayout.SOUTH);

        repaint();

        updateFieldFromValue();

    }

    private SlidePanel createSlider() {
        SlidePanel slidePanel = new SlidePanel(0, -1, 0, 1, true, true, true);
        slidePanel.addListener(new SlideListener() {
            @Override public void onChanged(SlidePanel slidePanel, double tuneFactor, boolean first, boolean last) {
                // On press down, initialize reference value
                if (first) unmodifiedSlideValue = value;

                // Keep track of old value for possible listener update
                double oldValue = continuousUpdate ? value : unmodifiedSlideValue;

                // Apply tuning factor
                double factor = tuneFactor > 0 == unmodifiedSlideValue > 0 ? tuneFactor*TUNE_UP_FACTOR : tuneFactor*TUNE_DOWN_FACTOR;
                value = unmodifiedSlideValue + abs(unmodifiedSlideValue) * factor;

                // Update field value
                updateFieldFromValue();

                // Notify listeners etc
                if (last || continuousUpdate) {
                    changeValueFromUi(oldValue, value);
                }

                repaint();
            }
        });

        return slidePanel;
    }

    private JTextField createField(boolean continuousUpdate) {
        JTextField field = new JTextField();
        field.setFont(numberFont);

        // Listen to when editing ready
        field.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    changeValueFromField();
                    updateFieldFromValue();
                }
            }
        });
        field.addFocusListener(new FocusAdapter() {
            @Override public void focusLost(FocusEvent e) {
                changeValueFromField();
                updateFieldFromValue();
            }
        });

        // Listen to changes to field
        if (continuousUpdate) {
            field.getDocument().addDocumentListener(new DocumentListener() {
                @Override public void insertUpdate(DocumentEvent e) {
                    changeValueFromField();
                }

                @Override public void removeUpdate(DocumentEvent e) {
                    changeValueFromField();
                }

                @Override public void changedUpdate(DocumentEvent e) {
                    changeValueFromField();
                }
            });
        }

        return field;
    }

    private void updateFieldFromValue() {
        final double value = getValue();

        String s;
        if (restrictToInteger) {
            s = Integer.toString((int) value);
        }
        else if (restrictToFloat) {
            s = Float.toString((float) value);
        }
        else {
            s = Double.toString(value);
        }

        // Strip decimal point from end if even integer
        if (s.endsWith(".0")) s = s.substring(0, s.length() - 2);

        field.setText(s);

        // Update color based on number scale
        double magnitude = value == 0 ? 0 : (value > 0 ? log(value + 1) : -log(-value + 1));
        field.setBackground(magnitudeColoring.getColorMixed(magnitude, 0.2, Color.WHITE));
        field.repaint();
    }

    public void setValue(double value) {
        undoStack.push(this.value);
        this.value = value;
        repaint();
        // TODO: Release mouse drag
    }

    private void changeValueFromField() {
        final String text = field.getText();
        try {
            double value = Double.parseDouble(text.trim());
            changeValueFromUi(this.value, value);
        }
        catch (NumberFormatException e) {
            // Ignore
        }
    }

    private void changeValueFromUi(double oldValue, double newValue) {
        undoStack.push(value);
        value = newValue;
        if (listeners != null && oldValue != newValue) {
            for (NumberFieldListener listener : listeners) {
                listener.onChanged(this, oldValue, newValue);
            }
        }
    }

    public void addListener(NumberFieldListener listener) {
        if (listeners == null) listeners = new ArrayList<NumberFieldListener>(3);
        listeners.add(listener);
    }

    public void removeListener(NumberFieldListener listener) {
        if (listeners != null) listeners.remove(listener);
    }

    public double getValue() {
        return value;
    }

}
