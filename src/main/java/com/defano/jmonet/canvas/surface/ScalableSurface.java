package com.defano.jmonet.canvas.surface;

import com.defano.jmonet.canvas.observable.ObservableSurface;
import com.defano.jmonet.canvas.observable.SurfaceInteractionObserver;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * A component that paints a layered image at scale.
 */
public abstract class ScalableSurface extends ScrollableSurface implements
        Disposable, Scalable, ScaledLayeredImage, SwingSurface, ObservableSurface, KeyListener, MouseListener,
        MouseMotionListener, KeyEventDispatcher {

    private Dimension surfaceDimensions = new Dimension();
    private final static Color CLEAR_COLOR = new Color(0, 0, 0, 0);
    private final List<SurfaceInteractionObserver> interactionListeners = new ArrayList<>();
    private BufferedImage buffer;
    private final BehaviorSubject<Double> scaleSubject = BehaviorSubject.createDefault(1.0);

    /**
     * Creates a ScalableSurface with the specified dimensions.
     *
     * @param surfaceDimensions The size of the surface.
     */
    public ScalableSurface(Dimension surfaceDimensions) {
        setOpaque(false);
        setLayout(null);
        setFocusable(true);

        addMouseListener(this);
        addMouseMotionListener(this);

        // Adding a KeyListener to this component won't always work the way the user expects; this is cheating, but
        // it assures paint tools get all key events, regardless of the component hierarchy we may be embedded in.
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);

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
     * Gets the dimensions of the surface multiplied by the surface's scale factor.
     *
     * @return The scaled surface dimensions.
     */
    public Dimension getScaledSurfaceDimension() {
        return getScaledDimension(surfaceDimensions);
    }

    /**
     * Specifies the un-scaled size of this painting surface. This determines the size of the image (document) that will
     * be painted by a user.
     *
     * @param surfaceDimensions  The dimensions of the painting surface
     */
    public void setSurfaceDimension(Dimension surfaceDimensions) {
        this.surfaceDimensions = new Dimension(surfaceDimensions.width, surfaceDimensions.height);
        Dimension scaledDimension = getScaledDimension(surfaceDimensions);

        buffer = new BufferedImage(scaledDimension.width, scaledDimension.height, BufferedImage.TYPE_INT_ARGB);

        super.setMaximumSize(scaledDimension);
        super.setPreferredSize(scaledDimension);
        super.setSize(scaledDimension);
        super.invalidate();
    }

    /** {@inheritDoc} */
    @Override
    public double getScale() {
        return scaleSubject.getValue();
    }

    /** {@inheritDoc} */
    @Override
    public Observable<Double> getScaleObservable() {
        return scaleSubject;
    }

    /** {@inheritDoc} */
    @Override
    public void setScale(double scale) {
        scaleSubject.onNext(scale);
        Dimension scaledDimension = getScaledDimension(surfaceDimensions);

        buffer = new BufferedImage(scaledDimension.width, scaledDimension.height, BufferedImage.TYPE_INT_ARGB);

        super.setPreferredSize(scaledDimension);
        super.setMaximumSize(scaledDimension);
        super.invalidate();
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

        Rectangle clip = g.getClipBounds().intersection(new Rectangle(0, 0, buffer.getWidth(), buffer.getHeight()));

        if (!clip.isEmpty() && isVisible() && buffer != null) {
            Graphics2D g2d = buffer.createGraphics();
            g2d.setBackground(CLEAR_COLOR);
            g2d.clearRect(clip.x, clip.y, clip.width, clip.height);
            drawOnto(g2d, getScale(), clip);
            g2d.dispose();
            g.drawImage(buffer.getSubimage(clip.x, clip.y, clip.width, clip.height), clip.x, clip.y, null);
        }
        
        // DO NOT dispose the graphics context in this method.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addSurfaceInteractionObserver(SurfaceInteractionObserver listener) {
        interactionListeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeSurfaceInteractionObserver(SurfaceInteractionObserver listener) {
        return interactionListeners.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void keyTyped(KeyEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners.toArray(new SurfaceInteractionObserver[]{})) {
            thisListener.keyTyped(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void keyPressed(KeyEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners.toArray(new SurfaceInteractionObserver[]{})) {
            thisListener.keyPressed(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void keyReleased(KeyEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners.toArray(new SurfaceInteractionObserver[]{})) {
            thisListener.keyReleased(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mouseClicked(MouseEvent e) {
        requestFocus();

        for (SurfaceInteractionObserver thisListener : interactionListeners.toArray(new SurfaceInteractionObserver[]{})) {
            thisListener.mouseClicked(e, convertViewPointToModel(e.getPoint()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mousePressed(MouseEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners.toArray(new SurfaceInteractionObserver[]{})) {
            thisListener.mousePressed(e, convertViewPointToModel(e.getPoint()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mouseReleased(MouseEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners.toArray(new SurfaceInteractionObserver[]{})) {
            thisListener.mouseReleased(e, convertViewPointToModel(e.getPoint()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mouseEntered(MouseEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners.toArray(new SurfaceInteractionObserver[]{})) {
            thisListener.mouseEntered(e, convertViewPointToModel(e.getPoint()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mouseExited(MouseEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners.toArray(new SurfaceInteractionObserver[]{})) {
            thisListener.mouseExited(e, convertViewPointToModel(e.getPoint()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mouseDragged(MouseEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners.toArray(new SurfaceInteractionObserver[]{})) {
            thisListener.mouseDragged(e, convertViewPointToModel(e.getPoint()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mouseMoved(MouseEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners.toArray(new SurfaceInteractionObserver[]{})) {
            thisListener.mouseMoved(e, convertViewPointToModel(e.getPoint()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addComponent(Component component) {
        add(component);

        revalidate();
        repaint();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeComponent(Component component) {
        remove(component);

        revalidate();
        repaint();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        switch (e.getID()) {
            case KeyEvent.KEY_TYPED:
                ScalableSurface.this.keyTyped(e);
                break;
            case KeyEvent.KEY_PRESSED:
                ScalableSurface.this.keyPressed(e);
                break;
            case KeyEvent.KEY_RELEASED:
                ScalableSurface.this.keyReleased(e);
                break;
        }

        return false;
    }
}
