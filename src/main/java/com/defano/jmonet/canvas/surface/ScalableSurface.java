package com.defano.jmonet.canvas.surface;

import io.reactivex.Observable;

import java.awt.*;

/**
 * An surface that has an observable scale factor associated with it.
 */
public interface ScalableSurface {

    /**
     * Gets the displayed scale factor of this image.
     *
     * @return The displayed scale factor of this image.
     */
    double getScale();

    /**
     * Sets the scale factor of the canvas. Values greater than 1 result in the canvas image appearing enlarged; values
     * less than 1 result in the canvas image appearing shrunken. When enlarging the canvas (scale greater than 1), only
     * whole values are allowed; the fractional portion of the argument will be rounded to the nearest whole integer.
     * <p>
     * Changes to scale attempt to zoom-in or zoom-out on the area currently centered in the viewport (when the surface
     * is embedded in a {@link javax.swing.JScrollPane} and managed with a {@link SurfaceScrollController}).
     *
     * @param scale The scale factor
     */
    void setScale(double scale);

    /**
     * Gets an observable provider of the current scale factor.
     *
     * @return An observable scale factor.
     */
    Observable<Double> getScaleObservable();

    /**
     * Scales a given dimension by the current scale factor.
     *
     * @param d The dimension to scale
     * @return A new dimension whose height and width have been multiplied by the scale factor.
     */
    default Dimension scaleDimension(Dimension d) {
        return new Dimension((int) (d.width * getScale()), (int) (d.height * getScale()));
    }

    /**
     * Scales a point by the current scale factor.
     *
     * @param p The point to scale
     * @return A new point whose x and y coordinate has been multiplied by the scale factor.
     */
    default Point scalePoint(Point p) {
        return new Point(
                (int) (p.x * getScale()),
                (int) (p.y * getScale())
        );
    }

    /**
     * Scales a rectangle by the current scale factor.
     *
     * @param r The rectangle to scale
     * @return A new rectangle whose location and dimension has been multiplied by the current scale factor.
     */
    default Rectangle scaleRectangle(Rectangle r) {
        return new Rectangle(
                (int) (r.x * getScale()),
                (int) (r.y * getScale()),
                (int) (r.width * getScale()),
                (int) (r.height * getScale())
        );
    }

    /**
     * Converts a point on the visible surface (view) to the equivalent point within the surface's image (model),
     * taking into account scale, grid and scroll pane complications as appropriate.
     * <p>
     * When a scale factor has been applied, the input coordinates are divided by the scale factor. When a grid
     * factor has been applied, the result is rounded to the nearest grid spacing factor. When the surface is being
     * scrolled and a scale greater than or equal to 1.0 is applied, then the scroll error is factored in (see
     * {@link ScrollableSurface#getScrollError()} for details).
     * <p>
     * See {@link #convertModelPointToView(Point)} for the reverse function.
     *
     * @param p A point in scaled, surface space
     * @return The equivalent point in terms of the canvas image.
     */
    Point convertViewPointToModel(Point p);

    /**
     * Converts a point within the surface's image (model) to an equivalent point in the surface component's coordinate
     * space, accounting for scale and scroll error as appropriate.
     * <p>
     * Note that there is no concept of snap-to-grid when converting from model to view (only when converting in the
     * opposite direction).
     * <p>
     * See {@link #convertViewPointToModel(Point)} for the reverse function.
     *
     * @param p A point within the canvas' image bounds
     * @return The equivalent point in the coordinate space of the surface component
     */
    Point convertModelPointToView(Point p);

}
