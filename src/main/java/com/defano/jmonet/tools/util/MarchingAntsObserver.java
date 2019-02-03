package com.defano.jmonet.tools.util;

import java.awt.*;

/**
 * An observer of movement of the ants (i.e., change in dashed line phase)
 */
public interface MarchingAntsObserver {
    /**
     * Called to indicate that ants have "marched" (the dotted line stroke phase has changed) and that observers should
     * re-paint using the stroke provided.
     *
     * @param ants The new paint stroke to re-draw.
     */
    void onAntsMoved(Stroke ants);
}
