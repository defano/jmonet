package com.defano.jmonet.transform.dither;

/**
 * An implementation of the Jarvis-Judice-Ninke dithering algorithm.
 */
@SuppressWarnings({"PointlessArithmeticExpression", "unused"})
public class JarvisJudiceNinkeDitherer extends AbstractDitherer {

    /**
     * {@inheritDoc}
     */
    @Override
    public void ditherPixel(int x, int y, double qer, double qeg, double qeb) {
        distributeError(x + 1, y + 0, qer, qeg, qeb, 7.0 / 48.0);
        distributeError(x + 2, y + 0, qer, qeg, qeb, 5.0 / 48.0);

        distributeError(x - 2, y + 1, qer, qeg, qeb, 3.0 / 48.0);
        distributeError(x - 1, y + 1, qer, qeg, qeb, 5.0 / 48.0);
        distributeError(x + 0, y + 1, qer, qeg, qeb, 7.0 / 48.0);
        distributeError(x + 1, y + 1, qer, qeg, qeb, 5.0 / 48.0);
        distributeError(x + 2, y + 1, qer, qeg, qeb, 3.0 / 48.0);

        distributeError(x - 2, y + 2, qer, qeg, qeb, 1.0 / 48.0);
        distributeError(x - 1, y + 2, qer, qeg, qeb, 3.0 / 48.0);
        distributeError(x + 0, y + 2, qer, qeg, qeb, 5.0 / 48.0);
        distributeError(x + 1, y + 2, qer, qeg, qeb, 3.0 / 48.0);
        distributeError(x + 2, y + 2, qer, qeg, qeb, 1.0 / 48.0);
    }
}
