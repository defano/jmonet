package com.defano.jmonet.algo.dither;

/**
 * An implementation of the Sierra-Lite dithering algorithm.
 */
@SuppressWarnings("PointlessArithmeticExpression")
public class SierraLiteDitherer extends AbstractDitherer {

    /**
     * {@inheritDoc}
     */
    @Override
    public void ditherPixel(int x, int y, double qer, double qeg, double qeb) {
        distributeError(x + 1, y + 0, qer, qeg, qeb, 2.0 / 4.0);

        distributeError(x - 1, y + 1, qer, qeg, qeb, 1.0 / 4.0);
        distributeError(x + 0, y + 1, qer, qeg, qeb, 1.0 / 4.0);
    }
}
