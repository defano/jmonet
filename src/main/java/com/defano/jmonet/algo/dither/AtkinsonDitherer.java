package com.defano.jmonet.algo.dither;

/**
 * An implementation of Bill Atkinson's dithering algorithm.
 */
public class AtkinsonDitherer extends AbstractDitherer {

    /**
     * {@inheritDoc}
     */
    @Override
    public void ditherPixel(int x, int y, double qer, double qeg, double qeb) {
        distributeError(x + 1, y + 0, qer, qeg, qeb, 1.0 / 8.0);
        distributeError(x + 2, y + 0, qer, qeg, qeb, 1.0 / 8.0);

        distributeError(x - 1, y + 1, qer, qeg, qeb, 1.0 / 8.0);
        distributeError(x + 0, y + 1, qer, qeg, qeb, 1.0 / 8.0);
        distributeError(x + 1, y + 1, qer, qeg, qeb, 1.0 / 8.0);

        distributeError(x + 0, y + 1, qer, qeg, qeb, 1.0 / 8.0);
    }
}
