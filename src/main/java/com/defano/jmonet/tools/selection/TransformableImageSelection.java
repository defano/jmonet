package com.defano.jmonet.tools.selection;

import com.defano.jmonet.algo.DefaultFillFunction;
import com.defano.jmonet.algo.Transform;
import com.defano.jmonet.algo.dither.Ditherer;
import com.defano.jmonet.algo.dither.FloydSteinbergDitherer;
import com.defano.jmonet.algo.dither.quant.ColorReductionQuantizer;
import com.defano.jmonet.algo.dither.quant.GrayscaleQuantizer;
import com.defano.jmonet.algo.dither.quant.MonochromaticQuantizer;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Represents a selection in which the pixels of the selected image can be transformed (i.e., change of brightness,
 * opacity, etc.).
 *
 * Differs from {@link TransformableSelection} in that these transforms do no change the selection shape (outline) or
 * location on the canvas; only the underlying selected image.
 */
public interface TransformableImageSelection extends MutableSelection {

    /**
     * Converts the current selection to a reduced palette containing no more than the specified number of colors.
     *
     * This method uses a naive color quantization algorithm that does not optimize the reduced color palette for the
     * given image; it simply produces a new palette with colors evenly distributed in the color space and maps colors
     * in the selection image to the nearest color in the reduced palette.
     *
     * The number of unique colors in the reduced-color selection will not, necessarily, equal the provided color
     * depth.
     *
     * Note that this method merely adjusts the look of the selection and does not affect the canvas' image storage
     * in any way (all images are always stored in 24-bit "true color" irrespective of whether they have been reduced
     * via this method. Thus, reducing colors will not reduce memory usage or affect how the image is exported or saved.
     *
     * @param colorDepth The maximum number of unique colors that should appear in the resultant selection image; zero
     *                   produces a black and white (monochrome) image. Note that color depth should be cubic; if
     *                   the cubed root of colorDepth is not an integer, the cube of the floor of the cubed root
     *                   will be assumed.
     * @param ditherer The dithering algorithm to use, for example, {@link FloydSteinbergDitherer}.
     */
    default void reduceColor(int colorDepth, Ditherer ditherer) {
        if (hasSelection()) {
            BufferedImage source = getSelectedImage();
            int channelDepth = (int) Math.floor(Math.cbrt(colorDepth));

            BufferedImage reduced = colorDepth == 0 ?
                    ditherer.dither(source, new MonochromaticQuantizer()) :
                    ditherer.dither(source, new ColorReductionQuantizer(channelDepth));

            setSelectedImage(reduced);
            setDirty();
        }
    }

    /**
     * Converts the current selection to a gray-scale image containing no more than the specified number of gray shades.
     * See {@link #reduceColor(int, Ditherer)} for details about palette selection and dithering.
     *
     * @param grayDepth The maximum number of unique shades of gray in which to render the given image; zero produces
     *                  a black and white (monochrome) image.
     * @param ditherer The dithering algorithm to use, for example {@link FloydSteinbergDitherer}.
     */
    default void reduceGreyscale(int grayDepth, Ditherer ditherer) {
        if (hasSelection()) {
            BufferedImage source = getSelectedImage();

            BufferedImage reduced = grayDepth == 0 ?
                    ditherer.dither(source, new MonochromaticQuantizer()) :
                    ditherer.dither(source, new GrayscaleQuantizer(grayDepth));

            setSelectedImage(reduced);
            setDirty();
        }
    }

    /**
     * Adjusts the brightness of the image by the amount specified in delta while preserving the image's alpha
     * transparency.
     *
     * @param delta An amount by which brightness should be adjusted, -256 to +256. Negative numbers darken an image,
     *              positive numbers brighten it.
     */
    default void adjustBrightness(int delta) {
        if (hasSelection()) {
            Transform.adjustBrightness(getSelectedImage(), getIdentitySelectionOutline(), delta);
            setDirty();
        }
    }

    /**
     * Adjusts the alpha transparency of the image while maintain hue, saturation and brightness.
     *
     * @param delta An amount by which transparency should be adjusted -256 to +256; Negative numbers make the image
     *              more transparent, positive numbers make the image more opaque.
     */
    default void adjustTransparency(int delta) {
        if (hasSelection()) {
            Transform.adjustTransparency(getSelectedImage(), getIdentitySelectionOutline(), delta);
            setDirty();
        }
    }

    /**
     * Inverts the color of the image while preserving the alpha transparency.
     */
    default void invert() {
        if (hasSelection()) {
            Transform.invert(getSelectedImage(), getIdentitySelectionOutline());
            setDirty();
        }
    }

    /**
     * Fills all transparent pixels in the selection with the given fill paint.
     * @param fillPaint The paint to fill with.
     */
    default void fill(Paint fillPaint) {
        if (hasSelection()) {
            Transform.fill(getSelectedImage(), getIdentitySelectionOutline(), fillPaint, new DefaultFillFunction());
            setDirty();
        }
    }

}
