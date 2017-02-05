package com.defano.jmonet.tools;

import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.AbstractLineTool;

import java.awt.*;

/**
 * Tool that draws straight lines on the canvas.
 */
public class LineTool extends AbstractLineTool {

    public LineTool() {
        super(PaintToolType.LINE);
    }

    @Override
    protected void drawLine(Graphics2D g, Stroke stroke, Paint paint, int x1, int y1, int x2, int y2) {
        g.setPaint(paint);
        g.setStroke(stroke);
        g.drawLine(x1, y1, x2, y2);
    }
}
