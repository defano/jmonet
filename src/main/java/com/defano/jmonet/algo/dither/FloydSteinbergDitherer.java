package com.defano.jmonet.algo.dither;

/**
 * An implementation of the Floyd-Steinberg dithering algorithm.
 */
public class FloydSteinbergDitherer extends AbstractDitherer {

    /**
     * {@inheritDoc}
     */
    @Override
    public void ditherPixel(int x, int y, double qer, double qeg, double qeb) {
        distributeError(x + 1, y + 0, qer, qeg, qeb, 7.0 / 16.0);

        distributeError(x - 1, y + 1, qer, qeg, qeb, 3.0 / 16.0);
        distributeError(x + 0, y + 1, qer, qeg, qeb, 5.0 / 16.0);
        distributeError(x + 1, y + 1, qer, qeg, qeb, 1.0 / 16.0);
    }
}
