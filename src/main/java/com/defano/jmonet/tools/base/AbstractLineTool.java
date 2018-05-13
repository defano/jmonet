package com.defano.jmonet.tools.base;

import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.builder.PaintTool;
import com.defano.jmonet.tools.util.Geometry;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Mouse and keyboard handler for tools defining a line drawn between two points. When the shift key is held down, the
 * defined line is constrained to the nearest 15 degree angle.
 */
public abstract class AbstractLineTool extends PaintTool {

    private Point initialPoint;

    public AbstractLineTool(PaintToolType type) {
        super(type);
        setToolCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    }

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
    protected abstract void drawLine(Scratch scratch, Stroke stroke, Paint paint, int x1, int y1, int x2, int y2);

    /** {@inheritDoc} */
    @Override
    public void mouseMoved(MouseEvent e, Point imageLocation) {
        setToolCursor(getToolCursor());
    }

    /** {@inheritDoc} */
    @Override
    public void mousePressed(MouseEvent e, Point imageLocation) {
        initialPoint = imageLocation;
    }

    /** {@inheritDoc} */
    @Override
    public void mouseDragged(MouseEvent e, Point imageLocation) {
        getScratch().clear();

        Point currentLoc = imageLocation;

        if (e.isShiftDown()) {
            currentLoc = Geometry.line(initialPoint, currentLoc, getConstrainedAngle());
        }

        drawLine(getScratch(), getStroke(), getStrokePaint(), initialPoint.x, initialPoint.y, currentLoc.x, currentLoc.y);
        getCanvas().invalidateCanvas();
    }

    /** {@inheritDoc} */
    @Override
    public void mouseReleased(MouseEvent e, Point imageLocation) {
        getCanvas().commit();
    }
}
