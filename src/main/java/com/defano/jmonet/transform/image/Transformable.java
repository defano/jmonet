package com.defano.jmonet.transform.image;

import com.defano.jmonet.transform.dither.Ditherer;
import com.defano.jmonet.transform.dither.FloydSteinbergDitherer;
import com.defano.jmonet.tools.attributes.FillFunction;
import com.defano.jmonet.transform.pixel.BrightnessPixelTransform;
import com.defano.jmonet.transform.pixel.InvertPixelTransform;
import com.defano.jmonet.transform.pixel.RemoveAlphaPixelTransform;
import com.defano.jmonet.transform.pixel.TransparencyPixelTransform;

import java.awt.*;
import java.awt.image.BufferedImageOp;
import java.awt.image.Kernel;

/**
 * An image-containing object whose image can be transformed by a {@link StaticImageTransform}. This is a facade
 * interface providing convenience methods for common transforms.
 */
public interface Transformable extends StaticImageTransformable {

    /**
     * Performs a convolution of the image using the given kernel (convolution matrix).
     *
     * @param kernel The {@link Kernel} to apply
     */
    default void convolve(Kernel kernel) {
        transform(new ConvolutionTransform(kernel));
    }

    /**
     * Performs a BufferedImageOp on the image.
     *
     * @param operation The {@link BufferedImageOp} to perform.
     */
    default void operate(BufferedImageOp operation) {
        transform(new BufferedImageOpTransform(operation));
    }

    /**
     * Converts the image to a reduced palette containing no more than the specified number of colors.
     * <p>
     * This method uses a naive color quantization algorithm that does not optimize the reduced color palette for the
     * given image; it simply produces a new palette with colors evenly distributed in the color space and maps colors
     * in the selection image to the nearest color in the reduced palette.
     * <p>
     * The number of unique colors in the reduced-color selection will not, necessarily, equal the provided color
     * depth.
     * <p>
     * Note that this method merely adjusts the look of the selection and does not affect the canvas' image storage
     * in any way (all images are always stored in 24-bit "true color" irrespective of whether they have been reduced
     * via this method. Thus, reducing colors will not reduce memory usage or affect how the image is exported or saved.
     *
     * @param colorDepth The maximum number of unique colors that should appear in the resultant selection image; zero
     *                   produces a black and white (monochrome) image. Note that color depth should be cubic; if
     *                   the cubed root of colorDepth is not an integer, the cube of the floor of the cubed root
     *                   will be assumed.
     * @param ditherer   The dithering algorithm to use, for example, {@link FloydSteinbergDitherer}.
     */
    default void reduceColor(int colorDepth, Ditherer ditherer) {
        transform(new ColorReductionTransform(ditherer, colorDepth));
    }

    /**
     * Converts the image to gray-scale, containing no more than the specified number of gray shades.
     * See {@link #reduceColor(int, Ditherer)} for details about palette selection and dithering.
     *
     * @param grayDepth The maximum number of unique shades of gray in which to render the given image; zero produces
     *                  a black and white (monochrome) image.
     * @param ditherer  The dithering algorithm to use, for example {@link FloydSteinbergDitherer}.
     */
    default void reduceGreyscale(int grayDepth, Ditherer ditherer) {
        transform(new GreyscaleReductionTransform(ditherer, grayDepth));
    }

    /**
     * Adjusts the brightness of the image by the amount specified in delta while preserving the image's alpha
     * transparency.
     *
     * @param delta An amount by which brightness should be adjusted, -256 to +256. Negative numbers darken an image,
     *              positive numbers brighten it.
     */
    default void adjustBrightness(int delta) {
        transform(new BrightnessPixelTransform(delta));
    }

    /**
     * Adjusts the alpha transparency of the image while maintain hue, saturation and brightness.
     *
     * @param delta An amount by which transparency should be adjusted -256 to +256; Negative numbers make the image
     *              more transparent, positive numbers make the image more opaque.
     */
    default void adjustTransparency(int delta) {
        transform(new TransparencyPixelTransform(delta));
    }

    /**
     * Inverts the color of the image while preserving the alpha transparency.
     */
    default void invert() {
        transform(new InvertPixelTransform());
    }

    /**
     * Makes translucent pixels in the active selection either fully transparent or fully opaque. Has no effect on
     * pixels that are either fully opaque or fully transparent.
     *
     * @param makeTransparent When true, translucent pixels will become transparent; when false, opaque.
     */
    default void removeTranslucency(boolean makeTransparent) {
        transform(new RemoveAlphaPixelTransform(makeTransparent));
    }

    /**
     * Fills all transparent pixels in the image with the given fill paint.
     *
     * @param fillPaint    The paint to fill with.
     * @param fillFunction A method to fill pixels in the selected image
     */
    default void fill(Paint fillPaint, FillFunction fillFunction) {
        transform(new FillTransform(fillPaint, fillFunction));
    }

}
