package com.defano.jmonet.canvas;

import com.defano.jmonet.canvas.observable.CanvasCommitObserver;
import com.defano.jmonet.canvas.observable.SurfaceInteractionObserver;
import com.defano.jmonet.canvas.surface.AbstractScrollableSurface;
import com.defano.jmonet.canvas.surface.PaintableSurface;
import com.defano.jmonet.tools.util.Geometry;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * A scrollable, Swing component that can be painted upon using the paint tools in {@link com.defano.jmonet.tools}. See
 * {@link JMonetCanvas} for a canvas with an undo/redo buffer.
 */
public abstract class AbstractPaintCanvas extends AbstractScrollableSurface implements PaintCanvas, ComponentListener {

    private final PaintableSurface surface = new PaintableSurface();

    private final java.util.List<CanvasCommitObserver> observers = new ArrayList<>();
    private final BehaviorSubject<Double> scaleSubject = BehaviorSubject.createDefault(1.0);
    private final BehaviorSubject<Integer> gridSpacingSubject = BehaviorSubject.createDefault(1);

    private BufferedImage scratch;      // Temporary buffer for changes not yet committed to the canvas

    /**
     * Creates a new AbstractPaintCanvas with a blank (transparent) initial image displayed in it.
     */
    public AbstractPaintCanvas() {
        this.surface.setPainting(this);
        setSurface(surface);
    }

    /**
     * Marks this component safe for garbage collection; removes registered listeners and components.
     */
    public void dispose() {
        scaleSubject.onComplete();
        gridSpacingSubject.onComplete();
        observers.clear();
        surface.dispose();
        surface.removeComponentListener(this);
        setTransferHandler(null);
    }

    /** {@inheritDoc} */
    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        surface.setPreferredSize(new Dimension((int) (width * scaleSubject.getValue()), (int) (height * scaleSubject.getValue())));

        BufferedImage newScratch = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics newScratchGraphics = newScratch.getGraphics();
        newScratchGraphics.drawImage(getScratchImage(), 0, 0, null);
        setScratchImage(newScratch);

        newScratchGraphics.dispose();

        surface.addComponentListener(this);
        invalidateCanvas();
    }

    /** {@inheritDoc} */
    @Override
    public BufferedImage[] getPaintLayers() {
        return new BufferedImage[]{getCanvasImage(), getScratchImage()};
    }

    /** {@inheritDoc} */
    @Override
    public void clearCanvas() {
        Graphics2D g2 = (Graphics2D) getScratchImage().getGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();

        commit(new ChangeSet(getScratchImage(), AlphaComposite.getInstance(AlphaComposite.DST_OUT, 1.0f)));
    }

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
    @Override
    public Point convertPointToImage(Point p) {
        return new Point(translateX(p.x), translateY(p.y));
    }

    private int translateX(int x) {
        int gridSpacing = getGridSpacingObservable().blockingFirst();
        double scale = getScaleObservable().blockingFirst();

        x = Geometry.round(x, (int) (gridSpacing * scale));
        return (int) (x / scale);
    }

    private int translateY(int y) {
        int gridSpacing = getGridSpacingObservable().blockingFirst();
        double scale = getScaleObservable().blockingFirst();

        y = Geometry.round(y, (int) (gridSpacing * scale));
        return (int) (y / scale);
    }

    /** {@inheritDoc} */
    @Override
    public BufferedImage getScratchImage() {
        return scratch;
    }

    /** {@inheritDoc} */
    @Override
    public void setScratchImage(BufferedImage image) {
        this.scratch = image;
    }

    /** {@inheritDoc} */
    @Override
    public void commit() {
        commit(new ChangeSet(getScratchImage()));
    }

    /** {@inheritDoc} */
    @Override
    public void addComponent(Component component) {
        surface.addComponent(component);
    }

    /** {@inheritDoc} */
    @Override
    public void removeComponent(Component component) {
        surface.removeComponent(component);
    }

    /** {@inheritDoc} */
    @Override
    public void addSurfaceInteractionObserver(SurfaceInteractionObserver listener) {
        surface.addSurfaceInteractionObserver(listener);
    }

    /** {@inheritDoc} */
    @Override
    public boolean removeSurfaceInteractionObserver(SurfaceInteractionObserver listener) {
        return surface.removeSurfaceInteractionObserver(listener);
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
    public void setGridSpacing(int grid) {
        this.gridSpacingSubject.onNext(grid);
    }

    /** {@inheritDoc} */
    @Override
    public Observable<Integer> getGridSpacingObservable() {
        return gridSpacingSubject;
    }

    /** {@inheritDoc} */
    @Override
    public void setScale(double scale) {
        this.scaleSubject.onNext(scale);
        surface.setPreferredSize(new Dimension((int) (getWidth() * scale), (int) (getHeight() * scale)));
        surface.revalidate();

        invalidateCanvas();
    }

    /** {@inheritDoc} */
    @Override
    public void addCanvasCommitObserver(CanvasCommitObserver observer) {
        observers.add(observer);
    }

    /** {@inheritDoc} */
    @Override
    public boolean removeCanvasCommitObserver(CanvasCommitObserver observer) {
        return observers.remove(observer);
    }

    protected void fireCanvasCommitObservers(PaintCanvas canvas, ChangeSet changeSet, BufferedImage canvasImage) {
        for (CanvasCommitObserver thisObserver : observers) {
            thisObserver.onCommit(canvas, changeSet, canvasImage);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void invalidateCanvas() {
        repaint();
    }

    /** {@inheritDoc} */
    @Override
    public void componentResized(ComponentEvent e) {
        updateScroll();
    }

    /** {@inheritDoc} */
    @Override
    public void componentMoved(ComponentEvent e) {
        // Nothing to do
    }

    /** {@inheritDoc} */
    @Override
    public void componentShown(ComponentEvent e) {
        // Nothing to do
    }

    /** {@inheritDoc} */
    @Override
    public void componentHidden(ComponentEvent e) {
        // Nothing to do
    }
}
