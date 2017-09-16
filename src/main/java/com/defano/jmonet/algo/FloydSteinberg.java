package com.defano.jmonet.algo;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.HashSet;

public class FloydSteinberg {

    /**
     * An implementation of the Floyd-Steinberg dithering algorithm.
     *
     * This method applies the given quantization function (which performs some transform on each pixel in the given
     * source image), and diffuses the quantization error across the image.
     *
     * See https://en.wikipedia.org/wiki/Floyd–Steinberg_dithering for a description of FS-dithering.
     *
     * @param source The source image to quantize and dither
     * @param quantizer The quantization function to apply and whose error should be dithered
     * @return A new image equal to the source image with the quantization function applied and dithered
     */
    public static BufferedImage dither(BufferedImage source, QuantizationFunction quantizer) {

        double[][][] matrix = toColorCubeMatrix(source);

        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[y].length; x++) {

                double[] oldPixel = matrix[y][x];
                double[] newPixel = quantizer.quantize(matrix[y][x]);

                matrix[y][x] = newPixel;

                // Calculate quantization error
                double qer = oldPixel[0] - newPixel[0];
                double qeg = oldPixel[1] - newPixel[1];
                double qeb = oldPixel[2] - newPixel[2];

                ditherPixel(matrix, x+1, y, qer, qeg, qeb, 7.0/16.0);
                ditherPixel(matrix, x-1, y+1, qer, qeg, qeb, 3.0/16.0);
                ditherPixel(matrix, x, y+1, qer, qeg, qeb, 5.0/16.0);
                ditherPixel(matrix, x+1, y+1, qer, qeg, qeb, 1.0/16.0);
            }
        }

        return fromColorCubeMatrix(matrix);
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
     *
     * The first dimension is the pixel's y-coordinate
     * The second dimension is the pixel's x-coordinate
     * the third dimension is the pixel's four-value color cube, where:
     *
     * The first item is the pixel's red channel color value represented as 0..1
     * The second item is the pixel's green channel color value represented as 0..1
     * The third item is the pixel's blue channel color value represented as 0..1
     * The fourth item is the pixel's alpha channel represented as 0..255
     *
     * @param image The image to convert into a color cube matrix
     * @return A three dimensional array representing the ARGB value of each pixel in the source image.
     */
    private static double[][][] toColorCubeMatrix(BufferedImage image) {

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

        return matrix;
    }

    /**
     * Converts a color cube matrix into a BufferedImage. See {@link #toColorCubeMatrix(BufferedImage)} for the
     * format details of the color cube matrix.
     *
     * @param matrix The matrix to convert into an image
     * @return The BufferedImage resulting from the given color cube matrix
     */
    private static BufferedImage fromColorCubeMatrix(double[][][] matrix) {
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

    private static void ditherPixel(double[][][] matrix, int x, int y, double qer, double qeg, double qeb, double fraction) {
        if (y >= 0 && y < matrix.length && x >= 0 && x < matrix[y].length) {
            matrix[y][x][0] += qer * fraction;
            matrix[y][x][1] += qeg * fraction;
            matrix[y][x][2] += qeb * fraction;
        }
    }
}
