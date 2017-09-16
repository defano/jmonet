package com.defano.jmonet.algo;

import com.defano.jmonet.model.FlexQuadrilateral;
import com.defano.jmonet.tools.util.Geometry;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * A utility class providing various affine transform routines supported by the paint tools.
 */
public class Transform {

    public static BufferedImage slant(BufferedImage image, FlexQuadrilateral quadrilateral, int xTranslation) {
        Point p = new Point(quadrilateral.getBottomLeft().x, quadrilateral.getTopLeft().y);
        double theta = Geometry.theta(quadrilateral.getBottomLeft(), p, quadrilateral.getTopLeft());

        AffineTransform transform = AffineTransform.getTranslateInstance(xTranslation, 0);
        transform.shear(Math.tan(theta), 0);

        transform.translate(xTranslation, 0);
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        return op.filter(image, null);
    }

    public static BufferedImage rotate(BufferedImage image, double theta, int anchorX, int anchorY) {
        return new AffineTransformOp(rotateTransform(theta, anchorX, anchorY), AffineTransformOp.TYPE_BILINEAR).filter(image, null);
    }

    public static BufferedImage transform(BufferedImage image, AffineTransform transform) {
        return new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR).filter(image, null);
    }

    public static BufferedImage resize(BufferedImage image, FlexQuadrilateral quadrilateral) {
        Rectangle resizedBounds = quadrilateral.getShape().getBounds();
        BufferedImage resized = new BufferedImage(resizedBounds.width, resizedBounds.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) resized.getGraphics();
        g.drawImage(image, 0, 0, resizedBounds.width, resizedBounds.height, null);
        g.dispose();

        return resized;
    }

    public static BufferedImage rubberSheet(BufferedImage image, FlexQuadrilateral quadrilateral) {
        BufferedImage scaled = Transform.resize(image, quadrilateral);
        return Projection.rubberSheet(scaled, quadrilateral);
    }

    public static BufferedImage project(BufferedImage image, FlexQuadrilateral quadrilateral) {
        BufferedImage scaled = Transform.resize(image, quadrilateral);
        return Projection.project(scaled, quadrilateral);
    }

    public static AffineTransform rotateLeft(int width, int height) {
        AffineTransform transform = AffineTransform.getTranslateInstance(height / 2, width / 2);
        transform.quadrantRotate(3);
        transform.translate(-width / 2, -height / 2);
        return transform;
    }

    public static AffineTransform rotateRight(int width, int height) {
        AffineTransform transform = AffineTransform.getTranslateInstance(height / 2, width / 2);
        transform.quadrantRotate(1);
        transform.translate(-width / 2, -height / 2);
        return transform;
    }

    public static AffineTransform flipHorizontalTransform(int width) {
        AffineTransform transform = AffineTransform.getScaleInstance(-1, 1);
        transform.translate(-width, 0);
        return transform;
    }

    public static AffineTransform flipVerticalTransform(int height) {
        AffineTransform transform = AffineTransform.getScaleInstance(1, -1);
        transform.translate(0, -height);
        return transform;
    }

    public static AffineTransform rotateTransform(double radians, int centerX, int centerY) {
        return AffineTransform.getRotateInstance(radians, centerX, centerY);
    }


    public static void invert(BufferedImage selectedImage) {
        for (int x = 0; x < selectedImage.getWidth(); x++) {
            for (int y = 0; y < selectedImage.getHeight(); y++) {

                int argb = selectedImage.getRGB(x, y);
                int alpha = 0xff000000 & argb;
                int rgb = 0x00ffffff & argb;

                // Invert preserving alpha channel
                selectedImage.setRGB(x, y, alpha | (~rgb & 0x00ffffff));
            }
        }
    }

    public static void adjustBrightness(BufferedImage selectedImage, int delta) {
        for (int x = 0; x < selectedImage.getWidth(); x++) {
            for (int y = 0; y < selectedImage.getHeight(); y++) {

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

    public static void adjustTransparency(BufferedImage selectedImage, int delta) {
        for (int x = 0; x < selectedImage.getWidth(); x++) {
            for (int y = 0; y < selectedImage.getHeight(); y++) {

                Color c = new Color(selectedImage.getRGB(x, y), true);
                int alpha = c.getAlpha() + delta;
                alpha = alpha > 255 ? 255 : alpha < 0 ? 0 : alpha;

                // Adjust preserving alpha channel
                selectedImage.setRGB(x, y, new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha).getRGB());
            }
        }
    }

    public static BufferedImage rotateLeft(BufferedImage image) {
        return transform(image, rotateLeft(image.getWidth(), image.getHeight()));
    }

    public static BufferedImage rotateRight(BufferedImage image) {
        return transform(image, rotateRight(image.getWidth(), image.getHeight()));
    }

    public static BufferedImage flipHorizontal(BufferedImage image) {
        return transform(image, flipHorizontalTransform(image.getWidth()));
    }

    public static BufferedImage flipVertical(BufferedImage image) {
        return transform(image, Transform.flipVerticalTransform(image.getHeight()));
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
