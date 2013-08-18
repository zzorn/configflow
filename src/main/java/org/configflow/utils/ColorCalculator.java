package org.configflow.utils;

import java.awt.*;

/**
 *
 */
public interface ColorCalculator {

    Color colorForValue(double value);

    int colorCodeForValue(double value);
}
