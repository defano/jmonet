package com.defano.jmonet.tools.base;

import com.defano.jmonet.canvas.ChangeSet;
import com.defano.jmonet.canvas.PaintCanvas;
import com.defano.jmonet.canvas.observable.CanvasCommitObserver;
import com.defano.jmonet.canvas.observable.SurfaceInteractionObserver;
import com.defano.jmonet.model.PaintToolType;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Optional;

/**
 * A base tool class holding common attribute providers (like stroke, fill and font), plus empty, template methods for
 * keyboard and mouse events.
 */
public abstract class PaintTool implements SurfaceInteractionObserver, CanvasCommitObserver {

    private PaintCanvas canvas;
    private final PaintToolType type;
    private AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
    private Cursor toolCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
    private int constrainedAngle = 15;

    private Observable<Stroke> strokeObservable = BehaviorSubject.createDefault(new BasicStroke(2));
    private Observable<Paint> strokePaintObservable = BehaviorSubject.createDefault(Color.BLACK);
    private Observable<Optional<Paint>> fillPaintObservable = BehaviorSubject.createDefault(Optional.empty());
    private Observable<Integer> shapeSidesObservable = BehaviorSubject.createDefault(5);
    private Observable<Font> fontObservable = BehaviorSubject.createDefault(new Font("Courier", Font.PLAIN, 14));
    private Observable<Color> fontColorObservable = BehaviorSubject.createDefault(Color.BLACK);

    public PaintTool(PaintToolType type) {
        this.type = type;
    }

    /**
     * Activates the tool on a given canvas.
     *
     * A paint tool does not "paint" on the canvas until it is activated. Typically, only one tool is active on
     * a canvas at any given time, but there is no technical limitation preventing multiple tools from being active
     * at once.
     *
     * Use {@link #deactivate()} to stop this tool from painting on the canvas.
     *
     * @param canvas The paint canvas on which to activate the tool.
     */
    public void activate (PaintCanvas canvas) {
        this.canvas = canvas;
        this.canvas.addSurfaceInteractionObserver(this);
        this.canvas.setCursor(toolCursor);
    }

    /**
     * Deactivates the tool on the canvas. A deactivated tool no longer affects the canvas and all listeners / observers
     * are un-subscribed making the tool available for garbage collection.
     */
    public void deactivate() {
        if (canvas != null) {
            canvas.removeSurfaceInteractionObserver(this);
            canvas.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /** {@inheritDoc} */
    @Override
    public void onCommit(PaintCanvas canvas, ChangeSet changeSet, BufferedImage canvasImage) {
        // Nothing to do
    }

    public AlphaComposite getComposite() {
        return composite;
    }

    public void setComposite(AlphaComposite composite) {
        this.composite = composite;
    }

    public PaintToolType getToolType() {
        return this.type;
    }

    protected PaintCanvas getCanvas() {
        return canvas;
    }

    public void setFontColorObservable(Observable<Color> fontColorObservable) {
        this.fontColorObservable = fontColorObservable;
    }

    public void setStrokePaintObservable(Observable<Paint> strokePaintObservable) {
        if (strokePaintObservable != null) {
            this.strokePaintObservable = strokePaintObservable;
        }
    }

    public void setStrokeObservable(Observable<Stroke> strokeObservable) {
        if (strokeObservable != null) {
            this.strokeObservable = strokeObservable;
        }
    }

    public void setShapeSidesObservable(Observable<Integer> shapeSidesObservable) {
        if (shapeSidesObservable != null) {
            this.shapeSidesObservable = shapeSidesObservable;
        }
    }

    public void setFontObservable(Observable<Font> fontObservable) {
        if (fontObservable != null) {
            this.fontObservable = fontObservable;
        }
    }

    public void setFillPaintObservable(Observable<Optional<Paint>> fillPaintObservable) {
        this.fillPaintObservable = fillPaintObservable;
    }

    public Stroke getStroke() {
        return strokeObservable.blockingFirst();
    }

    public Optional<Paint> getFillPaint() {
        return fillPaintObservable.blockingFirst();
    }

    public Font getFont() {
        return fontObservable.blockingFirst();
    }

    public int getShapeSides() {
        return shapeSidesObservable.blockingFirst() < 3 ? 3 :
                shapeSidesObservable.blockingFirst() > 20 ? 20 :
                shapeSidesObservable.blockingFirst();
    }

    public Paint getStrokePaint() {
        return strokePaintObservable.blockingFirst();
    }

    public Color getFontColor() {
        return fontColorObservable.blockingFirst();
    }

    public Observable<Optional<Paint>> getFillPaintObservable() {
        return fillPaintObservable;
    }

    public Observable<Stroke> getStrokeObservable() {
        return strokeObservable;
    }

    public Observable<Paint> getStrokePaintObservable() {
        return strokePaintObservable;
    }

    public Observable<Integer> getShapeSidesObservable() {
        return shapeSidesObservable;
    }

    public Observable<Font> getFontObservable() {
        return fontObservable;
    }

    public Observable<Color> getFontColorObservable() {
        return fontColorObservable;
    }

    public Cursor getToolCursor() {
        return toolCursor;
    }

    public void setToolCursor(Cursor toolCursor) {
        this.toolCursor = toolCursor;

        if (this.canvas != null) {
            this.canvas.setCursor(toolCursor);
        }
    }

    public int getConstrainedAngle() {
        return constrainedAngle;
    }

    public void setConstrainedAngle(int constrainedAngle) {
        this.constrainedAngle = constrainedAngle;
    }

    /** {@inheritDoc} */
    @Override
    public void mouseClicked(MouseEvent e, Point imageLocation) {
        // Nothing to do; override in subclasses.
    }

    /** {@inheritDoc} */
    @Override
    public void mousePressed(MouseEvent e, Point imageLocation) {
        // Nothing to do; override in subclasses.
    }

    /** {@inheritDoc} */
    @Override
    public void mouseReleased(MouseEvent e, Point imageLocation) {
        // Nothing to do; override in subclasses.
    }

    /** {@inheritDoc} */
    @Override
    public void mouseEntered(MouseEvent e, Point imageLocation) {
        // Nothing to do; override in subclasses.
    }

    /** {@inheritDoc} */
    @Override
    public void mouseExited(MouseEvent e, Point imageLocation) {
        // Nothing to do; override in subclasses.
    }

    /** {@inheritDoc} */
    @Override
    public void mouseDragged(MouseEvent e, Point imageLocation) {
        // Nothing to do; override in subclasses.
    }

    /** {@inheritDoc} */
    @Override
    public void mouseMoved(MouseEvent e, Point imageLocation) {
        // Nothing to do; override in subclasses.
    }

    /** {@inheritDoc} */
    @Override
    public void keyTyped(KeyEvent e) {
        // Nothing to do; override in subclasses.
    }

    /** {@inheritDoc} */
    @Override
    public void keyPressed(KeyEvent e) {
        // Nothing to do; override in subclasses.
    }

    /** {@inheritDoc} */
    @Override
    public void keyReleased(KeyEvent e) {
        // Nothing to do; override in subclasses.
    }
}
