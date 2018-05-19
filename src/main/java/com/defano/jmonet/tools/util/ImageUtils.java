package com.defano.jmonet.tools.util;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtils {

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

    /**
     * Calculates the smallest bounding rectangle that completely frames all pixels in the source image that are not
     * fully transparent. This bounding rectangle can be used to retrieve a sub-image
     * ({@link BufferedImage#getSubimage(int, int, int, int)}) that fully encapsulates all visible pixels.
     *
     * @param src A source image to analyze
     * @return The minimum bounding rectangle.
     */
    public static Rectangle getMinimumBounds(BufferedImage src) {

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = 0;
        int maxY = 0;

        for (int x = 0; x < src.getWidth(); x++) {
            for (int y = 0; y < src.getHeight(); y++) {
                Color c = new Color(src.getRGB(x, y), true);
                if (c.getAlpha() != 0) {
                    minX = Math.min(minX, x);
                    minY = Math.min(minY, y);
                    maxX = Math.max(maxX, x);
                    maxY = Math.max(maxY, y);
                }
            }
        }

        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }
}
