package com.defano.jmonet.tools.base;

import com.defano.jmonet.canvas.Scratch;

import java.awt.*;

/**
 * A delegate class responsible for rendering shapes drawn by the {@link LinearTool}.
 */
public interface LinearToolDelegate {

    /**
     * Draws a line from (x1, y1) to (x2, y2) on the given graphics context.
     *
     * @param scratch The scratch buffer on which to draw
     * @param stroke The stroke with which to draw
     * @param paint The paint with which to draw
     * @param x1 First x coordinate of the line
     * @param y1 First y coordinate of the line
     * @param x2 Second x coordinate of the line
     * @param y2 Second y coordinate of the line
     */
    void drawLine(Scratch scratch, Stroke stroke, Paint paint, int x1, int y1, int x2, int y2);

}
