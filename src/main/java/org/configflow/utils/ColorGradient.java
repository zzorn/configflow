package org.configflow.utils;

import java.awt.*;
import java.util.*;

import static org.flowutils.MathFlow.*;

/**
 * Simple color gradient.
 */
public class ColorGradient implements ColorCalculator {
    private TreeMap<Double, Color> colors = new TreeMap<Double, Color>();

    public void addColor(double value, double r, double g, double b) {
        addColor(value, r, g, b, 1);
    }

    public void addColor(double value, double r, double g, double b, double a) {
        // Clamp
        if (r < 0) r = 0; else if (r > 1) r = 1;
        if (g < 0) g = 0; else if (g > 1) g = 1;
        if (b < 0) b = 0; else if (b > 1) b = 1;
        if (a < 0) a = 0; else if (a > 1) a = 1;

        addColor(value, new Color((float) r, (float) g, (float) b, (float) a));
    }

    public void addColor(double value, Color color) {
        colors.put(value, color);
    }

    public Color getColor(double value) {
        return getColorMixed(value, 1, Color.white);
    }

    public Color getColorMixed(double value, double mixAmount, Color baseColor) {
        final Map.Entry<Double, Color> floor = colors.floorEntry(value);
        final Map.Entry<Double, Color> ceiling = colors.ceilingEntry(value);

        if (floor == null && ceiling == null) return mixColor(mixAmount, baseColor, Color.black);
        else if (floor == null) return mixColor(mixAmount, baseColor, ceiling.getValue());
        else if (ceiling == null) return mixColor(mixAmount, baseColor, floor.getValue());
        else {
            double t = map(value, floor.getKey(), ceiling.getKey(), 0, 1);
            final Color fCol = floor.getValue();
            final Color cCol = ceiling.getValue();
            int r = mixComponent(mixAmount, t, baseColor.getRed(), fCol.getRed(), cCol.getRed());
            int g = mixComponent(mixAmount, t, baseColor.getGreen(), fCol.getGreen(), cCol.getGreen());
            int b = mixComponent(mixAmount, t, baseColor.getBlue(), fCol.getBlue(), cCol.getBlue());
            int a = mixComponent(mixAmount, t, baseColor.getAlpha(), fCol.getAlpha(), cCol.getAlpha());
            return new Color(r, g, b, a);
        }

    }

    private Color mixColor(double t, Color base, Color top) {
        return new Color(
                (int) mix(t, base.getRed(), top.getRed()),
                (int) mix(t, base.getGreen(), top.getGreen()),
                (int) mix(t, base.getBlue(), top.getBlue()),
                (int) mix(t, base.getAlpha(), top.getAlpha())
        );
    }

    private int mixComponent(double colorAmount, double t, final int base, final int floorComp, final int ceilingComp) {
        return (int) mix(colorAmount, base, mix(t, floorComp, ceilingComp));
    }

    @Override public Color colorForValue(double value) {
        return getColor(value);
    }

    @Override public int colorCodeForValue(double value) {
        return getColor(value).getRGB();
    }
}
