package com.defano.jmonet.algo.dither.quant;

/**
 * Quantizes (reduces) a 24-bit, RGB-encoded color value to a gray-scale palette where the total number
 * of unique grays cannot not exceed a specified count.
 *
 * This class implements a naive quantization algorithm that does not optimize the palette of grays for the
 * given input; it simply produces a new palette with grays evenly distributed in the color space, and maps colors
 * in the input to the nearest gray in the reduced palette.
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
