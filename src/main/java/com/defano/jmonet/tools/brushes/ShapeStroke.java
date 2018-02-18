package com.defano.jmonet.tools.brushes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A brush whose tip is a user-provided shape. Produces a stroke where every point on the stroked path is "stamped" with a
 * filled shape.
 */
public class ShapeStroke extends StampStroke {

    private final List<Shape> shapes = new ArrayList<>();

    /**
     * Produces a brush of given shape.
     *
     * @param shape      The shape of the brush
     */
    public ShapeStroke(Shape shape) {
        this.shapes.add(shape);
    }

    public ShapeStroke(Collection<Shape> shapes) {
        this.shapes.addAll(shapes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stampPoint(GeneralPath path, Point point) {
        for (Shape shape : shapes) {
            Shape stamp = AffineTransform
                    .getTranslateInstance(point.x - (shape.getBounds().width / 2) - shape.getBounds().x, point.y - (shape.getBounds().height / 2) - shape.getBounds().y)
                    .createTransformedShape(shape);

            path.append(stamp, false);
        }
    }
}
