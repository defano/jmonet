package com.defano.jmonet.tools.selection;

import com.defano.jmonet.canvas.PaintCanvas;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Represents an image selection.
 */
public interface Selection {

    /**
     * Gets the canvas on which the selection is being made.
     * @return The active canvas.
     */
    PaintCanvas getCanvas();

    /**
     * Determines the location (top-left coordinate) of the selection outline ({@link #getSelectionFrame()} on
     * the canvas, relative to the top-left coordinate of the canvas.
     *
     * @return Get the top-left coordinate of the selection boundary or null if no selection exists
     */
    Point getSelectionLocation();

    /**
     * Gets the current selection outline's shape and location, i.e., the path on the canvas where "marching ants" will
     * be drawn.
     *
     * Note that the location of the shape's bounds is translated to the selection's location on the canvas. Use
     * {@link #getIdentitySelectionFrame()} for a selection outline located at (0, 0) and suitable for testing whether
     * a pixel in the selected image is within the selection shape's mask.
     *
     * @return The selection outline's shape and location on the canvas
     */
    Shape getSelectionFrame();

    /**
     * Gets the current selection outline's shape, located at (0, 0).
     *
     * This method returns a shape useful for testing whether a pixel in the selected image's raster
     * (see {@link #getSelectedImage()}) is contained within the selection.
     *
     * @return The selection outline's shape
     */
    default Shape getIdentitySelectionFrame() {
        return AffineTransform.getTranslateInstance(-getSelectionLocation().x, -getSelectionLocation().y)
                .createTransformedShape(getSelectionFrame());
    }

    /**
     * Determines if the user has an active selection. A selection exists only after the user has completed defining
     * a closed, selection outline.
     *
     * @return True is a selection exists, false otherwise.
     */
    boolean hasSelection();

    /**
     * Gets the image represented by the selection, or null, if no selection exists.
     * <p>
     * Note that a user can select a non-rectangular selection, but the selected image returned by this method will
     * always be a rectangle whose bounds are the smallest rectangle that can fit in the selected shape Any pixels
     * outside the user's selection frame will be fully transparent in the returned image.
     *
     * @return The selected image
     */
    BufferedImage getSelectedImage();

    /**
     * Clears the scratch buffer and redraws the current state of the selection onto it.
     *
     * @param includeFrame When true, draws the selection frame (marching ants) on the buffer; when false, the frame
     *                     is omitted.
     */
    void redrawSelection(boolean includeFrame);

    /**
     * Creates a new image in which every pixel not within the selection frame (i.e., bounded by marching ants) has been
     * changed to fully transparent; the image produced is the same dimensions as the source image.
     *
     * @param image The image to crop
     * @return A BufferedImage in which every pixel not within the selection has been made transparent
     */
    default BufferedImage crop(BufferedImage image) {
        Shape mask = getSelectionFrame();
        BufferedImage maskedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        int clearPixel = new Color(0, 0, 0, 0).getRGB();

        for (int y = 0; y < image.getRaster().getHeight(); y++) {
            for (int x = 0; x < image.getRaster().getWidth(); x++) {
                if (x > image.getWidth() || y > image.getHeight()) continue;

                if (mask.contains(x, y)) {
                    maskedImage.setRGB(x, y, image.getRGB(x, y));
                } else {
                    maskedImage.setRGB(x, y, clearPixel);
                }
            }
        }

        return maskedImage;
    }

}
