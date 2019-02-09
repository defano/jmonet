package com.defano.jmonet.canvas.surface;

/**
 * A surface that supports a snap-to-grid property.
 */
@SuppressWarnings("unused")
public interface GridSurface {

    /**
     * Sets a grid spacing on which the mouse coordinates provided to the paint tools will be "snapped to".
     *
     * @param grid The grid spacing
     */
    void setGridSpacing(int grid);

    /**
     * Gets the grid spacing property.
     *
     * @return The grid spacing
     */
    int getGridSpacing();

}
