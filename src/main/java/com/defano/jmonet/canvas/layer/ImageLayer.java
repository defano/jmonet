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
     * @param g The graphics context on which to draw.
     * @param scale The scale at which to draw the image (1.0 or null means no scaling)
     * @param clip The portion of this image that is visible within the graphics context.
     */
    public void drawOnto(Graphics2D g, Double scale, final Rectangle clip) {
        g.setComposite(composite);

        if (clip != null) {

            if (scale == null) {
                scale = 1.0;
            }

            // Scale position of clipping rectangle
            Rectangle translatedClip = new Rectangle((int) (clip.x / scale), (int) (clip.y / scale), clip.width, clip.height);

            // Unscaled bounding box of where image will be drawn
            Rectangle drawBounds = new Rectangle(location.x, location.y, image.getWidth(), image.getHeight());

            // Portion of unscaled draw region that is also within the clipping rectangle
            Rectangle visibleBounds = drawBounds.intersection(translatedClip);

            // Nothing to draw if visible bounds is empty
            if (visibleBounds.isEmpty()) return;

            // Calculate bounds of unscaled subimage (visible portion of this layer after clipping)
            int x = visibleBounds.x > location.x ? visibleBounds.x - location.x : 0;
            int y = visibleBounds.y > location.y ? visibleBounds.y - location.y : 0;
            int width = Math.min(x + visibleBounds.width, image.getWidth() - x);
            int height = Math.min(y + visibleBounds.height, image.getHeight() - y);
            BufferedImage clippedImage = image.getSubimage(x, y, width, height);

            // Calculate scaled location and size of visible subimage
            int renderX = (int) ((x + location.x) * scale);
            int renderY = (int) ((y + location.y) * scale);
            int renderW = (int) (width * scale);
            int renderH = (int) (height * scale);
            g.drawImage(clippedImage, renderX, renderY, renderW, renderH, null);

        } else if (scale == null) {
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
