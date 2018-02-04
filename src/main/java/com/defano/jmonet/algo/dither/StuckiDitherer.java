package com.defano.jmonet.algo.dither;

/**
 * An implementation of the Stucki dithering algorithm.
 */
public class StuckiDitherer extends AbstractDitherer {

    /** {@inheritDoc} */
    @Override
    public void ditherPixel(int x, int y, double qer, double qeg, double qeb) {
        distributeError(x+1, y+0, qer, qeg, qeb, 8.0/42.0);
        distributeError(x+2, y+0, qer, qeg, qeb, 4.0/42.0);

        distributeError(x-2, y+1, qer, qeg, qeb, 2.0/42.0);
        distributeError(x-1, y+1, qer, qeg, qeb, 4.0/42.0);
        distributeError(x+0, y+1, qer, qeg, qeb, 8.0/42.0);
        distributeError(x+1, y+1, qer, qeg, qeb, 4.0/42.0);
        distributeError(x+2, y+1, qer, qeg, qeb, 2.0/42.0);

        distributeError(x-2, y+2, qer, qeg, qeb, 1.0/42.0);
        distributeError(x-1, y+2, qer, qeg, qeb, 2.0/42.0);
        distributeError(x+0, y+2, qer, qeg, qeb, 4.0/42.0);
        distributeError(x+1, y+2, qer, qeg, qeb, 2.0/42.0);
        distributeError(x+2, y+2, qer, qeg, qeb, 1.0/42.0);
    }
}