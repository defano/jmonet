package com.defano.jmonet.algo.dither.quant;

/**
 * Quantizes (reduces) an 24-bit, RGB-encoded color value to a grayscale palette where the total number
 * of unique grays cannot not exceed a user-specified count.
 *
 * This quantizer provides a trivial, equal distribution of color values. It does not attempt to
 * analyze an image and generate a reduced palette optimized for the color distribution of the
 * source image.
 */
public class GrayscaleQuantizer implements QuantizationFunction {

    private final int graysCount;

    /**
     * Creates a {@link GrayscaleQuantizer} that reduces the color palette to a series of "true" grays
     * not exceeding the specified count.
     *
     * Note that 24-bit RGB color images support only 256 "true" grays (including full white and full
     * black); specifying any value greater than this will have the same effect as 256.
     *
     * @param graysCount The maximum number of unique grays to be present in the output.
     */
    public GrayscaleQuantizer(int graysCount) {
        this.graysCount = graysCount;
    }

    /** {@inheritDoc} */
    @Override
    public double[] quantize(double[] input) {
        double[] reduced = new double[4];

        double luminosity = (input[0] + input[1] + input[2]) / 3.0;
        luminosity = Math.round(luminosity * (double) graysCount) / (double) graysCount;

        reduced[0] = luminosity;
        reduced[1] = luminosity;
        reduced[2] = luminosity;
        reduced[3] = input[3];      // No change to alpha channel

        return reduced;
    }
}
