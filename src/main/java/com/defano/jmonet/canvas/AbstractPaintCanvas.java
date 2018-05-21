package com.defano.jmonet.canvas;

import com.defano.jmonet.canvas.observable.CanvasCommitObserver;
import com.defano.jmonet.canvas.observable.SurfaceInteractionObserver;
import com.defano.jmonet.canvas.surface.AbstractScrollablePaintSurface;
import com.defano.jmonet.canvas.layer.ImageLayer;
import com.defano.jmonet.canvas.layer.ImageLayerSet;
import com.defano.jmonet.canvas.surface.PaintSurface;
import com.defano.jmonet.tools.util.Geometry;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * A scrollable, Swing component that can be painted upon using the paint tools in {@link com.defano.jmonet.tools}. See
 * {@link JMonetCanvas} for a canvas with an undo/redo buffer.
 */
public abstract class AbstractPaintCanvas extends AbstractScrollablePaintSurface implements PaintCanvas, ComponentListener {

    private final PaintSurface surface = new PaintSurface();

    private final java.util.List<CanvasCommitObserver> observers = new ArrayList<>();
    private final BehaviorSubject<Double> scaleSubject = BehaviorSubject.createDefault(1.0);
    private final BehaviorSubject<Integer> gridSpacingSubject = BehaviorSubject.createDefault(1);

    private Scratch scratch = new Scratch();

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
        scratch.resize(width, height);
        surface.addComponentListener(this);
        invalidateCanvas();
    }

    /** {@inheritDoc} */
    @Override
    public ImageLayer[] getImageLayers() {
        return new ImageLayer[] {
                new ImageLayer(getCanvasImage()),
                getScratch().getRemoveScratchLayer(),
                getScratch().getAddScratchLayer()};
    }

    /** {@inheritDoc} */
    @Override
    public void clearCanvas() {
        Graphics2D g2 = scratch.getRemoveScratchGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());

        commit(scratch.getLayerSet());
    }

    @Override
    public Color getCanvasColor() {
        Color background = (Color) UIManager.getLookAndFeelDefaults().get("Panel.background");
        return background == null ? Color.WHITE : background;
    }

    /** {@inheritDoc} */
    @Override
    public Point convertViewPointToModel(Point p) {
        return new Point(translateX(p.x), translateY(p.y));
    }

    /** {@inheritDoc} */
    @Override
    public Scratch getScratch() {
        return scratch;
    }

    /** {@inheritDoc} */
    @Override
    public void commit() {
        commit(scratch.getLayerSet());
        invalidateCanvas();
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

    protected void fireCanvasCommitObservers(PaintCanvas canvas, ImageLayerSet imageLayerSet, BufferedImage canvasImage) {
        for (CanvasCommitObserver thisObserver : observers) {
            thisObserver.onCommit(canvas, imageLayerSet, canvasImage);
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
}
