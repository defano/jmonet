package com.defano.jmonet.algo.dither;

import com.defano.jmonet.algo.dither.quant.QuantizationFunction;

import java.awt.image.BufferedImage;

/**
 * An object that applies a {@link QuantizationFunction} to a {@link BufferedImage} and dithers (diffuses)
 * the resulting quantization error.
 */
public interface Ditherer {

    /**
     * Applies a {@link QuantizationFunction} to each pixel in the source image and dithers (diffuses) the
     * quantization error.
     *
     * @param source    The source image to be quantized and dithered; unmodified by this operation.
     * @param quantizer The quantization function to use.
     * @return A copy of the source image with the quantization/dithering function applied.
     */
    BufferedImage dither(BufferedImage source, QuantizationFunction quantizer);
}
