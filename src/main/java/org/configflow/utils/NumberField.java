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

        gradient.addColor(  -4,     new Color(0.36078432f, 0.047058824f, 0.5529412f));
        gradient.addColor(  -3,     new Color(0.45490196f, 0.05882353f, 0.7607843f));
        gradient.addColor(  -2,     new Color(0.0627451f, 0.050980393f, 0.827451f));
        gradient.addColor(  -1,     new Color(0.24705882f, 0.60784316f, 0.8509804f));
        gradient.addColor(  -0.2,   new Color(0.06666667f, 0.8627451f, 0.9098039f));
        gradient.addColor(  -0.03,   new Color(0.44313726f, 0.5803922f, 0.6431373f));
        gradient.addColor(   0,     new Color(1.0f, 1.0f, 1.0f));
        gradient.addColor(   0.03,  new Color(0.28627452f, 0.68235296f, 0.5372549f));
        gradient.addColor(   0.2,   new Color(0.11372549f, 0.8156863f, 0.17254902f));
        gradient.addColor(   1,     new Color(0.8039216f, 0.95686275f, 0.13333334f));
        gradient.addColor(   2,     new Color(0.98039216f, 0.9411765f, 0.06666667f));
        gradient.addColor(   3,     new Color(1.0f, 0.7f, 0.0f));
        gradient.addColor(   4,     new Color(1.0f, 0.0f, 0.0f));
        gradient.addColor(   5,     new Color(0.9098039f, 0.17254902f, 0.45490196f));
        gradient.addColor(   6,     new Color(0.9098039f, 0.20392157f, 0.84705883f));
        gradient.addColor(   7,     new Color(0.7921569f, 0.17254902f, 0.87058824f));

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
        double magnitude = value == 0 ? 0 : (value > 0 ? log10(value + 1) : -log10(-value + 1));
        field.setBackground(magnitudeColoring.getColorMixed(magnitude, 0.25, Color.WHITE));
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
