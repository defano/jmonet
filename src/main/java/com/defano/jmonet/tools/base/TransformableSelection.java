package com.defano.jmonet.tools.base;

import com.defano.jmonet.algo.*;
import com.defano.jmonet.algo.dither.*;
import com.defano.jmonet.algo.dither.quant.ColorReductionQuantizer;
import com.defano.jmonet.algo.dither.quant.GrayscaleQuantizer;
import com.defano.jmonet.algo.dither.quant.MonochromaticQuantizer;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Defines a series of "static" transforms that can be applied to a selection. A static transform is one that can
 * be applied directly, without user/mouse engagement (e.g., scale, slant, or projection).
 */
public interface TransformableSelection extends MutableSelection {

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
     * Rotates the image 90 degrees counter-clockwise.
     */
    default void rotateLeft() {
        if (hasSelection()) {
            int width = getSelectedImage().getWidth();
            int height = getSelectedImage().getHeight();

            applyTransform(Transform.rotateLeft(width, height));
            adjustSelectionBounds((width - height) / 2, -(width - height) / 2);
        }
    }

    /**
     * Rotates the image 90 degrees clockwise.
     */
    default void rotateRight() {
        if (hasSelection()) {
            int width = getSelectedImage().getWidth();
            int height = getSelectedImage().getHeight();

            applyTransform(Transform.rotateRight(width, height));
            adjustSelectionBounds((width - height) / 2, -(width - height) / 2);
        }
    }

    /**
     * Flips or mirrors the image along its vertical axis. That is, all pixels on the left side of the image
     * will move the right side and vice versa.
     */
    default void flipHorizontal() {
        if (hasSelection()) {
            int width = getSelectedImage().getWidth();
            applyTransform(Transform.flipHorizontalTransform(width));
        }
    }

    /**
     * Flips or mirrors the image along its horizontal axis. That is, all pixels on the top of the image will
     * move to the bottom and vice versa.
     */
    default void flipVertical() {
        if (hasSelection()) {
            int height = getSelectedImage().getHeight();
            applyTransform(Transform.flipVerticalTransform(height));
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
            Transform.adjustBrightness(getSelectedImage(), delta);
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
            Transform.adjustTransparency(getSelectedImage(), delta);
            setDirty();
        }
    }

    /**
     * Inverts the color of the image while preserving the alpha transparency.
     */
    default void invert() {
        if (hasSelection()) {
            Transform.invert(getSelectedImage());
            setDirty();
        }
    }

    /**
     * Applies an AffineTransform the current image.
     * @param transform The transform to apply.
     */
    default void applyTransform(AffineTransform transform) {
        if (hasSelection()) {
            setDirty();

            // Get the original location of the selection
            Point originalLocation = getSelectionLocation();

            // Transform the selected image
            setSelectedImage(Transform.transform(getSelectedImage(), transform));

            // Relocate the image to its original location
            Rectangle newBounds = getSelectedImage().getRaster().getBounds();
            newBounds.setLocation(originalLocation);
            setSelectionOutline(newBounds);

            redrawSelection();
        }
    }
}
