package com.defano.jmonet.algo.dither.quant;

/**
 * An object which performs a color quantization (reduction) function.
 */
public interface QuantizationFunction {

    /**
     * Given a color value whose red, green, blue and alpha values (in that order) are represented
     * as double in the range 0.0..1.0, this function performs a quantization of the color, returning
     * a reduced set of red, green, blue and alpha values.
     *
     * @param input A color value where input[0] is the red channel, input[1] is the green channel
     *              input[2] is the blue channel and input[3] in the alpha channel.
     * @return A quantized color where input[0] is the red channel, input[1] is the green channel
     * input[2] is the blue channel and input[3] in the alpha channel
     */
    double[] quantize(double[] input);
}
