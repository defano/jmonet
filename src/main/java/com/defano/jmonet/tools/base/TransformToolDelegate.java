package com.defano.jmonet.tools.base;

import com.defano.jmonet.model.FlexQuadrilateral;

import java.awt.*;

/**
 * A delegate responsible for handling changes made to the transformed selection frame.
 */
public interface TransformToolDelegate {

    /**
     * Invoked to indicate that the user has dragged/moved the top-left handle of the transform quadrilateral to a
     * new position. Transforms the selection frame and bounded pixels accordingly.
     *
     * @param quadrilateral The quadrilateral representing the transform bounds.
     * @param newPosition The new location of the affected drag handle.
     * @param isShiftDown True to indicate user is holding shift down; implementers may optionally use this flag
     *                    to constrain drag movement or apply some other feature of the transform.
     */
    void moveTopLeft(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown);

    /**
     * Invoked to indicate that the user has dragged/moved the top-right handle of the transform quadrilateral to a
     * new position. Transforms the selection frame and bounded pixels accordingly.
     *
     * @param quadrilateral The quadrilateral representing the transform bounds.
     * @param newPosition The new location of the affected drag handle.
     * @param isShiftDown True to indicate user is holding shift down; implementers may optionally use this flag
     *                    to constrain drag movement or apply some other feature of the transform.
     */
    void moveTopRight(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown);

    /**
     * Invoked to indicate that the user has dragged/moved the bottom-left handle of the transform quadrilateral to a
     * new position. Transforms the selection frame and bounded pixels accordingly.
     *
     * @param quadrilateral The quadrilateral representing the transform bounds.
     * @param newPosition The new location of the affected drag handle.
     * @param isShiftDown True to indicate user is holding shift down; implementers may optionally use this flag
     *                    to constrain drag movement or apply some other feature of the transform.
     */
    void moveBottomLeft(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown);

    /**
     * Invoked to indicate that the user has dragged/moved the bottom-right handle of the transform quadrilateral to a
     * new position. Transforms the selection frame and bounded pixels accordingly.
     *
     * @param quadrilateral The quadrilateral representing the transform bounds.
     * @param newPosition The new location of the affected drag handle.
     * @param isShiftDown True to indicate user is holding shift down; implementers may optionally use this flag
     *                    to constrain drag movement or apply some other feature of the transform.
     */
    void moveBottomRight(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown);
}
