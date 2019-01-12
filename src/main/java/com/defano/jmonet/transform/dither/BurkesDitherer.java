package com.defano.jmonet.transform.dither;

/**
 * An implementation of the Burkes dithering algorithm.
 */
@SuppressWarnings("PointlessArithmeticExpression")
public class BurkesDitherer extends AbstractDitherer {

    /**
     * {@inheritDoc}
     */
    @Override
    public void ditherPixel(int x, int y, double qer, double qeg, double qeb) {
        distributeError(x + 1, y + 0, qer, qeg, qeb, 8.0 / 32.0);
        distributeError(x + 2, y + 0, qer, qeg, qeb, 4.0 / 32.0);

        distributeError(x - 2, y + 1, qer, qeg, qeb, 2.0 / 32.0);
        distributeError(x - 1, y + 1, qer, qeg, qeb, 4.0 / 32.0);
        distributeError(x + 0, y + 1, qer, qeg, qeb, 8.0 / 32.0);
        distributeError(x + 1, y + 1, qer, qeg, qeb, 4.0 / 32.0);
        distributeError(x + 2, y + 1, qer, qeg, qeb, 2.0 / 32.0);

    }
}
