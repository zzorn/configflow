package org.configflow.utils;

import java.awt.*;

/**
 * Mutable subclass of normal AWT color.
 */
// NOTE: Seems slow to use in graphics context for some reason.
public final class MutableColor extends Color {

    private int colorValue;

    public MutableColor() {
        super(0);
        setColor(0,0,0);
    }

    public MutableColor(double r, double g, double b) {
        super(0);
        setColor(r, g, b);
    }

    public MutableColor(double r, double g, double b, double a) {
        super(0);
        setColor(r, g, b, a);
    }

    public void setColor(double r, double g, double b, double a) {
        // Set int color
        setColorFromInt((int)(r*255+0.5),
                        (int)(g*255+0.5),
                        (int)(b*255+0.5),
                        (int)(a*255+0.5));
    }

    public void setColor(double r, double g, double b) {
        setColor(r, g, b, 1);
    }

    public void setColorFromInt(int r, int g, int b) {
        setColorFromInt(r, g, b, 255);
    }

    public void setColorFromInt(int r, int g, int b, int a) {
        // Clamp
        if (r < 0) r = 0; else if (r > 255) r = 255;
        if (g < 0) g = 0; else if (g > 255) g = 255;
        if (b < 0) b = 0; else if (b > 255) b = 255;
        if (a < 0) a = 0; else if (a > 255) a = 255;

        // Calculate color int
        colorValue = ((a & 0xFF) << 24) |
                     ((r & 0xFF) << 16) |
                     ((g & 0xFF) << 8)  |
                     ((b & 0xFF) << 0);
    }

    public double getR() {
        return getRed() / 255.0;
    }

    public double getG() {
        return getGreen() / 255.0;
    }

    public double getB() {
        return getBlue() / 255.0;
    }

    public double getA() {
        return getAlpha() / 255.0;
    }

    public void setR(double r) {
        setColor(r, getG(), getB(), getA());
    }

    public void setG(double g) {
        setColor(getR(), g, getB(), getA());
    }

    public void setB(double b) {
        setColor(getR(), getG(), b, getA());
    }

    public void setA(double a) {
        setColor(getR(), getG(), getB(), a);
    }

    public void setRed(int r) {
        setColorFromInt(r, getGreen(), getBlue(), getAlpha());
    }

    public void setGreen(int g) {
        setColorFromInt(getRed(), g, getBlue(), getAlpha());
    }

    public void setBlue(int b) {
        setColorFromInt(getRed(), getGreen(), b, getAlpha());
    }

    public void setAlpha(int a) {
        setColorFromInt(getRed(), getGreen(), getBlue(), a);
    }

    @Override public int getRGB() {
        return colorValue;
    }

    public void setRGBA(int rgba) {
        colorValue = rgba;
    }

    @Override public int hashCode() {
        return colorValue;
    }
}
