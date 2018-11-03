package com.defano.jmonet.canvas;

import com.defano.jmonet.canvas.layer.ImageLayer;
import com.defano.jmonet.canvas.layer.ImageLayerSet;
import com.defano.jmonet.canvas.observable.CanvasCommitObserver;
import com.defano.jmonet.canvas.surface.AbstractPaintSurface;
import com.defano.jmonet.tools.util.Geometry;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * A scrollable, Swing component that can be painted upon using the paint tools in {@link com.defano.jmonet.tools}. See
 * {@link JMonetCanvas} for a canvas with an undo/redo buffer.
 */
public abstract class AbstractPaintCanvas extends AbstractPaintSurface implements PaintCanvas {

    private final ArrayList<CanvasCommitObserver> observers = new ArrayList<>();
    private final BehaviorSubject<Integer> gridSpacingSubject = BehaviorSubject.createDefault(1);
    private final Scratch scratch;
    private Paint canvasBackground;

    public AbstractPaintCanvas(Dimension dimension) {
        super(dimension);
        scratch = new Scratch(dimension.width, dimension.height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCanvasSize(Dimension dimension) {
        setSurfaceDimension(dimension);

        if (scratch != null) {
            scratch.setSize(dimension.width, dimension.height);
        }

        repaint();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getCanvasSize() {
        return getSurfaceDimension();
    }

    /**
     * Marks this component safe for garbage collection; removes registered listeners and components.
     */
    public void dispose() {
        super.dispose();

        gridSpacingSubject.onComplete();
        observers.clear();
        setTransferHandler(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImageLayer[] getImageLayers() {
        return new ImageLayer[]{
                new ImageLayer(getCanvasImage()),
                getScratch().getRemoveScratchLayer(),
                getScratch().getAddScratchLayer()};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearCanvas() {
        Rectangle clear = new Rectangle(new Point(), getCanvasSize());
        Graphics2D g2 = scratch.getRemoveScratchGraphics(null, clear);
        g2.setColor(Color.WHITE);
        g2.fill(clear);

        commit(scratch.getLayerSet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Paint getCanvasBackground() {
        return canvasBackground;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCanvasBackground(Paint paint) {
        this.canvasBackground = paint;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point convertViewPointToModel(Point p) {
        Point error = getScrollError();
        int gridSpacing = getGridSpacingObservable().blockingFirst();
        double scale = getScaleObservable().blockingFirst();

        int x = p.x - error.x;
        x = Geometry.round(x, (int) (gridSpacing * scale));
        x = (int) (x / scale);

        int y = p.y - error.y;
        y = Geometry.round(y, (int) (gridSpacing * scale));
        y = (int) (y / scale);

        return new Point(x, y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point convertModelPointToView(Point p) {
        Point error = getScrollError();
        double scale = getScaleObservable().blockingFirst();

        int x = (int) (p.x * scale) + error.x;
        int y = (int) (p.y * scale) + error.y;

        return new Point(x, y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Scratch getScratch() {
        return scratch;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit() {
        commit(scratch.getLayerSet());
        repaint();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGridSpacing(int grid) {
        this.gridSpacingSubject.onNext(grid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Observable<Integer> getGridSpacingObservable() {
        return gridSpacingSubject;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setScale(double scale) {
        super.setScale(scale);
        repaint();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addCanvasCommitObserver(CanvasCommitObserver observer) {
        observers.add(observer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeCanvasCommitObserver(CanvasCommitObserver observer) {
        return observers.remove(observer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point getScrollError() {
        Rectangle viewRect = getSurfaceScrollController().getScrollRect();
        double scale = getScale();

        return new Point((int) (viewRect.x % scale), (int) (viewRect.y % scale));
    }



    protected void fireCanvasCommitObservers(PaintCanvas canvas, ImageLayerSet imageLayerSet, BufferedImage canvasImage) {
        for (CanvasCommitObserver thisObserver : observers) {
            thisObserver.onCommit(canvas, imageLayerSet, canvasImage);
        }
    }

}
