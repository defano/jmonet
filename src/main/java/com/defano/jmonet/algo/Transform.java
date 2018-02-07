package com.defano.jmonet.algo;

import com.defano.jmonet.model.Quadrilateral;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * A utility for performing various affine transform routines supported by the paint tools.
 */
public class Transform {

    /**
     * Vertically slants (shears) an image by a given angle and applies an x-translation to compensate for the change
     * in position implied by the slant (allow the vertical center-line to remain constant, if desired).
     *
     * @param image The image to transform
     * @param theta The angle, in radians, to shear the image
     * @param xTranslation The number of pixels to translate the image, typically this is calculated by determining the
     *                     number of pixels left or right the image has been sheared (delta between top-left corner and
     *                     bottom-left corner), then dividing this value in half.
     * @return The sheared and translated result. Note that the size/bounds of the result may be enlarged to prevent
     * clipping.
     */
    public static BufferedImage slant(BufferedImage image, double theta, int xTranslation) {
        AffineTransform transform = AffineTransform.getTranslateInstance(xTranslation, 0);
        transform.shear(Math.tan(theta), 0);

        transform.translate(xTranslation, 0);
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);
        return op.filter(image, null);
    }

    /**
     * Rotates an image about a specified axis.
     *
     * @param image The image to rotate
     * @param theta The angle (in radians) that is should be rotated
     * @param anchorX The x-coordinate of the rotational axis (typically the centerpoint of the image)
     * @param anchorY The y-coordinate of the rotational axis (typically the centerpoint of the image)
     * @return The rotated image. Note that the size/bounds of the result may be enlarged to prevent clipping of the
     * original image.
     */
    public static BufferedImage rotate(BufferedImage image, double theta, int anchorX, int anchorY) {
        return new AffineTransformOp(rotateTransform(theta, anchorX, anchorY), AffineTransformOp.TYPE_BICUBIC).filter(image, null);
    }

    /**
     * Performs an affine transform on a given image.
     *
     * @param image The image to transform
     * @param transform The transform to perform
     * @return The transformed image
     */
    public static BufferedImage transform(BufferedImage image, AffineTransform transform) {
        return new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC).filter(image, null);
    }

    /**
     * Scales an image to a requested dimension.
     *
     * @param image The image to be scaled
     * @param size The dimension the image should be scaled to
     * @return The resized image
     */
    public static BufferedImage resize(BufferedImage image, Dimension size) {
        BufferedImage resized = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) resized.getGraphics();
        g.drawImage(image, 0, 0, size.width, size.height, null);
        g.dispose();

        return resized;
    }

    /**
     * Performs a rubber sheet projection of an image onto an arbitrary quadrilateral. Implicitly scales the image
     * as required to match the quadrilateral.
     *
     * @param image The image to be projected.
     * @param quadrilateral The quadrilateral on which to project the image
     * @return The projected image
     */
    public static BufferedImage rubberSheet(BufferedImage image, Quadrilateral quadrilateral) {
        BufferedImage scaled = resize(image, quadrilateral.getShape().getBounds().getSize());
        return Projection.rubberSheet(scaled, quadrilateral);
    }

    /**
     * Performs a homography projection of an image onto an arbitrary quadrilateral. Implicitly scales the image as
     * required to match the quadrilateral.
     *
     * @param image The image to be projected
     * @param quadrilateral The quadrilateral on which to project the image
     * @return The projected image
     */
    public static BufferedImage project(BufferedImage image, Quadrilateral quadrilateral) {
        BufferedImage scaled = resize(image, quadrilateral.getShape().getBounds().getSize());
        return Projection.project(scaled, quadrilateral);
    }

    /**
     * Creates an {@link AffineTransform} that performs a 90-degree counter-clockwise rotation.
     *
     * @param width The width of the shape or image to be rotated
     * @param height The height of the shape or image to be rotated
     * @return The rotation transform
     */
    public static AffineTransform rotateLeft(int width, int height) {
        AffineTransform transform = AffineTransform.getTranslateInstance(height / 2, width / 2);
        transform.quadrantRotate(3);
        transform.translate(-width / 2, -height / 2);
        return transform;
    }

    /**
     * Creates an {@link AffineTransform} that performs a 90-degree clockwise rotation.
     *
     * @param width The width of the shape or image to be rotated
     * @param height The height of the shape or image to be rotated
     * @return The rotation transform
     */
    public static AffineTransform rotateRight(int width, int height) {
        AffineTransform transform = AffineTransform.getTranslateInstance(height / 2, width / 2);
        transform.quadrantRotate(1);
        transform.translate(-width / 2, -height / 2);
        return transform;
    }

    /**
     * Creates an {@link AffineTransform} that performs a horizontal mirroring of a shape or image about its vertical
     * center-line.
     *
     * @param width The width of the shape or image to be rotated
     * @return The flip transform
     */
    public static AffineTransform flipHorizontalTransform(int width) {
        AffineTransform transform = AffineTransform.getScaleInstance(-1, 1);
        transform.translate(-width, 0);
        return transform;
    }

    /**
     * Creates an {@link AffineTransform} that performs a vertical mirroring of a shape or image about its horizontal
     * center-line.
     *
     * @param height The height of the shape or image to be rotated
     * @return The flip transform
     */
    public static AffineTransform flipVerticalTransform(int height) {
        AffineTransform transform = AffineTransform.getScaleInstance(1, -1);
        transform.translate(0, -height);
        return transform;
    }

    /**
     * Creates an {@link AffineTransform} that performs a rotation about a specified axis.
     *
     * @param radians The angle (in radians) that the the transform should rotate
     * @param centerX The x-coordinate of the axis about which the rotation should occur
     * @param centerY The y-coordinate of the axis about which the rotation should occur
     * @return The rotation transform
     */
    public static AffineTransform rotateTransform(double radians, int centerX, int centerY) {
        return AffineTransform.getRotateInstance(radians, centerX, centerY);
    }

    /**
     * Inverts the color of every pixel of an image that lies within a given mask. Operation is destructive to the
     * input image.
     *
     * @param selectedImage The image to invert
     * @param mask The shape determining which pixels to invert; null to invert all pixels
     */
    public static void invert(BufferedImage selectedImage, Shape mask) {
        for (int x = 0; x < selectedImage.getWidth(); x++) {
            for (int y = 0; y < selectedImage.getHeight(); y++) {
                if (mask == null || mask.contains(x, y)) {

                    int argb = selectedImage.getRGB(x, y);
                    int alpha = 0xff000000 & argb;
                    int rgb = 0x00ffffff & argb;

                    // Invert preserving alpha channel
                    selectedImage.setRGB(x, y, alpha | (~rgb & 0x00ffffff));
                }
            }
        }
    }

    /**
     * Modifies the brightness/luminosity of every pixel of an image that lies within a given mask. Operation is
     * destructive to the input image.
     *
     * @param selectedImage The image whose brightness should be changed
     * @param mask The shape determining which pixels to adjust; null to adjust all pixels
     * @param delta A value between -255 and +255; delta will be added to red, green and blue color channels equally (
     *              channel values are clipped in the range of 0..255).
     */
    public static void adjustBrightness(BufferedImage selectedImage, Shape mask, int delta) {
        for (int x = 0; x < selectedImage.getWidth(); x++) {
            for (int y = 0; y < selectedImage.getHeight(); y++) {
                if (mask == null || mask.contains(x, y)) {

                    int argb = selectedImage.getRGB(x, y);
                    int alpha = 0xff000000 & argb;
                    int r = ((0xff0000 & argb) >> 16) + delta;
                    int g = ((0xff00 & argb) >> 8) + delta;
                    int b = (0xff & argb) + delta;

                    // Saturate at 0 and 256
                    r = r > 0xff ? 0xff : r < 0 ? 0 : r;
                    g = g > 0xff ? 0xff : g < 0 ? 0 : g;
                    b = b > 0xff ? 0xff : b < 0 ? 0 : b;

                    // Adjust preserving alpha channel
                    selectedImage.setRGB(x, y, alpha | (r << 16) | (g << 8) | b);
                }
            }
        }
    }

    /**
     * Modifies the alpha channel of every pixel of an image that lies within a given mask. Operation is destructive to
     * the input image.
     *
     * @param selectedImage The image whose opacity should be changed
     * @param mask The shape determining which pixels to adjust; null to adjust all pixels
     * @param delta A value between -255 and +255; delta will be added to each pixel's alpha channel (alpha channel
     *              is clipped in the range of 0..255).
     */
    public static void adjustTransparency(BufferedImage selectedImage, Shape mask, int delta) {
        for (int x = 0; x < selectedImage.getWidth(); x++) {
            for (int y = 0; y < selectedImage.getHeight(); y++) {
                if (mask == null || mask.contains(x, y)) {

                    Color c = new Color(selectedImage.getRGB(x, y), true);
                    int alpha = c.getAlpha() + delta;
                    alpha = alpha > 255 ? 255 : alpha < 0 ? 0 : alpha;

                    // Adjust preserving alpha channel
                    selectedImage.setRGB(x, y, new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha).getRGB());
                }
            }
        }
    }

    /**
     * Fills every transparent pixel of an image that lies within a given mask with a specified paint or texture.
     * Operation is destructive to the input image. Has no effect on pixels which are not fully transparent (i.e.,
     * alpha == 0).
     *
     * @param selectedImage The image to fill
     * @param mask The shape determining which transparent pixels to fill; null to adjust all transparent pixels.
     * @param paint The {@link Paint} with which to fill
     * @param fillFunction An function that applies the paint to each affected pixel. Most users should simply supply
     *                     an instance of {@link DefaultFillFunction}.
     */
    public static void fill(BufferedImage selectedImage, Shape mask, Paint paint, FillFunction fillFunction) {
        for (int x = 0; x < selectedImage.getWidth(); x++) {
            for (int y = 0; y < selectedImage.getHeight(); y++) {
                if (mask == null || mask.contains(x, y)) {

                    Color c = new Color(selectedImage.getRGB(x, y), true);
                    if (c.getAlpha() == 0) {
                        fillFunction.fill(selectedImage, new Point(x, y), paint);
                    }
                }
            }
        }
    }

    /**
     * Rotates an image 90-degrees counter-clockwise.
     *
     * @param image The image to rotate
     * @return The rotated image
     */
    public static BufferedImage rotateLeft(BufferedImage image) {
        return transform(image, rotateLeft(image.getWidth(), image.getHeight()));
    }

    /**
     * Rotates an image 90-degrees clockwise.
     *
     * @param image The image to rotate
     * @return The rotated image
     */
    public static BufferedImage rotateRight(BufferedImage image) {
        return transform(image, rotateRight(image.getWidth(), image.getHeight()));
    }

    /**
     * Flips an image about its vertical center-line.
     *
     * @param image The image to flip
     * @return The flipped image
     */
    public static BufferedImage flipHorizontal(BufferedImage image) {
        return transform(image, flipHorizontalTransform(image.getWidth()));
    }

    /**
     * Flips an image about its horizontal center-line.
     *
     * @param image The image to flip
     * @return The flipped image
     */
    public static BufferedImage flipVertical(BufferedImage image) {
        return transform(image, flipVerticalTransform(image.getHeight()));
    }

    /**
     * Makes a "deep" copy of the given image, returning a copy whose type is TYPE_INT_ARGB.
     *
     * @param src The image to copy.
     * @return A copy of the source, in ARGB mode.
     */
    public static BufferedImage argbCopy(BufferedImage src) {
        BufferedImage copy = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = copy.getGraphics();
        g.drawImage(src, 0, 0, null);
        g.dispose();
        return copy;
    }

}
