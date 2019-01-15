package com.defano.jmonet.tools.builder;

import com.defano.jmonet.tools.attributes.*;
import com.defano.jmonet.canvas.JFXPaintCanvasNode;
import com.defano.jmonet.canvas.PaintCanvas;
import com.defano.jmonet.model.Interpolation;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.AirbrushTool;
import com.defano.jmonet.tools.FillTool;
import com.defano.jmonet.tools.PolygonTool;
import com.defano.jmonet.tools.TextTool;
import com.defano.jmonet.tools.cursors.CursorManager;
import com.defano.jmonet.tools.cursors.SwingCursorManager;
import com.defano.jmonet.tools.base.Tool;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.awt.*;
import java.util.Optional;

/**
 * A utility for building paint tools.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class PaintToolBuilder {

    private final PaintToolType type;

    // Non-observable attributes
    private PaintCanvas canvas;
    private MarkPredicate markPredicate;
    private FillFunction fillFunction;
    private BoundaryFunction boundaryFunction;

    // Observable attributes
    private Observable<Stroke> strokeObservable;
    private Observable<Paint> strokePaintObservable;
    private Observable<Optional<Paint>> fillPaintObservable = BehaviorSubject.createDefault(Optional.empty());
    private Observable<Optional<Color>> erasePaintObservable = BehaviorSubject.createDefault(Optional.empty());
    private Observable<Integer> shapeSidesObservable;
    private Observable<Font> fontObservable;
    private Observable<Color> fontColorObservable;
    private Observable<Double> intensityObservable;
    private Observable<Boolean> drawMultipleObservable;
    private Observable<Boolean> drawCenteredObservable;
    private Observable<Integer> cornerRadiusObservable;
    private Observable<Interpolation> antiAliasingObservable;
    private Observable<Integer> constrainedAngleObservable;
    private Observable<Double> minimumScaleObservable;
    private Observable<Double> maximumScaleObservable;
    private Observable<Double> magnificationStepObservable;
    private Observable<Boolean> recenterOnMagnifyObservable;
    private Observable<Boolean> pathInterpolationObservable;

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
     * method by invoking {@link Tool#activate(PaintCanvas)}).
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
     * method by invoking {@link Tool#activate(PaintCanvas)}).
     *
     * @param canvas The Swing canvas on which to activate the tool
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder makeActiveOnCanvas(PaintCanvas canvas) {
        this.canvas = canvas;
        return this;
    }

    /**
     * Specifies the font painted by the tool. Applies only to the {@link TextTool}.
     *
     * @param font The font to paint
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withFont(Font font) {
        return withFontObservable(BehaviorSubject.createDefault(font));
    }

    /**
     * Specifies an observable provider of the font painted by the tool. Applies only to the
     * {@link TextTool}.
     *
     * @param fontProvider The font to paint
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withFontObservable(Observable<Font> fontProvider) {
        this.fontObservable = fontProvider;
        return this;
    }

    /**
     * Specifies the color of text painted by the tool. Applies only to the {@link TextTool}).
     *
     * @param color The text color
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withFontColor(Color color) {
        return withFontColorObservable(BehaviorSubject.createDefault(color));
    }

    /**
     * Specifies an observable provider of the color of the text painted by the tool. Applies only to the
     * {@link TextTool}.
     *
     * @param colorProvider The color of the text to paint
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withFontColorObservable(Observable<Color> colorProvider) {
        this.fontColorObservable = colorProvider;
        return this;
    }

    /**
     * Specifies the number of sides drawn by the tool. Applies only to the {@link PolygonTool}.
     *
     * @param sides The number of sides drawn on a regular polygon
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withShapeSides(int sides) {
        return withShapeSidesObservable(BehaviorSubject.createDefault(sides));
    }

    /**
     * Specifies an observable provider of the number of sides drawn by the tool. Applies only to the
     * {@link PolygonTool}.
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
     * and brush strokes. Use StrokeBuilder to create complex strokes.
     *
     * @param stroke The stroke to be drawn by this tool
     * @return Ths PaintToolBuilder
     */
    public PaintToolBuilder withStroke(Stroke stroke) {
        return withStrokeObservable(BehaviorSubject.createDefault(stroke));
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
     * "pen" used to draw paths, lines, and brush strokes. Use StrokeBuilder to
     * create complex strokes.
     *
     * @param strokePaint The color and/or texture of the stroke drawn by this tool
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withStrokePaint(Paint strokePaint) {
        return withStrokePaintObservable(BehaviorSubject.createDefault(strokePaint));
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
        return withFillPaintObservable(BehaviorSubject.createDefault(paint == null ? Optional.empty() : Optional.of(paint)));
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
     * Specifies the color that pixels are changed to when they're erased (via the eraser or pencil tools). Specify null
     * for fully-transparent (default behavior).
     * <p>
     * Note that this color does not affect the color of "void" pixels that are left when selecting a region and moving
     * or deleting it. Further note that the default boundary behavior associated with
     * {@link FillTool} looks for fully transparent pixels, thus, when changing the erase color
     * to a non-null value, erased pixels will not be filled by this tool (install a custom
     * BoundaryFunction if such behavior is desired).
     *
     * @param paint The color that erased pixels should become; null means fully transparent.
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withEraseColor(Color paint) {
        return withEraseColorObservable(
                BehaviorSubject.createDefault(paint == null ? Optional.empty() : Optional.of(paint))
        );
    }

    /**
     * Specifies an observable provider of the paint that pixels are changed to when they're erased (via the eraser or
     * pencil tools). Specify {@link Optional#empty()} for fully-transparent (default behavior).
     * <p>
     * Note that this color does not affect the color of "void" pixels that are left when selecting a region and moving
     * or deleting it. Further note that the default boundary behavior associated with
     * {@link FillTool} looks for fully transparent pixels, thus, when changing the erase color
     * to a non-null value, erased pixels will not be filled by this tool (install a custom
     * BoundaryFunction if such behavior is desired).
     *
     * @param erasePaintObservable Observable providing the color that erased pixels should become; null means fully
     *                             transparent.
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withEraseColorObservable(Observable<Optional<Color>> erasePaintObservable) {
        this.erasePaintObservable = erasePaintObservable;
        return this;
    }

    /**
     * Specifies the intensity with which the tool paints. Used only by the
     * {@link AirbrushTool}.
     *
     * @param intensity A value between 0.0 and 1.0 where 0 is no intensity (tool produces no paint) and 1.0 is full
     *                  intensity.
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withIntensity(double intensity) {
        return withIntensityObservable(BehaviorSubject.createDefault(intensity));
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
        return withDrawCenteredObservable(BehaviorSubject.createDefault(drawCentered));
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
     * Affects tools extending {@link com.defano.jmonet.tools.base.BoundsTool}.
     *
     * @param drawMultiple True to draw multiple shapes; false to draw just one.
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withDrawMultiple(boolean drawMultiple) {
        return withDrawMultipleObservable(BehaviorSubject.createDefault(drawMultiple));
    }

    /**
     * Specifies the height and width of the corner used for round rectangles.
     *
     * @param cornerRadius The height and width of the corner radius
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withCornerRadius(int cornerRadius) {
        return withCornerRadiusObservable(BehaviorSubject.createDefault(cornerRadius));
    }

    /**
     * Specifies the height and width of the corner used for round rectangles.
     *
     * @param observable An observable of the height and width of the corner radius
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withCornerRadiusObservable(Observable<Integer> observable) {
        this.cornerRadiusObservable = observable;
        return this;
    }

    /**
     * Specifies the antialiasing mode to use when rendering shapes and images. See {@link Interpolation}.
     *
     * @param mode The mode to use with this tool.
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withAntiAliasing(Interpolation mode) {
        return withAntiAliasingObservable(BehaviorSubject.createDefault(mode));
    }

    /**
     * Specifies an observable provider of anti-aliasing configuration.
     *
     * @param observable An observable providing the current {@link Interpolation}.
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withAntiAliasingObservable(Observable<Interpolation> observable) {
        this.antiAliasingObservable = observable;
        return this;
    }

    public PaintToolBuilder withPathInterpolationObservable(Observable<Boolean> observable) {
        this.pathInterpolationObservable = observable;
        return this;
    }

    public PaintToolBuilder withPathInterpolation(boolean enabled) {
        return withPathInterpolationObservable(BehaviorSubject.createDefault(enabled));
    }

    public PaintToolBuilder withConstrainedAngleObservable(Observable<Integer> observable) {
        this.constrainedAngleObservable = observable;
        return this;
    }

    public PaintToolBuilder withConstrainedAngle(int angle) {
        return withConstrainedAngleObservable(BehaviorSubject.createDefault(angle));
    }

    public PaintToolBuilder withMaximumScaleObservable(Observable<Double> observable) {
        this.maximumScaleObservable = observable;
        return this;
    }

    public PaintToolBuilder withMaximumScale(double maximumScale) {
        return withMaximumScaleObservable(BehaviorSubject.createDefault(maximumScale));
    }

    public PaintToolBuilder withMinimumScaleObservable(Observable<Double> observable) {
        this.minimumScaleObservable = observable;
        return this;
    }

    public PaintToolBuilder withMinimumScale(double minimumScale) {
        return withMinimumScaleObservable(BehaviorSubject.createDefault(minimumScale));
    }

    public PaintToolBuilder withMagnificationStepObservable(Observable<Double> observable) {
        this.magnificationStepObservable = observable;
        return this;
    }

    public PaintToolBuilder withMagnificationStep(double magnificationStep) {
        return withMagnificationStepObservable(BehaviorSubject.createDefault(magnificationStep));
    }

    public PaintToolBuilder withRecenterOnMagnifyObservable(Observable<Boolean> observable) {
        this.recenterOnMagnifyObservable = observable;
        return this;
    }

    public PaintToolBuilder withRecenterOnMagnify(boolean recenterOnMagnify) {
        return withRecenterOnMagnifyObservable(BehaviorSubject.createDefault(recenterOnMagnify));
    }

    /**
     * Specifies the predicate function used to determine if a canvas pixel is considered "marked," not blank (for
     * example, used in determining if the pencil tool should mark or erase). See {@link MarkPredicate} for details.
     *
     * @param markPredicate The mark predicate function
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withMarkPredicate(MarkPredicate markPredicate) {
        this.markPredicate = markPredicate;
        return this;
    }

    /**
     * Specifies the function used to color the canvas with paint flooding a region. See {@link FillFunction} for
     * details.
     *
     * @param fillFunction The fill function to use
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withFillFunction(FillFunction fillFunction) {
        this.fillFunction = fillFunction;
        return this;
    }

    /**
     * Specifies the function used to detect when paint flooding a region has reached a boundary. See
     * {@link BoundaryFunction} for details.
     *
     * @param boundaryFunction The boundary function to use.
     * @return The PaintToolBuilder
     */
    public PaintToolBuilder withBoundaryFunction(BoundaryFunction boundaryFunction) {
        this.boundaryFunction = boundaryFunction;
        return this;
    }

    /**
     * Creates a paint tool as previously configured.
     *
     * @return The built paint tool.
     */
    public Tool build() {

        Tool selectedTool = Guice.createInjector(new ToolAssembly()).getInstance(type.getToolClass());
        ToolAttributes toolAttributes = selectedTool.getAttributes();

        if (strokeObservable != null) {
            toolAttributes.setStrokeObservable(strokeObservable);
        }

        if (strokePaintObservable != null) {
            toolAttributes.setStrokePaintObservable(strokePaintObservable);
        }

        if (erasePaintObservable != null) {
            toolAttributes.setEraseColorObservable(erasePaintObservable);
        }

        if (shapeSidesObservable != null) {
            toolAttributes.setShapeSidesObservable(shapeSidesObservable);
        }

        if (fontObservable != null) {
            toolAttributes.setFontObservable(fontObservable);
        }

        if (fillPaintObservable != null) {
            toolAttributes.setFillPaintObservable(fillPaintObservable);
        }

        if (fontColorObservable != null) {
            toolAttributes.setFontColorObservable(fontColorObservable);
        }

        if (intensityObservable != null) {
            toolAttributes.setIntensityObservable(intensityObservable);
        }

        if (drawMultipleObservable != null) {
            toolAttributes.setDrawMultipleObservable(drawMultipleObservable);
        }

        if (drawCenteredObservable != null) {
            toolAttributes.setDrawCenteredObservable(drawCenteredObservable);
        }

        if (cornerRadiusObservable != null) {
            toolAttributes.setCornerRadiusObservable(cornerRadiusObservable);
        }

        if (antiAliasingObservable != null) {
            toolAttributes.setAntiAliasingObservable(antiAliasingObservable);
        }

        if (constrainedAngleObservable != null) {
            toolAttributes.setConstrainedAngleObservable(constrainedAngleObservable);
        }

        if (maximumScaleObservable != null) {
            toolAttributes.setMaximumScaleObservable(maximumScaleObservable);
        }

        if (minimumScaleObservable != null) {
            toolAttributes.setMinimumScaleObservable(minimumScaleObservable);
        }

        if (magnificationStepObservable != null) {
            toolAttributes.setMagnificationStepObservable(magnificationStepObservable);
        }

        if (recenterOnMagnifyObservable != null) {
            toolAttributes.setRecenterOnMagnifyObservable(recenterOnMagnifyObservable);
        }

        if (pathInterpolationObservable != null) {
            toolAttributes.setPathInterpolationObservable(pathInterpolationObservable);
        }

        if (markPredicate != null) {
            toolAttributes.setMarkPredicate(markPredicate);
        }

        if (fillFunction != null) {
            toolAttributes.setFillFunction(fillFunction);
        }

        if (boundaryFunction != null) {
            toolAttributes.setBoundaryFunction(boundaryFunction);
        }

        if (canvas != null) {
            selectedTool.activate(canvas);
        }

        return selectedTool;
    }

    private static class ToolAssembly extends AbstractModule {

        @Override
        protected void configure() {
            bind(ToolAttributes.class).to(RxToolAttributes.class);
            bind(CursorManager.class).to(SwingCursorManager.class);
        }
    }
}
