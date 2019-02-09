package com.defano.jmonet.transform.dither.quant;

/**
 * Quantizes (reduces) a 24-bit, RGB-encoded color value to a monochrome (black and white) palette
 * where each pixel is either white (0xffffff) or black (0x000000).
 */
public class MonochromaticQuantizer implements QuantizationFunction {

    /**
     * {@inheritDoc}
     */
    @Override
    public double[] quantize(double[] input) {
        double[] reduced = new double[4];
        double luminosity = (input[0] + input[1] + input[2]) / 3.0;

        reduced[0] = luminosity > .5 ? 1.0 : 0.0;
        reduced[1] = luminosity > .5 ? 1.0 : 0.0;
        reduced[2] = luminosity > .5 ? 1.0 : 0.0;
        reduced[3] = input[3];          // No change to alpha channel

        return reduced;
    }
}
