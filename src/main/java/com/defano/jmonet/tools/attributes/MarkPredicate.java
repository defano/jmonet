package com.defano.jmonet.tools.attributes;

import java.awt.*;

/**
 * An interface for determining if a given pixel constitutes a "mark" on a canvas. Used, for example, when determining
 * if the pencil tool should draw or erase (when starting on a marked pixel the pencil erases, when starting on an
 * unmarked or blank pixel, it draws).
 */
public interface MarkPredicate {

    /**
     * Determines if a pixel of a given color is considered to be a mark on the canvas in contrast to an "erase color"
     * that may optionally define the color that erased pixels are changed to.
     *
     * @param pixel A non-null value, indicating the present color value of the pixel being queried.
     * @param eraseColor An nullable value, indicating the present erase color in the context of this query.
     * @return True if this pixel should be treated as a mark; false otherwise.
     */
    default boolean isMarked(Color pixel, Color eraseColor) {
        if (eraseColor == null) {
            return pixel.getAlpha() >= 128;
        } else {
            return eraseColor.getRed() != pixel.getRed() || eraseColor.getBlue() != pixel.getBlue() || eraseColor.getGreen() != pixel.getGreen();
        }
    }
}
