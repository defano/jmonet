package com.defano.jmonet.transform.image;

import com.defano.jmonet.transform.dither.Ditherer;
import com.defano.jmonet.transform.dither.FloydSteinbergDitherer;
import com.defano.jmonet.transform.dither.quant.GrayscaleQuantizer;
import com.defano.jmonet.transform.dither.quant.MonochromaticQuantizer;

import java.awt.image.BufferedImage;

/**
 * Transform an image to a gray-scale containing no more than the specified number of gray shades.
 */
public class GreyscaleReductionTransform implements StaticImageTransform {

    private final Ditherer ditherer;
    private final int grayDepth;

    /**
     * Creates a greyscale color reduction transform.
     *
     * @param grayDepth The maximum number of unique shades of gray in which to render the given image; zero produces
     *                  a black and white (monochrome) image.
     * @param ditherer  The dithering algorithm to use, for example {@link FloydSteinbergDitherer}.
     */
    public GreyscaleReductionTransform(Ditherer ditherer, int grayDepth) {
        this.ditherer = ditherer;
        this.grayDepth = grayDepth;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BufferedImage apply(BufferedImage source) {
        return grayDepth == 0 ?
                ditherer.dither(source, new MonochromaticQuantizer()) :
                ditherer.dither(source, new GrayscaleQuantizer(grayDepth));

    }
}
