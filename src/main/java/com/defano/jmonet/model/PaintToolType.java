package com.defano.jmonet.model;

/**
 * An enumeration of paint tools.
 */
public enum PaintToolType {
    ARROW,
    PENCIL,
    RECTANGLE,
    ROUND_RECTANGLE,
    OVAL,
    PAINTBRUSH,
    ERASER,
    LINE,
    POLYGON,
    SHAPE,
    FREEFORM,
    SELECTION,
    LASSO,
    TEXT,
    FILL,
    AIRBRUSH,
    CURVE,
    SLANT,
    ROTATE,
    SCALE,
    MAGNIFIER;

    /**
     * Determines if the given tool type draws closed-path "shapes" (i.e., elements which can be
     * filled with paint.
     *
     * @return True if the tool draws shapes; false otherwise.
     */
    public boolean isShapeTool() {
        return  this == RECTANGLE       ||
                this == ROUND_RECTANGLE ||
                this == OVAL            ||
                this == POLYGON         ||
                this == SHAPE           ||
                this == FREEFORM;
    }
}
