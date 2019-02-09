package com.defano.jmonet.tools.builder;

import com.defano.jmonet.tools.brushes.ShapeStroke;

import java.awt.*;

/**
 * A utility for building strokes, both {@link BasicStroke} and {@link ShapeStroke}.
 */
public class StrokeBuilder {

    /**
     * Builds a stroke in which a shape is "stamped" along each point of the stroked path. Note that the stamped shape
     * (the pen) is not automatically rotated to match the angular perpendicular to the path (as occurs with a
     * {@link #withBasicStroke()}.
     *
     * @return The builder.
     */
    public static ShapeStrokeBuilder withShape() {
        return new ShapeStrokeBuilder();
    }

    /**
     * Builds a basic stroke.
     *
     * @return The builder
     */
    public static BasicStrokeBuilder withBasicStroke() {
        return new BasicStrokeBuilder();
    }

}
