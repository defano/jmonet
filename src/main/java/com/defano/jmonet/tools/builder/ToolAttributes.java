package com.defano.jmonet.tools.builder;

import com.defano.jmonet.algo.fill.BoundaryFunction;
import com.defano.jmonet.algo.fill.FillFunction;
import com.defano.jmonet.model.Interpolation;
import com.defano.jmonet.tools.base.MarkPredicate;

import java.awt.*;
import java.util.Optional;

public interface ToolAttributes extends ObservableToolAttributes {

    int MIN_SHAPE_SIDES = 3;
    int MAX_SHAPE_SIDES = 20;

    /**
     * Gets the stroke (the pen or brush outline) drawn by the tool.
     * @return The active stroke.
     */
    default Stroke getStroke() {
        return getStrokeObservable().blockingFirst();
    }

    /**
     * Gets the paint used to stroke or outline the item drawn by the tool.
     * @return The stroke paint.
     */
    default Paint getStrokePaint() {
        return getStrokePaintObservable().blockingFirst();
    }

    /**
     * Gets the paint used to fill the item drawn by the tool.
     * @return The fill paint.
     */
    default Optional<Paint> getFillPaint() {
        return getFillPaintObservable().blockingFirst();
    }

    /**
     * Gets the text font drawn by the tool.
     * @return The text font.
     */
    default Font getFont() {
        return getFontObservable().blockingFirst();
    }

    /**
     * Gets the color of the text drawn by the tool.
     * @return The text font color.
     */
    default Color getFontColor() {
        return getFontColorObservable().blockingFirst();
    }

    /**
     * Gets the number of sides of the shape drawn by this tool.
     * @return The number of shape sides.
     */
    default int getShapeSides() {
        int sides = getShapeSidesObservable().blockingFirst();

        return sides < MIN_SHAPE_SIDES ? MIN_SHAPE_SIDES :
                sides > MAX_SHAPE_SIDES ? MAX_SHAPE_SIDES :
                        sides;
    }

    /**
     * Gets the color of the pixel that erased pixels are changed to, or null if erased pixels should be fully
     * transparent (default behavior).
     *
     * @return The erase color or null
     */
    default Color getEraseColor() {
        return getEraseColorObservable().blockingFirst().orElse(null);
    }

    /**
     * Gets the angle (in degrees) that the tool should be constrained to when the shift-key is held down.
     * @return The constrained angle, in degrees.
     */
    default int getConstrainedAngle() {
        return getConstrainedAngleObservable().blockingFirst();
    }

    /**
     * Gets the intensity (opacity) of the paint drawn by this tool. A value in the range [0.0, 1.0] where higher values
     * represent greater paint intensity.
     *
     * @return The tool intensity.
     */
    default double getIntensity() {
        return getIntensityObservable().blockingFirst();
    }

    /**
     * Gets the state of draw multiple flag.
     * @return The draw multiple flag.
     */
    default boolean isDrawMultiple() {
        return getDrawMultipleObservable().blockingFirst();
    }

    /**
     * Gets the state of the draw centered flag.
     * @return The draw centered flag.
     */
    default boolean isDrawCentered() {
        return getDrawCenteredObservable().blockingFirst();
    }

    /**
     * Gets the radius, in pixels, of the rounded corners drawn by this tool.
     * @return The corner radius.
     */
    default int getCornerRadius() {
        return getCornerRadiusObservable().blockingFirst();
    }

    /**
     * Gets the anti-aliasing interpolation mode used by this tool.
     * @return The anti-aliasing interpolation mode.
     */
    default Interpolation getAntiAliasing() {
        return getAntiAliasingObservable().blockingFirst();
    }

    /**
     * Gets the predicate function used to determine if a canvas pixel is considered "marked," not blank (for example,
     * used in determining if the pencil tool should mark or erase).
     *
     * @return The current mark predicate function
     */
    MarkPredicate getMarkPredicate();

    /**
     * Sets the predicate function used to determine if a canvas pixel is considered "marked," not blank (for example,
     * used in determining if the pencil tool should mark or erase).
     *
     * @param markPredicate The mark predicate function
     */
    void setMarkPredicate(MarkPredicate markPredicate);

    /**
     * Gets the function used to detect when paint flooding a region has reached a boundary. See
     * BoundaryFunction for details.
     *
     * @return The current boundary function in use.
     */
    BoundaryFunction getBoundaryFunction();

    /**
     * Sets the function used to detect when paint flooding a region has reached a boundary. See
     * BoundaryFunction for details.
     *
     * @param boundaryFunction The boundary function to use.
     */
    void setBoundaryFunction(BoundaryFunction boundaryFunction);

    /**
     * Gets the function used to color the canvas with paint flooding a region. See {@link FillFunction} for details.
     * @return The fill function being used.
     */
    FillFunction getFillFunction();

    /**
     * Sets the function used to color the canvas with paint flooding a region. See {@link FillFunction} for details.
     * @param fillFunction The fill function to use
     */
    void setFillFunction(FillFunction fillFunction);
}
