package com.defano.jmonet.tools.brushes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

/**
 * A brush whose tip is a user-provided shape. Produces a stroke where every point on the stroked path is "stamped" with a
 * filled shape.
 */
public class ShapeBrush extends InterpolatedStampStroke {

    private final Shape shape;
    private final boolean translated;

    /**
     * Produces a brush of given shape.
     * @param shape The shape of the brush
     * @param translated When true, the shape will be translated half its width left, and half its height up.
     */
    public ShapeBrush(Shape shape, boolean translated) {
        this.shape = shape;
        this.translated = translated;
    }

    /** {@inheritDoc} */
    @Override
    public void stampPoint(GeneralPath path, Point point) {
        if (translated) {
            path.append(AffineTransform
                    .getTranslateInstance(point.x - shape.getBounds().getWidth() / 2.0, point.y + shape.getBounds().getHeight() / 2.0)
                    .createTransformedShape(shape), false);
        } else {
            path.append(AffineTransform
                    .getTranslateInstance(point.x, point.y)
                    .createTransformedShape(shape), false);
        }
    }
}
