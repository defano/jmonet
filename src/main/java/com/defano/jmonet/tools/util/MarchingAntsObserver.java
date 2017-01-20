package com.defano.jmonet.tools.util;

/**
 * An observer of movement of the ants (i.e., change in dashed line phase)
 */
public interface MarchingAntsObserver {
    void onAntsMoved();
}
