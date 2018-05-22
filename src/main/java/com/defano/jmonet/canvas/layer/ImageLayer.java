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
     * Draws this image layer onto the given graphics context.
     *
     * @param g The graphics context on which to draw.
     */
    public void drawOnto(Graphics2D g, Double scale, Rectangle clip) {
        g.setComposite(composite);

        BufferedImage image = this.image;

        if (clip != null && (scale == null || scale == 1.0f)) {

            Rectangle drawBounds = new Rectangle(location.x, location.y, image.getWidth(), image.getHeight());
            Rectangle clipping = drawBounds.intersection(clip);

            if (clipping.isEmpty()) return;

            int x = clipping.x > location.x ? clipping.x - location.x : 0;
            int y = clipping.y > location.y ? clipping.y - location.y : 0;

            int width = Math.min(x + clipping.width, image.getWidth() - x);
            int height = Math.min(y + clipping.height, image.getHeight() - y);

            g.drawImage(image.getSubimage(x, y, width, height), location.x + x, location.y + y, null);
            return;
        }

        if (scale == null) {
            g.drawImage(image, location.x, location.y, null);
        } else {
            g.drawImage(image, (int) (location.x * scale), (int) (location.y * scale), (int) (image.getWidth(null) * scale), (int) (image.getHeight(null) * scale), null);
        }
    }

    public Point getLocation() {
        return location;
    }

    public BufferedImage getImage() {
        return image;
    }

    public AlphaComposite getComposite() {
        return composite;
    }

    public Dimension getSize() {
        return new Dimension(image.getWidth(null), image.getHeight(null));
    }
}
