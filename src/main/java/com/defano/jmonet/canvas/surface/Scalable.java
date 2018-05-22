package com.defano.jmonet.canvas.surface;

import io.reactivex.Observable;

import java.awt.*;

/**
 * An object that has an observable scale factor associated with it.
 */
public interface Scalable {

    /**
     * Sets the scale factor of the canvas. Values greater than 1 result in the canvas image appearing enlarged; values
     * less than 1 result in the canvas image appearing shrunken.
     *
     * @param scale The scale factor
     */
    void setScale(double scale);

    /**
     * Gets the displayed scale factor of this image.
     * @return The displayed scale factor of this image.
     */
    double getScale();

    /**
     * Gets an observable provider of the current scale factor.
     * @return An observable scale factor.
     */
    Observable<Double> getScaleObservable();

    /**
     * Scales a given dimension by the current scale factor.
     *
     * @param d The dimension to scale
     * @return A new dimension whose height and width have been multiplied by the scale factor.
     */
    default Dimension getScaledDimension(Dimension d) {
        return new Dimension((int) (d.width * getScale()), (int) (d.height * getScale()));
    }

}
