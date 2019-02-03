package com.defano.jmonet.model;

import com.defano.jmonet.tools.*;
import com.defano.jmonet.tools.base.Tool;

/**
 * An enumeration of paint tools provided by this library.
 */
public enum PaintToolType {
    ARROW(ArrowTool.class),
    PENCIL(PencilTool.class),
    RECTANGLE(RectangleTool.class),
    ROUND_RECTANGLE(RoundRectangleTool.class),
    OVAL(OvalTool.class),
    PAINTBRUSH(PaintbrushTool.class),
    ERASER(EraserTool.class),
    LINE(LineTool.class),
    POLYGON(PolygonTool.class),
    SHAPE(ShapeTool.class),
    FREEFORM(FreeformShapeTool.class),
    SELECTION(MarqueeTool.class),
    LASSO(LassoTool.class),
    TEXT(TextTool.class),
    FILL(FillTool.class),
    AIRBRUSH(AirbrushTool.class),
    CURVE(CurveTool.class),
    SLANT(SlantTool.class),
    ROTATE(RotateTool.class),
    SCALE(ScaleTool.class),
    MAGNIFIER(MagnifierTool.class),
    PROJECTION(ProjectionTool.class),
    PERSPECTIVE(PerspectiveTool.class),
    RUBBERSHEET(RubberSheetTool.class);

    private final Class<? extends Tool> toolClass;

    PaintToolType(Class<? extends Tool> clazz) {
        this.toolClass = clazz;
    }

    public Class<? extends Tool> getToolClass() {
        return toolClass;
    }

    /**
     * Determines if the given tool type draws closed-path "shapes" (i.e., elements which can be
     * filled with paint.
     *
     * @return True if the tool draws shapes; false otherwise.
     */
    public boolean isShapeTool() {
        return this == RECTANGLE ||
                this == ROUND_RECTANGLE ||
                this == OVAL ||
                this == POLYGON ||
                this == SHAPE ||
                this == FREEFORM;
    }
}
