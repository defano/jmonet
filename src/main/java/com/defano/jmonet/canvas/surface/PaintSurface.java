package com.defano.jmonet.canvas.surface;

import com.defano.jmonet.canvas.layer.ScaledLayeredImage;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A surface that paints a layered image.
 */
public abstract class PaintSurface extends Surface implements ScaledLayeredImage {

    private final static Color CLEAR_COLOR = new Color(0, 0, 0, 0);
    private final BehaviorSubject<Double> scaleSubject = BehaviorSubject.createDefault(1.0);
    private Dimension surfaceDimensions = new Dimension();

    private double scanlineThreadhold = 6.0;
    private Color scanlineColor = Color.WHITE;
    private AlphaComposite scanlineComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);

    public abstract Paint getCanvasBackground();

    /**
     * Creates a paint surface with the specified dimensions. Note that this dimension refers to the size of the
     * "paintable" space, which is not the same as the size of the Swing component (the surface dimension may be larger,
     * smaller, or the same size as this Swing component).
     *
     * @param surfaceDimensions The size of the paintable surface.
     */
    public PaintSurface(Dimension surfaceDimensions) {
        super();

        setOpaque(false);
        setSurfaceDimension(surfaceDimensions);
    }

    /**
     * Gets the un-scaled dimensions of the surface (that is, the size of the image which can be painted on it).
     *
     * @return The un-scaled dimensions of this surface.
     */
    public Dimension getSurfaceDimension() {
        return surfaceDimensions;
    }

    /**
     * Specifies the un-scaled size of this painting surface. This determines the size of the image (document) that will
     * be painted by a user.
     *
     * @param surfaceDimensions The dimensions of the painting surface
     */
    public void setSurfaceDimension(Dimension surfaceDimensions) {
        this.surfaceDimensions = new Dimension(surfaceDimensions.width, surfaceDimensions.height);
        Dimension scaledDimension = getScaledDimension(surfaceDimensions);

        setMaximumSize(scaledDimension);
        setPreferredSize(scaledDimension);
        setSize(scaledDimension);
        invalidate();
    }

    /**
     * Gets the dimensions of the surface multiplied by the surface's scale factor.
     *
     * @return The scaled surface dimensions.
     */
    public Dimension getScaledSurfaceDimension() {
        return getScaledDimension(surfaceDimensions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getScale() {
        return scaleSubject.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setScale(double scale) {
        scaleSubject.onNext(scale);

        Dimension scaledDimension = getScaledDimension(surfaceDimensions);

        getSurfaceScrollController().resetScrollPosition();
        setPreferredSize(scaledDimension);
        setMaximumSize(scaledDimension);
        invalidate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Observable<Double> getScaleObservable() {
        return scaleSubject;
    }

    public double getScanlineThreadhold() {
        return scanlineThreadhold;
    }

    public void setScanlineThreadhold(double scanlineThreadhold) {
        this.scanlineThreadhold = scanlineThreadhold;
    }

    public Color getScanlineColor() {
        return scanlineColor;
    }

    public void setScanlineColor(Color scanlineColor) {
        this.scanlineColor = scanlineColor;
    }

    public AlphaComposite getScanlineComposite() {
        return scanlineComposite;
    }

    public void setScanlineComposite(AlphaComposite scanlineComposite) {
        this.scanlineComposite = scanlineComposite;
    }

    /**
     * Unregisters listeners and removes all child components making this surface available for garbage collection.
     */
    public void dispose() {
        super.dispose();

        scaleSubject.onComplete();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
        removeAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Rectangle clip = g.getClipBounds();

        if (clip != null && !clip.isEmpty() && isVisible()) {

            // Draw visible portion of this surface's image into a buffer
            BufferedImage buffer = new BufferedImage(clip.width, clip.height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = buffer.createGraphics();
            g2d.setBackground(CLEAR_COLOR);
            g2d.clearRect(clip.x, clip.y, clip.width, clip.height);
            drawOnto(g2d, getScale(), clip);
            paintScanlines(g2d, clip.getSize());
            g2d.dispose();

            // Draw the surface background
            if (getCanvasBackground() != null) {
                ((Graphics2D) g).setPaint(getCanvasBackground());
                g.fillRect(clip.x, clip.y, clip.width, clip.height);
            }

            // Draw the paint image
            g.drawImage(buffer, clip.x, clip.y, null);
        }

        // DO NOT dispose the graphics context in this method.
    }

    private void paintScanlines(Graphics2D g, Dimension size) {
        double scale = getScale();

        if (scale > scanlineThreadhold) {
            g.setPaint(scanlineColor);
            g.setComposite(scanlineComposite);
            g.setStroke(new BasicStroke(1));

            for (int scanLine = 0; scanLine < size.height; scanLine += scale) {
                g.fillRect(0, scanLine, size.width, 1);
            }

            for (int scanLine = 0; scanLine < size.width; scanLine += scale) {
                g.fillRect(scanLine, 0, 1, size.height);
            }
        }
    }

}
