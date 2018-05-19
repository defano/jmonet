package com.defano.jmonet.canvas.layer;

import java.awt.*;

/**
 * A layer in a layered image.
 */
public class ImageLayer {

    private final Point location;
    private final Image image;
    private final AlphaComposite composite;

    /**
     * Creates a layer in which the given image is drawn atop a destination image using {@link AlphaComposite#SRC_OVER}
     * at the image's origin (0, 0).
     *
     * @param image The image comprising this layer.
     */
    public ImageLayer(Image image) {
        this(new Point(0, 0), image, AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    /**
     * Creates a layer in which a given image is drawn atop a destination image using a provided composite mode at the
     * destination image's origin (0, 0).
     *
     * @param image     The image comprising this layer
     * @param composite The composite mode this layer is drawn with
     */
    public ImageLayer(Image image, AlphaComposite composite) {
        this(new Point(0, 0), image, composite);
    }

    /**
     * Creates a lyer in which a given image is drawn atop a destination image at a given position using a provided
     * composite mode.
     *
     * @param location The location on the destination where this layer should be drawn
     * @param image The image to be drawn
     * @param composite The composite mode to draw with
     */
    public ImageLayer(Point location, Image image, AlphaComposite composite) {
        this.location = location;
        this.image = image;
        this.composite = composite;
    }

    /**
     * Draws this image layer onto the given graphics context.
     *
     * @param g The graphics context on which to draw.
     */
    public void drawOnto(Graphics2D g) {
        g.setComposite(composite);
        g.drawImage(image, location.x, location.y, null);
    }

    public Point getLocation() {
        return location;
    }

    public Image getImage() {
        return image;
    }

    public AlphaComposite getComposite() {
        return composite;
    }

    public Dimension getSize() {
        return new Dimension(image.getWidth(null), image.getHeight(null));
    }
}
