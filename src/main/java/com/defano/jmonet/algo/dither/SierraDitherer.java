package com.defano.jmonet.algo.dither;

/**
 * An implementation of the Sierra-3 dithering algorithm.
 */
public class SierraDitherer extends AbstractDitherer {

    /** {@inheritDoc} */
    @Override
    public void ditherPixel(int x, int y, double qer, double qeg, double qeb) {
        distributeError(x+1, y+0, qer, qeg, qeb, 5.0/32.0);
        distributeError(x+2, y+0, qer, qeg, qeb, 3.0/32.0);

        distributeError(x-2, y+1, qer, qeg, qeb, 2.0/32.0);
        distributeError(x-1, y+1, qer, qeg, qeb, 4.0/32.0);
        distributeError(x+0, y+1, qer, qeg, qeb, 5.0/32.0);
        distributeError(x+1, y+1, qer, qeg, qeb, 4.0/32.0);
        distributeError(x+2, y+1, qer, qeg, qeb, 2.0/32.0);

        distributeError(x-1, y+2, qer, qeg, qeb, 2.0/32.0);
        distributeError(x+0, y+2, qer, qeg, qeb, 3.0/32.0);
        distributeError(x+1, y+2, qer, qeg, qeb, 2.0/32.0);
    }
}
