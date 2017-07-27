package com.defano.jmonet.canvas;

import com.defano.jmonet.canvas.observable.CanvasCommitObserver;
import com.defano.jmonet.canvas.observable.SurfaceInteractionObserver;
import com.defano.jmonet.canvas.surface.AbstractScrollableSurface;
import com.defano.jmonet.canvas.surface.PaintableSurface;
import com.defano.jmonet.model.Provider;
import com.defano.jmonet.tools.util.Geometry;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * A scrollable, Swing component that can be painted upon using the paint tools in {@link com.defano.jmonet.tools}. See
 * {@link UndoablePaintCanvas} for a canvas with an undo/redo buffer.
 */
public abstract class AbstractPaintCanvas extends AbstractScrollableSurface implements PaintCanvas, ComponentListener {

    private final PaintableSurface surface = new PaintableSurface();

    private final java.util.List<CanvasCommitObserver> observers = new ArrayList<>();
    private final Provider<Double> scale = new Provider<>(1.0);
    private final Provider<Integer> gridSpacing = new Provider<>(1);

    private BufferedImage scratch;      // Temporary buffer for changes not yet committed to the canvas

    /**
     * Creates a new AbstractPaintCanvas with a blank (transparent) initial image displayed in it.
     */
    public AbstractPaintCanvas() {
        this.surface.setPainting(this);
        setSurface(surface);
    }

    /**
     * Invoke to mark this component safe for garbage collection; removes registered listeners and components.
     */
    public void dispose() {
        scale.deleteObservers();
        gridSpacing.deleteObservers();
        observers.clear();
        surface.dispose();
        surface.removeComponentListener(this);
        setTransferHandler(null);
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        surface.setPreferredSize(new Dimension((int) (width * scale.get()), (int) (height * scale.get())));

        BufferedImage newScratch = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics newScratchGraphics = newScratch.getGraphics();
        newScratchGraphics.drawImage(getScratchImage(), 0, 0, null);
        setScratchImage(newScratch);

        newScratchGraphics.dispose();

        surface.addComponentListener(this);
        invalidateCanvas();
    }

    @Override
    public BufferedImage[] getPaintLayers() {
        return new BufferedImage[]{getCanvasImage(), getScratchImage()};
    }

    @Override
    public void clearCanvas() {
        Graphics2D g2 = (Graphics2D) getScratchImage().getGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();

        commit(new ChangeSet(getScratchImage(), AlphaComposite.getInstance(AlphaComposite.DST_OUT, 1.0f)));
    }

    @Override
    public void clearScratch() {
        scratch = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        invalidateCanvas();
    }

    protected void applyImage(BufferedImage source, BufferedImage destination, AlphaComposite composite) {
        Graphics2D g2d = (Graphics2D) destination.getGraphics();
        g2d.setComposite(composite);
        g2d.drawImage(source, 0, 0, null);
        g2d.dispose();
    }

    protected void applyChangeSet(ChangeSet changeSet, BufferedImage destination) {
        Graphics2D g2d = (Graphics2D) destination.getGraphics();

        for (int index = 0; index < changeSet.size(); index++) {
            g2d.setComposite(changeSet.getComposite(index));
            g2d.drawImage(changeSet.getImage(index), 0, 0, null);
        }

        g2d.dispose();
    }

    @Override
    public Point convertPointToImage(Point p) {
        return new Point(translateX(p.x), translateY(p.y));
    }

    private int translateX(int x) {
        int gridSpacing = getGridSpacingProvider().get();
        double scale = getScaleProvider().get();

        x = Geometry.round(x, (int) (gridSpacing * scale));
        return (int) (x / scale);
    }

    private int translateY(int y) {
        int gridSpacing = getGridSpacingProvider().get();
        double scale = getScaleProvider().get();

        y = Geometry.round(y, (int) (gridSpacing * scale));
        return (int) (y / scale);
    }

    @Override
    public BufferedImage getScratchImage() {
        return scratch;
    }

    @Override
    public void setScratchImage(BufferedImage image) {
        this.scratch = image;
    }

    public void commit() {
        commit(new ChangeSet(getScratchImage()));
    }

    @Override
    public void addComponent(Component component) {
        surface.addComponent(component);
    }

    @Override
    public void removeComponent(Component component) {
        surface.removeComponent(component);
    }

    @Override
    public void addSurfaceInteractionObserver(SurfaceInteractionObserver listener) {
        surface.addSurfaceInteractionObserver(listener);
    }

    @Override
    public boolean removeSurfaceInteractionObserver(SurfaceInteractionObserver listener) {
        return surface.removeSurfaceInteractionObserver(listener);
    }

    @Override
    public double getScale() {
        return scale.get();
    }

    @Override
    public Provider<Double> getScaleProvider() {
        return scale;
    }

    @Override
    public void setGridSpacing(int grid) {
        this.gridSpacing.set(grid);
    }

    @Override
    public Provider<Integer> getGridSpacingProvider() {
        return gridSpacing;
    }

    @Override
    public void setScale(double scale) {
        this.scale.set(scale);
        surface.setPreferredSize(new Dimension((int) (getWidth() * scale), (int) (getHeight() * scale)));
        surface.revalidate();

        invalidateCanvas();
    }

    @Override
    public void addCanvasCommitObserver(CanvasCommitObserver observer) {
        observers.add(observer);
    }

    @Override
    public boolean removeCanvasCommitObserver(CanvasCommitObserver observer) {
        return observers.remove(observer);
    }

    protected void fireCanvasCommitObservers(PaintCanvas canvas, ChangeSet changeSet, BufferedImage canvasImage) {
        for (CanvasCommitObserver thisObserver : observers) {
            thisObserver.onCommit(canvas, changeSet, canvasImage);
        }
    }

    @Override
    public void invalidateCanvas() {
        repaint();
    }

    @Override
    public void componentResized(ComponentEvent e) {
        updateScroll();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        // Nothing to do
    }

    @Override
    public void componentShown(ComponentEvent e) {
        // Nothing to do
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        // Nothing to do
    }
}
