package com.defano.jmonet.canvas.layer;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A layer in a layered image.
 */
public class ImageLayer {

    private final Point location;
    private final BufferedImage image;
    private final AlphaComposite composite;

    /**
     * Creates a layer in which the given image is drawn atop a destination image using {@link AlphaComposite#SRC_OVER}
     * at the image's origin (0, 0).
     *
     * @param image The image comprising this layer.
     */
    public ImageLayer(BufferedImage image) {
        this(new Point(0, 0), image, AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    /**
     * Creates a layer in which a given image is drawn atop a destination image using a provided composite mode at the
     * destination image's origin (0, 0).
     *
     * @param image     The image comprising this layer
     * @param composite The composite mode this layer is drawn with
     */
    public ImageLayer(BufferedImage image, AlphaComposite composite) {
        this(new Point(0, 0), image, composite);
    }

    /**
     * Creates a lyer in which a given image is drawn atop a destination image at a given position using a provided
     * composite mode.
     *
     * @param location  The location on the destination where this layer should be drawn
     * @param image     The image to be drawn
     * @param composite The composite mode to draw with
     */
    public ImageLayer(Point location, BufferedImage image, AlphaComposite composite) {
        this.location = location;
        this.image = image;
        this.composite = composite;
    }

    /**
     * Draws the visible portion of this image layer onto a graphics context at scale.
     *
     * @param g     The graphics context on which to draw.
     * @param scale The scale at which to draw the image (1.0 or null means no scaling). When null, no scaling will be
     *              provided.
     * @param clip  The clipping rectangle; only the portion of this image bounded by this rectangle will be drawn. When
     *              null, the entire image will be drawn. Note that this rectangle is represented in scaled coordinate
     *              space. This, the rect (10,10), (100,100) when scale is 2.0 refers to the this layer's
     *              sub image (5,5),(50,50).
     */
    public void paint(Graphics2D g, Double scale, Rectangle clip) {
        g.setComposite(composite);

        if (clip == null) {
            clip = new Rectangle(0, 0, image.getWidth(), image.getHeight());
        }

        if (scale == null) {
            scale = 1.0;
        }

        // Clipping rectangle is in scaled coordinate space; descale to model coordinates
        Rectangle unscaledClipRgn = new Rectangle((int) (clip.x / scale), (int) (clip.y / scale), (int) (clip.width / scale), (int) (clip.height / scale));

        // Unscaled bounding box of where image will be drawn on graphics context
        Rectangle imageBounds = new Rectangle(location.x, location.y, image.getWidth(), image.getHeight());

        // Portion of unscaled draw region that is also within the clipping rectangle
        Rectangle drawBounds = imageBounds.intersection(unscaledClipRgn);

        // Slightly overdraw the image (to prevent clipping on bottom and right-most row/column)
        drawBounds.setSize(drawBounds.width + 2, drawBounds.height + 2);

        // Draw source geometry from image into destination geometry of graphics context
        g.drawImage(image,
                0, 0, (int) (scale * drawBounds.width), (int) (scale * drawBounds.height),
                drawBounds.x, drawBounds.y, drawBounds.x + drawBounds.width, drawBounds.y + drawBounds.height,
                null);
    }

    /**
     * Gets the location of this image layer. Coordinates are relative to the base image this layer is being drawn upon.
     *
     * @return The layer's location.
     */
    public Point getLocation() {
        return location;
    }

    /**
     * Gets the image associated with this layer.
     *
     * @return The layer's image.
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Gets the alpha composite mode used to overlay this image on the base.
     *
     * @return The overlay alpha mode.
     */
    public AlphaComposite getComposite() {
        return composite;
    }

    /**
     * Gets the dimensions of the image represented by this layer.
     *
     * @return The image size
     */
    public Dimension getSize() {
        return new Dimension(image.getWidth(null), image.getHeight(null));
    }
}
