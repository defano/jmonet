package com.defano.jmonet.tools;

import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.StrokedCursorPathTool;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Tool that erases pixels from the canvas by turning them back to fully transparent.
 */
public class EraserTool extends StrokedCursorPathTool {

    public EraserTool() {
        super(PaintToolType.ERASER);
    }

    /** {@inheritDoc} */
    @Override
    public void startPath(Scratch scratch, Stroke stroke, Paint fillPaint, Point initialPoint) {
        getScratch().erase(this, new Line2D.Float(initialPoint, initialPoint), stroke);
    }

    /** {@inheritDoc} */
    @Override
    public void addPoint(Scratch scratch, Stroke stroke, Paint fillPaint, Point lastPoint, Point thisPoint) {
        getScratch().erase(this, new Line2D.Float(lastPoint, thisPoint), stroke);
    }

    @Override
    public void completePath(Scratch scratch, Stroke stroke, Paint fillPaint) {
        // Nothing to do
    }
}
