package com.defano.jmonet.tools.builder;

import java.awt.*;
import java.util.ArrayList;

/**
 * Builds {@link BasicStroke} objects.
 */
@SuppressWarnings("unused")
public class BasicStrokeBuilder {

    private float width = 1;
    private int cap = BasicStroke.CAP_ROUND;
    private int join = BasicStroke.JOIN_ROUND;
    private final ArrayList<Float> dash = new ArrayList<>();
    private float dashPhase = 0;
    private float miterLimit = 1;

    /**
     * Use {@link StrokeBuilder#withBasicStroke()} to get an instance of this class.
     */
    BasicStrokeBuilder() {}

    /**
     * Specifies the width, in pixels, of the stroke.
     * @param width The width
     * @return The builder
     */
    public BasicStrokeBuilder ofWidth(float width) {
        this.width = width;
        return this;
    }

    /**
     * Ends unclosed subpaths and dash segments with a round decoration that has a radius equal to half of the
     * width of the pen.
     *
     * @return The builder
     */
    public BasicStrokeBuilder withRoundCap() {
        this.cap = BasicStroke.CAP_ROUND;
        return this;
    }

    /**
     * Ends unclosed subpaths and dash segments with no added decoration.
     *
     * @return The builder
     */
    public BasicStrokeBuilder withButtCap() {
        this.cap = BasicStroke.CAP_BUTT;
        return this;
    }

    /**
     * Ends unclosed subpaths and dash segments with a square projection that extends beyond the end of the segment
     * to a distance equal to half of the line width.
     *
     * @return The builder
     */
    public BasicStrokeBuilder withSquareCap() {
        this.cap = BasicStroke.CAP_SQUARE;
        return this;
    }

    /**
     * Joins path segments by rounding off the corner at a radius of half the line width.
     *
     * @return The builder
     */
    public BasicStrokeBuilder withRoundJoin() {
        this.join = BasicStroke.JOIN_ROUND;
        return this;
    }

    /**
     * Joins path segments by extending their outside edges until they meet.
     *
     * @return The builder
     */
    public BasicStrokeBuilder withMiterJoin() {
        this.join = BasicStroke.JOIN_MITER;
        return this;
    }

    /**
     * Joins path segments by connecting the outer corners of their wide outlines with a straight segment.
     *
     * @return The builder
     */
    public BasicStrokeBuilder withBevelJoin() {
        this.join = BasicStroke.JOIN_BEVEL;
        return this;
    }

    /**
     * Adds a dash pattern element to the stroke.
     *
     * For example, specifying '5' as the argument to this method produces a dashed/dotted line where every 5 pixels
     * are filled, and every 5 pixels are not. Invoking this method subsequent times appends values to the dash
     * pattern. Calling a second time with '2' produces a line where every 5 pixels are filled followed by 2 that
     * are not.
     *
     * @param dashLength The length of the dash pattern element to append
     * @return The Builder
     */
    public BasicStrokeBuilder withDash(float dashLength) {
        this.dash.add(dashLength);
        return this;
    }

    /**
     * The limit to trim the miter join; must be greater than or equal to 1.0f.
     *
     * @param miterLimit The miter limit
     * @return The builder
     */
    public BasicStrokeBuilder withMiterLimit(float miterLimit) {
        this.miterLimit = miterLimit;
        return this;
    }

    /**
     * The offset at which to start the dashing pattern.
     *
     * @param dashPhase The dash phase offset
     * @return The builder
     */
    public BasicStrokeBuilder withDashPhase(float dashPhase) {
        this.dashPhase = dashPhase;
        return this;
    }

    /**
     * Creates the {@link BasicStroke} as specified.
     *
     * @return The stroke
     */
    public BasicStroke build() {
        if (dash.isEmpty()) {
            return new BasicStroke(width, cap, join, miterLimit);
        } else {
            float[] dashArray = new float[dash.size()];
            for (int index = 0; index < dash.size(); index++) {
                dashArray[index] = dash.get(index);
            }
            return new BasicStroke(width, cap, join, miterLimit, dashArray, dashPhase);
        }
    }
}
