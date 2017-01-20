package com.defano.jmonet.canvas.surface;

import com.defano.jmonet.canvas.SurfaceInteractionObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class PaintSurface extends JPanel implements Surface, KeyListener, MouseListener, MouseMotionListener {

    private ScalableScratchDrawable drawable;
    private final List<SurfaceInteractionObserver> interactionListeners = new ArrayList<>();

    public PaintSurface() {
        setOpaque(false);
        setLayout(null);

        addMouseListener(this);
        addMouseMotionListener(this);

        // Adding a KeyListener to this component won't always work the way the user expects; this is cheating, but
        // assures paint tools get all key events, regardless of the component hierarchy we may be embedded in.
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(e -> {
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
                });
    }

    public void setDrawable(ScalableScratchDrawable drawable) {
        this.drawable = drawable;
    }

    public ScalableScratchDrawable getDrawable() {
        return drawable;
    }

    public void paintComponent(Graphics g) {
        if (isVisible()) {
            double scale = drawable.getScale();

            Graphics2D g2d = (Graphics2D) g;
            BufferedImage canvasImage = drawable.getCanvasImage();
            BufferedImage scratchImage = drawable.getScratchImage();

            g2d.drawImage(canvasImage, 0, 0, (int) (canvasImage.getWidth() * scale), (int) (canvasImage.getHeight() * scale), null);
            g2d.drawImage(scratchImage, 0, 0, (int) (scratchImage.getWidth() * scale), (int) (scratchImage.getHeight() * scale), null);
        }
    }

    @Override
    public void addComponent(Component component) {
        add(component);
        revalidate();
        repaint();
    }

    @Override
    public void removeComponent(Component component) {
        remove(component);
        revalidate();
        repaint();
    }

    @Override
    public void addSurfaceInteractionObserver(SurfaceInteractionObserver listener) {
        interactionListeners.add(listener);
    }

    @Override
    public boolean removeSurfaceInteractionObserver(SurfaceInteractionObserver listener) {
        return interactionListeners.remove(listener);
    }

    @Override
    public final void keyTyped(KeyEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners) {
            thisListener.keyTyped(e);
        }
    }

    @Override
    public final void keyPressed(KeyEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners) {
            thisListener.keyPressed(e);
        }
    }

    @Override
    public final void keyReleased(KeyEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners) {
            thisListener.keyReleased(e);
        }
    }

    @Override
    public final void mouseClicked(MouseEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners) {
            thisListener.mouseClicked(e, drawable.convertPointToImage(e.getPoint()));
        }
    }

    @Override
    public final void mousePressed(MouseEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners) {
            thisListener.mousePressed(e, drawable.convertPointToImage(e.getPoint()));
        }
    }

    @Override
    public final void mouseReleased(MouseEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners) {
            thisListener.mouseReleased(e, drawable.convertPointToImage(e.getPoint()));
        }
    }

    @Override
    public final void mouseEntered(MouseEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners) {
            thisListener.mouseEntered(e, drawable.convertPointToImage(e.getPoint()));
        }
    }

    @Override
    public final void mouseExited(MouseEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners) {
            thisListener.mouseExited(e, drawable.convertPointToImage(e.getPoint()));
        }
    }

    @Override
    public final void mouseDragged(MouseEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners) {
            thisListener.mouseDragged(e, drawable.convertPointToImage(e.getPoint()));
        }
    }

    @Override
    public final void mouseMoved(MouseEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners) {
            thisListener.mouseMoved(e, drawable.convertPointToImage(e.getPoint()));
        }
    }
}
