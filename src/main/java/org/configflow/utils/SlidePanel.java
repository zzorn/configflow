package org.configflow.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static org.flowutils.MathFlow.map;

/**
 * Panel with a slide control component.
 */
public class SlidePanel extends JPanel {
    private static final Color SLIDER_BG_COLOR = new Color(0.5f, 0.5f, 0.5f);
    private static final Color SLIDER_FG_COLOR = new Color(1f, 1f, 1f);
    private static final Color SLIDER_EDGE_COLOR = new Color(0f, 0f, 0f);


    private boolean continuousUpdate;
    private boolean snapToCenter;
    private boolean logarithmic;
    private double value;
    private double maxVal;
    private double minVal;
    private double origoValue;

    private static final int NUM_POLY_POINTS = 4;
    private int[] polyPointsX = new int[NUM_POLY_POINTS];
    private int[] polyPointsY = new int[NUM_POLY_POINTS];

    private ColorCalculator backgroundColorizer;

    private List<SlideListener> listeners = new ArrayList<SlideListener>(3);

    public SlidePanel(double value,
                      double minVal,
                      double origoValue,
                      double maxVal,
                      boolean continuousUpdate,
                      boolean snapToCenter,
                      boolean logarithmic) {
        this.value = value;
        this.origoValue = origoValue;
        this.minVal = minVal;
        this.maxVal = maxVal;
        this.continuousUpdate = continuousUpdate;
        this.snapToCenter = snapToCenter;
        this.logarithmic = logarithmic;

        setPreferredSize(new Dimension(64, 24));

        final int dragButton = MouseEvent.BUTTON1;
        final int dragButtonMask = MouseEvent.BUTTON1_DOWN_MASK;

        final MouseAdapter mouseHandler = new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                if (e.getButton() == dragButton) {
                    updateHandlePos(e.getX(), true, false);
                }
            }

            @Override public void mouseReleased(MouseEvent e) {
                if (e.getButton() == dragButton) {
                    updateHandlePos(e.getX(), false, true);

                    // Snap back if needed
                    if (SlidePanel.this.snapToCenter) {
                        snapToCenter();
                    }
                }
            }

            @Override public void mouseDragged(MouseEvent e) {
                if ((e.getModifiersEx() & (dragButtonMask)) == dragButtonMask) {
                    updateHandlePos(e.getX(), false, false);
                }
            }
        };
        addMouseMotionListener(mouseHandler);
        addMouseListener(mouseHandler);
    }

    private void snapToCenter() {
        value = origoValue;
        repaint();
    }

    private void updateHandlePos(int x, boolean first, boolean last) {
        double relativePos = relativePosAtX(x);
        double newValue = valueAtRelativePos(relativePos);
        if (newValue != value || last || first) {
            value = newValue;

            repaint();

            // Notify listeners of updates if needed
            if (continuousUpdate || first || last) {
                notifyListeners(value, first, last);
            }
        }
    }

    private double relativePosAtX(int xCoord) {
        return 1.0 * xCoord / getWidth();
    }

    private double valueAtRelativePos(double relativePos) {
        // TODO: Apply log

        if (relativePos > 0.5) {
            return map(relativePos, 0.5, 1, origoValue, maxVal);
        }
        else {
            return map(relativePos, 0, 0.5, minVal, origoValue);
        }
    }



    private double relativePosAtValue(double value) {
        // TODO: Apply log

        if (value > origoValue) {
            return map(value, origoValue, maxVal, 0.5, 1);
        }
        else {
            return map(value, minVal, origoValue, 0, 0.5);
        }
    }

    @Override protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        final int h = getHeight();
        final int w = getWidth();

        // Draw background
        if (backgroundColorizer != null) {
            // Colorize background
            for (int x = 0; x < w; x++) {
                final double relativePos = 1.0 * x / w;
                final double sliderValue = valueAtRelativePos(relativePos);
                g2.setColor(backgroundColorizer.colorForValue(sliderValue));
                g2.drawLine(x, 0, x, h);
            }
        }
        else {
            // Solid background
            g2.setColor(SLIDER_BG_COLOR);
            g2.fillRect(0, 0, w, h);
        }

        // TODO: Draw ticks
        // Draw origo
        int zeroX = (int) (relativePosAtValue(origoValue) * w);
        if (zeroX >= 0 && zeroX <= w) {
            g2.setColor(SLIDER_EDGE_COLOR);
            g2.drawLine(zeroX -1, 0, zeroX -1, h);
            g2.drawLine(zeroX + 1, 0, zeroX + 1, h);
            g2.setColor(SLIDER_FG_COLOR);
            g2.drawLine(zeroX , 0, zeroX , h);
        }

        // Draw indicator
        double sliderPos = relativePosAtValue(value);
        int sliderX = (int) (sliderPos * w);
        if (sliderX >= -h && sliderX <= w + h) {
            fillRhomb(g2, sliderX, h / 2, h - 1, h - 1, SLIDER_EDGE_COLOR);
            fillRhomb(g2, sliderX, h / 2, h - 5, h - 5, SLIDER_FG_COLOR);
        }

    }

    private void fillRhomb(Graphics2D g2, int cx, int cy, int w, int h, Color color) {
        int hw = w/2;
        int hh = h/2;
        polyPointsX[0] = cx;
        polyPointsX[1] = cx + hw;
        polyPointsX[2] = cx;
        polyPointsX[3] = cx - hw;
        polyPointsY[0] = cy - hh;
        polyPointsY[1] = cy;
        polyPointsY[2] = cy + hh;
        polyPointsY[3] = cy;
        g2.setColor(color);
        g2.fillPolygon(polyPointsX, polyPointsY, NUM_POLY_POINTS);
    }

    public void addListener(SlideListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(SlideListener listener) {
        listeners.remove(listener);
    }

    public boolean isContinuousUpdate() {
        return continuousUpdate;
    }

    public void setContinuousUpdate(boolean continuousUpdate) {
        this.continuousUpdate = continuousUpdate;
    }

    public boolean isSnapToCenter() {
        return snapToCenter;
    }

    public void setSnapToCenter(boolean snapToCenter) {
        this.snapToCenter = snapToCenter;
    }

    public ColorCalculator getBackgroundColorizer() {
        return backgroundColorizer;
    }

    public void setBackgroundColorizer(ColorCalculator backgroundColorizer) {
        this.backgroundColorizer = backgroundColorizer;
    }

    protected void notifyListeners(double value, boolean first, boolean last) {
        for (SlideListener listener : listeners) {
            listener.onChanged(this, value, first, last);
        }
    }
}
