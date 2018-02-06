package com.defano.jmonet.algo.dither.quant;

/**
 * Quantizes (reduces) a 24-bit, RGB-encoded color value to a reduced palette where the total number
 * of unique color values cannot not exceed a specified count.
 *
 * This class implements a naive color quantization algorithm that does not optimize the reduced color palette for the
 * given image; it simply produces a new palette with colors evenly distributed in the color space, and maps colors
 * in the input to the nearest color in the reduced palette.
 */
public class ColorReductionQuantizer implements QuantizationFunction {

    private final int channelColorCount;

    /**
     * Creates a {@link ColorReductionQuantizer} that reduces the color palette such that the resulting
     * image has no more than the given number of values in each color channel--red, green, and blue.
     *
     * Note that the specified color depth is on a per-channel basis. The total number of colors
     * in the palette will be the cube of this value. For example, a channelColorCount of 2 produces
     * an image with 8 unique colors--2 unique reds * 2 unique greens * 2 unique blues.
     *
     * @param channelColorCount The number of unique color values in each red, green and blue color
     *                          channel.
     */
    public ColorReductionQuantizer(int channelColorCount) {
        this.channelColorCount = channelColorCount - 1;
    }

    /** {@inheritDoc} */
    @Override
    public double[] quantize(double[] input) {
        double[] reduced = new double[4];

        reduced[0] = Math.round(input[0] * (double) channelColorCount) / (double) channelColorCount;
        reduced[1] = Math.round(input[1] * (double) channelColorCount) / (double) channelColorCount;
        reduced[2] = Math.round(input[2] * (double) channelColorCount) / (double) channelColorCount;
        reduced[3] = input[3];

        return reduced;
    }
}
