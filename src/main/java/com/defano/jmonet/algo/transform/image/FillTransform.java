package com.defano.jmonet.algo.transform.image;

import com.defano.jmonet.algo.fill.DefaultFillFunction;
import com.defano.jmonet.algo.fill.FillFunction;
import com.defano.jmonet.algo.transform.StaticImageTransform;
import com.defano.jmonet.tools.util.ImageUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Fills every transparent pixel of an image that lies within a given mask with a specified paint or texture.
 * Has no effect on pixels which are not fully transparent (i.e., alpha == 0).
 *
 * See {@link FloodFillTransform} for the "spill paint" transform.
 */
public class FillTransform implements StaticImageTransform {

    private final Shape mask;
    private final Paint paint;
    private final FillFunction fillFunction;

    /**
     * Creates a fill transform.
     *
     * @param mask The shape determining which transparent pixels to fill; null to adjust all transparent pixels.
     * @param paint The {@link Paint} with which to fill
     * @param fillFunction An function that applies the paint to each affected pixel. Most users should simply supply
     *                     an instance of {@link DefaultFillFunction}.
     */
    public FillTransform(Shape mask, Paint paint, FillFunction fillFunction) {
        this.mask = mask;
        this.paint = paint;
        this.fillFunction = fillFunction;
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
                    Color c = new Color(transformed.getRGB(x, y), true);
                    if (c.getAlpha() == 0) {
                        fillFunction.fill(transformed, new Point(x, y), paint);
                    }
                }
            }
        }

        return transformed;
    }
}
