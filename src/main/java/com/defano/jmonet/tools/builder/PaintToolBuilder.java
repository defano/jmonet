package com.defano.jmonet.tools.builder;

import com.defano.jmonet.canvas.JFXPaintCanvasNode;
import com.defano.jmonet.canvas.PaintCanvas;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.AbstractBoundsTool;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.awt.*;
import java.util.Optional;

/**
 * A utility for building paint tools.
 */
public class PaintToolBuilder {

    private final PaintToolType type;

    private PaintCanvas canvas;
    private Observable<Stroke> strokeObservable;
    private Observable<Paint> strokePaintObservable;
    private Observable<Optional<Paint>> fillPaintObservable = BehaviorSubject.createDefault(Optional.empty());
    private Observable<Integer> shapeSidesObservable;
    private Observable<Font> fontObservable;
    private Observable<Color> fontColorObservable;
    private Observable<Double> intensityObservable;
    private Observable<Boolean> drawMultipleObservable;
    private Observable<Boolean> drawCenteredObservable;

    /**
     * Constructs a builder for the specified tool type. Use {@link #create(PaintToolType)} to retrieve an instance
     * of the builder.
     *
     * @param toolType The type of tool to build
     */
    private PaintToolBuilder(PaintToolType toolType) {
        this.type = toolType;
    }

    /**
     * Creates a builder of a specified tool type.
     *
     * @param toolType The type of tool to start building.
     * @return The PaintToolBuilder
     */
    public static PaintToolBuilder create(PaintToolType toolType) {
        return new PaintToolBuilder(toolType);
    }

    /**
     * Makes the newly built tool active on the given canvas. The tool can be activated manually (instead of via this
     * method by invoking {@link PaintTool#activate(PaintCanvas)}).
     *
     * @param jfxPaintCanvasNode The JavaFX canvas on which to activate the tool
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder makeActiveOnCanvas(JFXPaintCanvasNode jfxPaintCanvasNode) {
        this.canvas = jfxPaintCanvasNode.getCanvas();
        return this;
    }

    /**
     * Makes the newly built tool active on the given canvas. The tool can be activated manually (instead of via this
     * method by invoking {@link PaintTool#activate(PaintCanvas)}).
     *
     * @param canvas The Swing canvas on which to activate the tool
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder makeActiveOnCanvas(PaintCanvas canvas) {
        this.canvas = canvas;
        return this;
    }

    /**
     * Specifies the font painted by the tool. Applies only to the {@link com.defano.jmonet.tools.TextTool}.
     *
     * @param font The font to paint
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withFont(Font font) {
        this.fontObservable = BehaviorSubject.createDefault(font);
        return this;
    }

    /**
     * Specifies an observable provider of the font painted by the tool. Applies only to the
     * {@link com.defano.jmonet.tools.TextTool}.
     *
     * @param fontProvider The font to paint
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withFontObservable(Observable<Font> fontProvider) {
        this.fontObservable = fontProvider;
        return this;
    }

    /**
     * Specifies the color of text painted by the tool. Applies only to the {@link com.defano.jmonet.tools.TextTool}).
     *
     * @param color The text color
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withFontColor(Color color) {
        this.fontColorObservable = BehaviorSubject.createDefault(color);
        return this;
    }

    /**
     * Specifies an observable provider of the color of the text painted by the tool. Applies only to the
     * {@link com.defano.jmonet.tools.TextTool}.
     *
     * @param colorProvider The color of the text to paint
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withFontColorObservable(Observable<Color> colorProvider) {
        this.fontColorObservable = colorProvider;
        return this;
    }

    /**
     * Specifies the number of sides drawn by the tool. Applies only to the {@link com.defano.jmonet.tools.PolygonTool}.
     * @param sides The number of sides drawn on a regular polygon
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withShapeSides(int sides) {
        this.shapeSidesObservable = BehaviorSubject.createDefault(sides);
        return this;
    }

    /**
     * Specifies an observable provider of the number of sides drawn by the tool. Applies only to the
     * {@link com.defano.jmonet.tools.PolygonTool}.
     *
     * @param shapeSidesProvider The number of sides drawn on a regular polygon
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withShapeSidesObservable(Observable<Integer> shapeSidesProvider) {
        this.shapeSidesObservable = shapeSidesProvider;
        return this;
    }

    /**
     * Specifies the stroke to be drawn by the tool. The stroke represents the shape of "pen" used to draw paths, lines,
     * and brush strokes. Use {@link StrokeBuilder} to create complex strokes.
     *
     * @param stroke The stroke to be drawn by this tool
     * @return Ths PaintToolBuilder
     */
    public PaintToolBuilder withStroke(Stroke stroke) {
        this.strokeObservable = BehaviorSubject.createDefault(stroke);
        return this;
    }

    /**
     * Specifies an observable provider of the stroke to be drawn by this tool. See {@link #withStroke(Stroke)}.
     *
     * @param strokeProvider The stroke to be drawn by this tool
     * @return The PaintToolBuilder.
     */
    public PaintToolBuilder withStrokeObservable(Observable<Stroke> strokeProvider) {
        this.strokeObservable = strokeProvider;
        return this;
    }

    /**
     * Specifies the paint (color and/or texture) of the stroke drawn by this tool. The stroke represents the shape of
     * "pen" used to draw paths, lines, and brush strokes. Use {@link StrokeBuilder} to
     * create complex strokes.
     *
     * @param strokePaint The color and/or texture of the stroke drawn by this tool
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withStrokePaint(Paint strokePaint) {
        this.strokePaintObservable = BehaviorSubject.createDefault(strokePaint);
        return this;
    }

    /**
     * Specifies an observable provider of the paint of the stroke drawn by this tool. See
     * {@link #withStrokePaint(Paint)}
     *
     * @param strokePaintProvider The color and/or texture of the stroke drawn by this tool
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withStrokePaintObservable(Observable<Paint> strokePaintProvider) {
        this.strokePaintObservable = strokePaintProvider;
        return this;
    }

    /**
     * Specifies the paint (color and/or texture) used to fill shapes drawn by this tool.
     *
     * @param paint The color and/or texture used to fill shapes
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withFillPaint(Paint paint) {
        this.fillPaintObservable = BehaviorSubject.createDefault(paint == null ? Optional.empty() : Optional.of(paint));
        return this;
    }

    /**
     * Specifies an observable provider of the paint used to fill shapes drawn by this tool. See
     * {@link #withFillPaint(Paint)}.
     *
     * @param paintProvider The color and/or texture used to fill shapes
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withFillPaintObservable(Observable<Optional<Paint>> paintProvider) {
        this.fillPaintObservable = paintProvider;
        return this;
    }

    /**
     * Specifies the intensity with which the tool paints. Used only by the
     * {@link com.defano.jmonet.tools.AirbrushTool}.
     *
     * @param intensity A value between 0.0 and 1.0 where 0 is no intensity (tool produces no paint) and 1.0 is full
     *                  intensity.
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withIntensity(double intensity) {
        this.intensityObservable = BehaviorSubject.createDefault(intensity);
        return this;
    }

    /**
     * Specifies an observable provider of the intensity with which the tool paints. See {@link #withIntensity(double)}.
     *
     * @param intensityObservable A value between 0.0 and 1.0 where 0 is no intensity (tool produces no paint) and 1.0
     *                            is full intensity.
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withIntensityObservable(Observable<Double> intensityObservable) {
        this.intensityObservable = intensityObservable;
        return this;
    }

    /**
     * Specifies an observable provider of a boolean value indicating whether the tool defines bounds by dragging
     * from the center-out, or from top-left to bottom-right.
     *
     * @param drawCenteredObservable True to define bounds from the center-out; false for top-left to bottom-right
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withDrawCenteredObservable(Observable<Boolean> drawCenteredObservable) {
        this.drawCenteredObservable = drawCenteredObservable;
        return this;
    }

    /**
     * Specifies whether the tool defines bounds by dragging from the center-out, or from the top-left to bottom-right.
     *
     * @param drawCentered True to define bounds from the center-out; false for top-left to bottom-right
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withDrawCentered(boolean drawCentered) {
        this.drawCenteredObservable = BehaviorSubject.createDefault(drawCentered);
        return this;
    }

    /**
     * Specifies an observable provider of a boolean value indicating whether the tool will draw multiple shapes or
     * just one.
     *
     * @param drawMultipleObservable True to draw multiple shapes; false to draw just one.
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withDrawMultipleObservable(Observable<Boolean> drawMultipleObservable) {
        this.drawMultipleObservable = drawMultipleObservable;
        return this;
    }

    /**
     * Specifies whether the tool should draw a single shape, or a trace of multiple shapes as the mouse is dragged.
     * Affects tools extending {@link AbstractBoundsTool}.
     *
     * @param drawMultiple True to draw multiple shapes; false to draw just one.
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withDrawMultiple(boolean drawMultiple) {
        this.drawMultipleObservable = BehaviorSubject.createDefault(drawMultiple);
        return this;
    }

    /**
     * Creates a paint tool as previously configured.
     * @return The built paint tool.
     */
    public PaintTool build() {

        PaintTool selectedTool = type.getToolInstance();

        if (strokeObservable != null) {
            selectedTool.setStrokeObservable(strokeObservable);
        }

        if (strokePaintObservable != null) {
            selectedTool.setStrokePaintObservable(strokePaintObservable);
        }

        if (shapeSidesObservable != null) {
            selectedTool.setShapeSidesObservable(shapeSidesObservable);
        }

        if (fontObservable != null) {
            selectedTool.setFontObservable(fontObservable);
        }

        if (fillPaintObservable != null) {
            selectedTool.setFillPaintObservable(fillPaintObservable);
        }

        if (fontColorObservable != null) {
            selectedTool.setFontColorObservable(fontColorObservable);
        }

        if (intensityObservable != null) {
            selectedTool.setIntensityObservable(intensityObservable);
        }

        if (drawMultipleObservable != null) {
            selectedTool.setDrawMultipleObservable(drawMultipleObservable);
        }

        if (drawCenteredObservable != null) {
            selectedTool.setDrawCenteredObservable(drawCenteredObservable);
        }

        if (canvas != null) {
            selectedTool.activate(canvas);
        }

        return selectedTool;
    }
}
