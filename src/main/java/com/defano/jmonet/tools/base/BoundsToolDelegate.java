package com.defano.jmonet.tools.base;

import com.defano.jmonet.canvas.Scratch;

import java.awt.*;

public interface BoundsToolDelegate {

    /**
     * Draws the stroke (outline) of a shape described by a rectangular boundary.
     *
     * @param scratch The scratch buffer on which to draw
     * @param stroke The stroke with which to draw
     * @param paint The paint with which to draw
     * @param bounds The bounds of the shape to draw
     * @param isShiftDown True to indicate that the user is holding the shift key; implementers may use this flag to
     *                    constrain the bounds or otherwise modify the tool behavior.
     */
    void strokeBounds(Scratch scratch, Stroke stroke, Paint paint, Rectangle bounds, boolean isShiftDown);

    /**
     * Fills a shape described by a rectangular boundary.
     *
     * @param scratch The scratch buffer on which to draw
     * @param fill The paint with which to fill the shape
     * @param bounds The bounds of the shape to draw
     * @param isShiftDown True to indicate that the user is holding the shift key; implementers may use this flag to
     *                    constrain the bounds or otherwise modify the tool behavior.
     */
    void fillBounds(Scratch scratch, Paint fill, Rectangle bounds, boolean isShiftDown);

}
