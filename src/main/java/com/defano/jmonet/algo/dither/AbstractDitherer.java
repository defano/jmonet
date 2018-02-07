package com.defano.jmonet.algo.dither;

import com.defano.jmonet.algo.Transform;
import com.defano.jmonet.algo.dither.quant.QuantizationFunction;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.HashSet;

/**
 * Abstract base class for implementers of {@link Ditherer} that operate on 24-bit, true color
 * ARGB images.
 */
public abstract class AbstractDitherer implements Ditherer {

    /**
     * A matrix providing a mapping of pixel coordinate to ARGB color value:
     * <p>
     * The first dimension is the pixel's y-coordinate
     * The second dimension is the pixel's x-coordinate
     * The third dimension is the pixel's four-value color cube, where:
     * <p>
     * The first item is the pixel's red channel color value represented as 0..1
     * The second item is the pixel's green channel color value represented as 0..1
     * The third item is the pixel's blue channel color value represented as 0..1
     * The fourth item is the pixel's alpha channel represented as 0..255
     */
    private double[][][] matrix;

    /**
     * Dithers a given quantization error for a specified pixel.
     * <p>
     * Most implementations of this method should distribute error by invoking
     * {@link #distributeError(int, int, double, double, double, double)} one or more times.
     *
     * @param x   The x-coordinate of the pixel to dither
     * @param y   The y-coordinate of the pixel to dither
     * @param qer The quantization error in the red color channel
     * @param qeg The quantization error in the green color channel
     * @param qeb The quantization error in the blue color channel
     */
    public abstract void ditherPixel(int x, int y, double qer, double qeg, double qeb);

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized BufferedImage dither(BufferedImage source, QuantizationFunction quantizer) {
        toColorCubeMatrix(source);

        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[y].length; x++) {

                double[] oldPixel = matrix[y][x];
                double[] newPixel = quantizer.quantize(matrix[y][x]);

                matrix[y][x] = newPixel;

                // Calculate quantization error
                double qer = oldPixel[0] - newPixel[0];
                double qeg = oldPixel[1] - newPixel[1];
                double qeb = oldPixel[2] - newPixel[2];

                ditherPixel(x, y, qer, qeg, qeb);
            }
        }

        return fromColorCubeMatrix();
    }

    /**
     * Determines the number of unique colors appearing in the given image.
     *
     * @param image The image whose colors should be counted.
     * @return The number of unique colors in the given image.
     */
    public int getColorCount(BufferedImage image) {
        HashSet<Integer> colors = new HashSet<>();

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                colors.add(image.getRGB(x, y));
            }
        }

        return colors.size();
    }

    /**
     * Converts the given image into a three-dimension matrix of color cubes.
     * <p>
     * The first dimension is the pixel's y-coordinate
     * The second dimension is the pixel's x-coordinate
     * the third dimension is the pixel's four-value color cube, where:
     * <p>
     * The first item is the pixel's red channel color value represented as 0..1
     * The second item is the pixel's green channel color value represented as 0..1
     * The third item is the pixel's blue channel color value represented as 0..1
     * The fourth item is the pixel's alpha channel represented as 0..255
     *
     * @param image The image to convert into a color cube matrix
     */
    private void toColorCubeMatrix(BufferedImage image) {

        // Source needs to be ARGB type; make a copy to assure constraint is met
        image = Transform.argbCopy(image);

        double[][][] matrix = new double[image.getHeight()][image.getWidth()][4];
        WritableRaster raster = image.getRaster();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                double[] pixel = raster.getPixel(x, y, (double[]) null);

                matrix[y][x][0] = pixel[0] / 255.0;
                matrix[y][x][1] = pixel[1] / 255.0;
                matrix[y][x][2] = pixel[2] / 255.0;
                matrix[y][x][3] = pixel[3];
            }
        }

        this.matrix = matrix;
    }

    /**
     * Converts a color cube matrix into a BufferedImage. See {@link #toColorCubeMatrix(BufferedImage)} for the
     * format details of the color cube matrix.
     *
     * @return The BufferedImage resulting from the given color cube matrix
     */
    private BufferedImage fromColorCubeMatrix() {
        BufferedImage restored = new BufferedImage(matrix[0].length, matrix.length, BufferedImage.TYPE_INT_ARGB);
        WritableRaster raster = restored.getRaster();

        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[y].length; x++) {
                double[] pixel = matrix[y][x];

                pixel[0] *= 255;
                pixel[1] *= 255;
                pixel[2] *= 255;
                pixel[3] = matrix[y][x][3];

                pixel[0] = pixel[0] < 0 ? 0 : pixel[0] > 255 ? 255 : pixel[0];
                pixel[1] = pixel[1] < 0 ? 0 : pixel[1] > 255 ? 255 : pixel[1];
                pixel[2] = pixel[2] < 0 ? 0 : pixel[2] > 255 ? 255 : pixel[2];

                raster.setPixel(x, y, pixel);
            }
        }

        return restored;
    }

    /**
     * Distributes a fraction of the quantization error to another pixel in the raster. Distribution
     * of quantization error adds (fraction * error) to the pixel's existing color channel values.
     * <p>
     * Has no effect if the specified pixel is not in the bounds of the current image.
     *
     * @param x        The x-coordinate of the pixel receiving the distributed quantization error
     * @param y        The y-coordinate of the pixel receiving the distributed quantization error
     * @param qer      The red channel quantization error
     * @param qeg      The green channel quantization error
     * @param qeb      The blue channel quantization error
     * @param fraction The fraction of each channel's error to be distributed to this pixel.
     */
    protected void distributeError(int x, int y, double qer, double qeg, double qeb, double fraction) {
        if (y >= 0 && y < matrix.length && x >= 0 && x < matrix[y].length) {
            matrix[y][x][0] += qer * fraction;
            matrix[y][x][1] += qeg * fraction;
            matrix[y][x][2] += qeb * fraction;
        }
    }

}
