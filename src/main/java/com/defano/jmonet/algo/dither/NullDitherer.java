package com.defano.jmonet.algo.dither;

/**
 * A no-op dithering algorithm; provides no dithering whatsoever.
 */
public class NullDitherer extends AbstractDitherer {

    /** {@inheritDoc} */
    @Override
    public void ditherPixel(int x, int y, double qer, double qeg, double qeb) {
        // Nothing to do
    }
}
