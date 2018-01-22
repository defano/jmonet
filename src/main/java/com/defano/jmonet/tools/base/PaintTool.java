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

    private Observable<Stroke> strokeProvider = BehaviorSubject.createDefault(new BasicStroke(2));
    private Observable<Paint> strokePaintProvider = BehaviorSubject.createDefault(Color.BLACK);
    private Observable<Optional<Paint>> fillPaintProvider = BehaviorSubject.createDefault(Optional.empty());
    private Observable<Integer> shapeSidesProvider = BehaviorSubject.createDefault(5);
    private Observable<Font> fontProvider = BehaviorSubject.createDefault(new Font("Courier", Font.PLAIN, 14));
    private Observable<Color> fontColorProvider = BehaviorSubject.createDefault(Color.BLACK);

    public PaintTool(PaintToolType type) {
        this.type = type;
    }

    public void activate (PaintCanvas canvas) {
        this.canvas = canvas;
        this.canvas.addSurfaceInteractionObserver(this);
        this.canvas.setCursor(toolCursor);
    }

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

    public void setFontColorProvider(Observable<Color> fontColorProvider) {
        this.fontColorProvider = fontColorProvider;
    }

    public void setStrokePaintProvider(Observable<Paint> strokePaintProvider) {
        if (strokePaintProvider != null) {
            this.strokePaintProvider = strokePaintProvider;
        }
    }

    public void setStrokeProvider(Observable<Stroke> strokeProvider) {
        if (strokeProvider != null) {
            this.strokeProvider = strokeProvider;
        }
    }

    public void setShapeSidesProvider(Observable<Integer> shapeSidesProvider) {
        if (shapeSidesProvider != null) {
            this.shapeSidesProvider = shapeSidesProvider;
        }
    }

    public void setFontProvider(Observable<Font> fontProvider) {
        if (fontProvider != null) {
            this.fontProvider = fontProvider;
        }
    }

    public void setFillPaintProvider(Observable<Optional<Paint>> fillPaintProvider) {
        this.fillPaintProvider = fillPaintProvider;
    }

    public Stroke getStroke() {
        return strokeProvider.blockingFirst();
    }

    public Optional<Paint> getFillPaint() {
        return fillPaintProvider.blockingFirst();
    }

    public Font getFont() {
        return fontProvider.blockingFirst();
    }

    public int getShapeSides() {
        return shapeSidesProvider.blockingFirst() < 3 ? 3 :
                shapeSidesProvider.blockingFirst() > 20 ? 20 :
                shapeSidesProvider.blockingFirst();
    }

    public Paint getStrokePaint() {
        return strokePaintProvider.blockingFirst();
    }

    public Color getFontColor() {
        return fontColorProvider.blockingFirst();
    }

    public Observable<Optional<Paint>> getFillPaintProvider() {
        return fillPaintProvider;
    }

    public Observable<Stroke> getStrokeProvider() {
        return strokeProvider;
    }

    public Observable<Paint> getStrokePaintProvider() {
        return strokePaintProvider;
    }

    public Observable<Integer> getShapeSidesProvider() {
        return shapeSidesProvider;
    }

    public Observable<Font> getFontProvider() {
        return fontProvider;
    }

    public Observable<Color> getFontColorProvider() {
        return fontColorProvider;
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
