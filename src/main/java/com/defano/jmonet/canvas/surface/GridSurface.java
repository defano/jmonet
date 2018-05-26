package com.defano.jmonet.canvas.surface;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * A surface that supports a snap-to-grid property.
 */
public interface GridSurface {

    /**
     * Sets a grid spacing on which the mouse coordinates provided to the paint tools will be "snapped to".
     *
     * @param grid The grid spacing
     */
    void setGridSpacing(int grid);

    /**
     * Gets an observable grid spacing property.
     *
     * @return The grid spacing {@link BehaviorSubject}
     */
    Observable<Integer> getGridSpacingObservable();

}
