package com.defano.jmonet.transform.image;

import com.defano.jmonet.transform.dither.Ditherer;
import com.defano.jmonet.transform.dither.FloydSteinbergDitherer;
import com.defano.jmonet.transform.dither.quant.ColorReductionQuantizer;
import com.defano.jmonet.transform.dither.quant.MonochromaticQuantizer;

import java.awt.image.BufferedImage;

/**
 * Converts the color space of an image to a reduced palette containing no more than a specified number of colors.
 * <p>
 * Note that this transform merely adjusts the look of the image and does not affect the image storage
 * in any way (all images are always stored in 24-bit "true color" irrespective of whether they have been reduced
 * via this method). Thus, reducing colors will not reduce memory usage or affect how the image is exported or saved.
 */
public class ColorReductionTransform implements StaticImageTransform {

    private final Ditherer ditherer;
    private final int colorDepth;

    /**
     * Constructs a color reduction transform.
     *
     * @param colorDepth The maximum number of unique colors that should appear in the resultant selection image; zero
     *                   produces a black and white (monochrome) image. Note that color depth should be cubic; if
     *                   the cubed root of colorDepth is not an integer, the the floor of the cubed root
     *                   will be assumed.
     * @param ditherer   The dithering algorithm to use, for example, {@link FloydSteinbergDitherer}.
     */
    public ColorReductionTransform(Ditherer ditherer, int colorDepth) {
        this.ditherer = ditherer;
        this.colorDepth = colorDepth;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BufferedImage apply(BufferedImage source) {
        int channelDepth = (int) Math.floor(Math.cbrt(colorDepth));

        return colorDepth == 0 ?
                ditherer.dither(source, new MonochromaticQuantizer()) :
                ditherer.dither(source, new ColorReductionQuantizer(channelDepth));
    }
}
