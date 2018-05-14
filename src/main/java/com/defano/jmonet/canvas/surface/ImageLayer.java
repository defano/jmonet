package com.defano.jmonet.canvas.surface;

import java.awt.*;

/**
 * A layer in a layered image.
 */
public class ImageLayer {

    private final Point location;
    private final Image image;
    private final AlphaComposite composite;

    public ImageLayer(Image image) {
        this(new Point(0, 0), image, AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    public ImageLayer(Image image, AlphaComposite composite) {
        this(new Point(0, 0), image, composite);
    }

    public ImageLayer(Point location, Image image, AlphaComposite composite) {
        this.location = location;
        this.image = image;
        this.composite = composite;
    }

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
