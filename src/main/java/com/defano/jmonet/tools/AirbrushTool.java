package com.defano.jmonet.tools;

import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.context.GraphicsContext;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.PathToolDelegate;
import com.defano.jmonet.tools.base.StrokedCursorPathTool;
import com.defano.jmonet.tools.util.MathUtils;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * A tool that paints translucent textured paint on the canvas.
 */
public class AirbrushTool extends StrokedCursorPathTool implements PathToolDelegate {

    /**
     * Tool must be constructed via {@link com.defano.jmonet.tools.builder.PaintToolBuilder} to handle dependency
     * injection.
     */
    AirbrushTool() {
        super(PaintToolType.AIRBRUSH);
        setDelegate(this);
    }

    /** {@inheritDoc} */
    @Override
    public void startPath(Scratch scratch, Stroke stroke, Paint strokePaint, Point initialPoint) {
        // Nothing to do
    }

    /** {@inheritDoc} */
    @Override
    public void addPoint(Scratch scratch, Stroke stroke, Paint strokePaint, Point lastPoint, Point thisPoint) {
        Line2D line = new Line2D.Float(lastPoint, thisPoint);

        GraphicsContext g = scratch.getAddScratchGraphics(this, stroke, line);
        g.setStroke(stroke);
        g.setPaint(strokePaint);

        if (getAttributes().isPathInterpolated()) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) getAttributes().getIntensity() / 10.0f));
            for (Point p : MathUtils.linearInterpolation(lastPoint, thisPoint, 1)) {
                g.draw(new Line2D.Float(p, p));
            }
        } else {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) getAttributes().getIntensity()));
            g.draw(line);
        }

    }

    @Override
    public void completePath(Scratch scratch, Stroke stroke, Paint strokePaint, Paint fillPaint) {
        // Nothing to do
    }

}
