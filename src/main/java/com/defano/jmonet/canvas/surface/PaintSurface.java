package com.defano.jmonet.canvas.surface;

import com.defano.jmonet.canvas.observable.ObservableSurface;
import com.defano.jmonet.canvas.observable.SurfaceInteractionObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * A Swing component that can be painted on.
 */
public class PaintSurface extends JComponent implements CompositeSurface, ObservableSurface, KeyListener, MouseListener, MouseMotionListener, KeyEventDispatcher {

    private final static Color CLEAR_COLOR = new Color(0, 0, 0, 0);
    private final List<SurfaceInteractionObserver> interactionListeners = new ArrayList<>();
    private BufferedImage buffer;
    private Graphics2D bufferGraphics;

    private ScaledLayeredImage painting;

    public PaintSurface() {
        setOpaque(false);
        setLayout(null);
        setFocusable(true);

        addMouseListener(this);
        addMouseMotionListener(this);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                bufferGraphics = buffer.createGraphics();
            }
        });

        // Adding a KeyListener to this component won't always work the way the user expects; this is cheating, but
        // it assures paint tools get all key events, regardless of the component hierarchy we may be embedded in.
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
    }

    /**
     * Unregisters listeners and removes all child components making this surface available for garbage collection.
     */
    public void dispose() {
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

        if (isVisible()) {
            Graphics2D g2d = buffer.createGraphics();
            g2d.setBackground(CLEAR_COLOR);
            g2d.clearRect(clip.x, clip.y, clip.width, clip.height);
            painting.drawOnto(g2d, painting.getScale(), clip);
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
            thisListener.mouseClicked(e, painting.convertViewPointToModel(e.getPoint()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mousePressed(MouseEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners.toArray(new SurfaceInteractionObserver[]{})) {
            thisListener.mousePressed(e, painting.convertViewPointToModel(e.getPoint()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mouseReleased(MouseEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners.toArray(new SurfaceInteractionObserver[]{})) {
            thisListener.mouseReleased(e, painting.convertViewPointToModel(e.getPoint()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mouseEntered(MouseEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners.toArray(new SurfaceInteractionObserver[]{})) {
            thisListener.mouseEntered(e, painting.convertViewPointToModel(e.getPoint()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mouseExited(MouseEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners.toArray(new SurfaceInteractionObserver[]{})) {
            thisListener.mouseExited(e, painting.convertViewPointToModel(e.getPoint()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mouseDragged(MouseEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners.toArray(new SurfaceInteractionObserver[]{})) {
            thisListener.mouseDragged(e, painting.convertViewPointToModel(e.getPoint()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mouseMoved(MouseEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners.toArray(new SurfaceInteractionObserver[]{})) {
            thisListener.mouseMoved(e, painting.convertViewPointToModel(e.getPoint()));
        }
    }

    /**
     * Gets the painting displayed on this surface.
     *
     * @return The painting displayed on this surface.
     */
    public ScaledLayeredImage getPainting() {
        return painting;
    }

    /**
     * Sets the painting displayed on this surface.
     *
     * @param painting The painting to display.
     */
    public void setPainting(ScaledLayeredImage painting) {
        this.painting = painting;
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
                PaintSurface.this.keyTyped(e);
                break;
            case KeyEvent.KEY_PRESSED:
                PaintSurface.this.keyPressed(e);
                break;
            case KeyEvent.KEY_RELEASED:
                PaintSurface.this.keyReleased(e);
                break;
        }

        return false;
    }
}
