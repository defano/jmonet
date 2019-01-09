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
     * Draws this image layer onto a graphics context at scale, clipped to a subimage as requested. This image is
     * always drawn at the origin (0,0) of the graphics context, offset by this layer's location.
     *
     * @param g     The graphics context on which to draw.
     * @param scale The scale at which to draw the image (1.0 or null means no scaling). When null, no scaling will be
     *              provided.
     * @param clip  The clipping rectangle; only the portion of this image bounded by this rectangle will be drawn. When
     *              null, the entire image will be drawn. Note that this rectangle is represented in scaled coordinate
     *              space. This, the rect (10,10), (100,100) when scale is 2.0 refers to the this layer's
     *              sub image (5,5),(50,50).
     */
    public void paint(Graphics2D g, double scale, Rectangle clip) {
        g.setComposite(composite);

        // When a clipping region is not specified, draw the entire image layer
        if (clip == null) {
            clip = new Rectangle(0, 0, (int) ((location.x  + image.getWidth()) * scale), (int) ((location.y + image.getHeight()) * scale));
        }

        Rectangle unscaledClip = new Rectangle (
                (int) (clip.x / scale),
                (int) (clip.y / scale),
                (int) (clip.width / scale),
                (int) (clip.height / scale)
        );

        // Rectangle defining the sub-image of this layer that will be rendered
        int x1 = Math.max(0, unscaledClip.x - location.x);
        int y1 = Math.max(0, unscaledClip.y - location.y);
        int x2 = x1 + Math.min(image.getWidth(), unscaledClip.width);
        int y2 = y1 + Math.min(image.getHeight(), unscaledClip.height);

        // Rectangle onto which this image will be projected in the graphics context
        int dx1 = (int)(scale * Math.max(0, location.x - unscaledClip.x));
        int dy1 = (int)(scale * Math.max(0, location.y - unscaledClip.y));
        int dx2 = dx1 + (int)(Math.min(image.getWidth(), unscaledClip.width) * scale);
        int dy2 = dy1 + (int)(Math.min(image.getHeight(), unscaledClip.height) * scale);

        g.drawImage(image, dx1, dy1, dx2, dy2, x1, y1, x2, y2, null);
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
    public Dimension getDisplayedSize() {
        return new Dimension(location.x + image.getWidth(), location.y + image.getHeight());
    }

    public Dimension getStoredSize() {
        return new Dimension(image.getWidth(), image.getHeight());
    }

    @Override
    public String toString() {
        return "ImageLayer{" +
                "location=" + location +
                ", displayedSize=" + getDisplayedSize() +
                ", storedSize=" + getStoredSize() +
                '}';
    }
}
