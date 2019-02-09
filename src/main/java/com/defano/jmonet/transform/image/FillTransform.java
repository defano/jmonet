package com.defano.jmonet.transform.image;

import com.defano.jmonet.tools.attributes.FillFunction;
import com.defano.jmonet.tools.attributes.MarkPredicate;
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
    private final MarkPredicate markPredicate;

    /**
     * Creates a fill transform that fills marked pixels bounded by a mask.
     *
     * @param mask The shape bounding pixels to be filled.
     * @param paint The paint that will be used fill affected pixels
     * @param fillFunction A function that applies the paint to each affected pixel
     * @param markPredicate A predicate function that determines if each pixel in the bounded area is eligible to be
     *                      filled. Only unmarked pixels will be filled.
     */
    public FillTransform(Shape mask, Paint paint, FillFunction fillFunction, MarkPredicate markPredicate) {
        this.mask = mask;
        this.paint = paint;
        this.fillFunction = fillFunction;
        this.markPredicate = markPredicate;
    }

    /**
     * Creates a fill transform that fills only completely transparent pixels bounded by mask.
     *
     * @param mask The shape determining which transparent pixels to fill; null to adjust all transparent pixels.
     * @param paint The {@link Paint} with which to fill
     * @param fillFunction An function that applies the paint to each affected pixel. Most users should simply supply
     *                     an instance of {@link FillFunction}.
     */
    public FillTransform(Shape mask, Paint paint, FillFunction fillFunction) {
        this(mask, paint, fillFunction, new MarkPredicate() {
            @Override
            public boolean isMarked(Color pixel, Color eraseColor) {
                return pixel.getAlpha() > 0;
            }
        });
    }

    /**
     * Creates a fill transform that fills all completely transparent pixels in the raster.
     *
     * @param paint The {@link Paint} with which to fill
     * @param fillFunction An function that applies the paint to each affected pixel. Most users should simply supply
     *                     an instance of {@link FillFunction}.
     */
    public FillTransform(Paint paint, FillFunction fillFunction) {
        this(null, paint, fillFunction);
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
                    if (!markPredicate.isMarked(c, null)) {
                        fillFunction.fill(transformed, x, y, paint);
                    }
                }
            }
        }

        return transformed;
    }
}
