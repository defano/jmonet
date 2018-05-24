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
    private BufferedImage buffer;

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

        buffer = new BufferedImage(scaledDimension.width, scaledDimension.height, BufferedImage.TYPE_INT_ARGB);

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
        buffer = new BufferedImage(scaledDimension.width, scaledDimension.height, BufferedImage.TYPE_INT_ARGB);

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

        Rectangle clipBounds = g.getClipBounds();
        Rectangle imageBounds = new Rectangle(0, 0, buffer.getWidth(), buffer.getHeight());

        Rectangle clip = clipBounds == null ?
                imageBounds :
                imageBounds.intersection(g.getClipBounds());

        if (!clip.isEmpty() && isVisible() && buffer != null) {

            // Draw visible portion of this surface's image onto a graphics buffer
            Graphics2D g2d = buffer.createGraphics();
            g2d.setBackground(CLEAR_COLOR);
            g2d.clearRect(clip.x, clip.y, clip.width, clip.height);
            drawOnto(g2d, getScale(), clip);
            g2d.dispose();

            // ... then draw that image on the component's graphics context
            g.drawImage(buffer.getSubimage(clip.x, clip.y, clip.width, clip.height), clip.x, clip.y, null);
        }

        // DO NOT dispose the graphics context in this method.
    }

}
