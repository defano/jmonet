package com.defano.jmonet.tools.builder;

import com.defano.jmonet.canvas.JFXPaintCanvasNode;
import com.defano.jmonet.canvas.PaintCanvas;
import com.defano.jmonet.model.PaintToolType;
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

    private PaintToolBuilder(PaintToolType toolType) {
        this.type = toolType;
    }

    public static PaintToolBuilder create(PaintToolType toolType) {
        return new PaintToolBuilder(toolType);
    }

    public PaintToolBuilder makeActiveOnCanvas(JFXPaintCanvasNode jfxPaintCanvasNode) {
        this.canvas = jfxPaintCanvasNode.getCanvas();
        return this;
    }

    public PaintToolBuilder makeActiveOnCanvas(PaintCanvas canvas) {
        this.canvas = canvas;
        return this;
    }

    public PaintToolBuilder withFont(Font font) {
        this.fontObservable = BehaviorSubject.createDefault(font);
        return this;
    }

    public PaintToolBuilder withFontObservable(Observable<Font> fontProvider) {
        this.fontObservable = fontProvider;
        return this;
    }

    public PaintToolBuilder withShapeSides(int sides) {
        this.shapeSidesObservable = BehaviorSubject.createDefault(sides);
        return this;
    }

    public PaintToolBuilder withShapeSidesObservable(Observable<Integer> shapeSidesProvider) {
        this.shapeSidesObservable = shapeSidesProvider;
        return this;
    }

    public PaintToolBuilder withStroke(Stroke stroke) {
        this.strokeObservable = BehaviorSubject.createDefault(stroke);
        return this;
    }

    public PaintToolBuilder withStrokeObservable(Observable<Stroke> strokeProvider) {
        this.strokeObservable = strokeProvider;
        return this;
    }

    public PaintToolBuilder withStrokePaint(Paint strokePaint) {
        this.strokePaintObservable = BehaviorSubject.createDefault(strokePaint);
        return this;
    }

    public PaintToolBuilder withStrokePaintObservable(Observable<Paint> strokePaintProvider) {
        this.strokePaintObservable = strokePaintProvider;
        return this;
    }

    public PaintToolBuilder withFillPaint(Paint paint) {
        this.fillPaintObservable = BehaviorSubject.createDefault(Optional.of(paint));
        return this;
    }

    public PaintToolBuilder withFillPaintObservable(Observable<Optional<Paint>> paintProvider) {
        this.fillPaintObservable = paintProvider;
        return this;
    }

    public PaintToolBuilder withFontColor(Color color) {
        this.fontColorObservable = BehaviorSubject.createDefault(color);
        return this;
    }

    public PaintToolBuilder withFontColorObservable(Observable<Color> colorProvider) {
        this.fontColorObservable = colorProvider;
        return this;
    }

    public PaintToolBuilder withIntensity(double intensity) {
        this.intensityObservable = BehaviorSubject.createDefault(intensity);
        return this;
    }

    public PaintToolBuilder withIntensityObservable(Observable<Double> intensityObservable) {
        this.intensityObservable = intensityObservable;
        return this;
    }

    public PaintToolBuilder withDrawCenteredObservable(Observable<Boolean> drawCenteredObservable) {
        this.drawCenteredObservable = drawCenteredObservable;
        return this;
    }

    public PaintToolBuilder withDrawCentered(boolean drawCentered) {
        this.drawCenteredObservable = BehaviorSubject.createDefault(drawCentered);
        return this;
    }

    public PaintToolBuilder withDrawMultipleObservable(Observable<Boolean> drawMultipleObservable) {
        this.drawMultipleObservable = drawMultipleObservable;
        return this;
    }

    public PaintToolBuilder withDrawMultiple(boolean drawMultiple) {
        this.drawMultipleObservable = BehaviorSubject.createDefault(drawMultiple);
        return this;
    }

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
