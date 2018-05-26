package com.defano.jmonet.algo.transform.image;

import com.defano.jmonet.algo.transform.StaticImageTransform;
import com.defano.jmonet.algo.transform.PixelTransform;
import com.defano.jmonet.tools.util.ImageUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Applies a {@link PixelTransform} to every pixel of a given image that's contained within a masking shape.
 */
public class ApplyPixelTransform implements StaticImageTransform {

    private final PixelTransform transform;
    private final Shape mask;

    /**
     * Creates a transform that applies a {@link PixelTransform} to each pixel that is contained within the bounds of
     * the given shape.
     *
     * @param transform The pixel transform to apply
     * @param mask      The shape whose bounds determine which pixels the transform should be applied to
     */
    public ApplyPixelTransform(PixelTransform transform, Shape mask) {
        this.transform = transform;
        this.mask = mask;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BufferedImage apply(BufferedImage source) {
        BufferedImage transformed = ImageUtils.argbCopy(source);

        for (int x = 0; x < transformed.getWidth(); x++) {
            for (int y = 0; y < transformed.getHeight(); y++) {
                if (mask == null || mask.contains(x, y)) {
                    transformed.setRGB(x, y, transform.apply(x, y, transformed.getRGB(x, y)));
                }
            }
        }

        return transformed;
    }
}
