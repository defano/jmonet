package com.defano.jmonet.tools.builder;

import com.defano.jmonet.canvas.JFXPaintCanvasNode;
import com.defano.jmonet.canvas.PaintCanvas;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.PaintTool;
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
    private Observable<Stroke> strokeSubject;
    private Observable<Paint> strokePaintSubject;
    private Observable<Optional<Paint>> fillPaintSubject = BehaviorSubject.createDefault(Optional.empty());
    private Observable<Integer> shapeSidesSubject;
    private Observable<Font> fontSubject;
    private Observable<Color> fontColorSubject;

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
        this.fontSubject = BehaviorSubject.createDefault(font);
        return this;
    }

    public PaintToolBuilder withFontSubject(Observable<Font> fontProvider) {
        this.fontSubject = fontProvider;
        return this;
    }

    public PaintToolBuilder withShapeSides(int sides) {
        this.shapeSidesSubject = BehaviorSubject.createDefault(sides);
        return this;
    }

    public PaintToolBuilder withShapeSidesSubject(Observable<Integer> shapeSidesProvider) {
        this.shapeSidesSubject = shapeSidesProvider;
        return this;
    }

    public PaintToolBuilder withStroke(Stroke stroke) {
        this.strokeSubject = BehaviorSubject.createDefault(stroke);
        return this;
    }

    public PaintToolBuilder withStrokeSubject(Observable<Stroke> strokeProvider) {
        this.strokeSubject = strokeProvider;
        return this;
    }

    public PaintToolBuilder withStrokePaint(Paint strokePaint) {
        this.strokePaintSubject = BehaviorSubject.createDefault(strokePaint);
        return this;
    }

    public PaintToolBuilder withStrokePaintSubject(Observable<Paint> strokePaintProvider) {
        this.strokePaintSubject = strokePaintProvider;
        return this;
    }

    public PaintToolBuilder withFillPaint(Paint paint) {
        this.fillPaintSubject = BehaviorSubject.createDefault(Optional.of(paint));
        return this;
    }

    public PaintToolBuilder withFillPaintSubject(Observable<Optional<Paint>> paintProvider) {
        this.fillPaintSubject = paintProvider;
        return this;
    }

    public PaintToolBuilder withFontColor(Color color) {
        this.fontColorSubject = BehaviorSubject.createDefault(color);
        return this;
    }

    public PaintToolBuilder withFontColorSubject(Observable<Color> colorProvider) {
        this.fontColorSubject = colorProvider;
        return this;
    }

    public PaintTool build() {

        PaintTool selectedTool = type.getToolInstance();

        if (strokeSubject != null) {
            selectedTool.setStrokeProvider(strokeSubject);
        }

        if (strokePaintSubject != null) {
            selectedTool.setStrokePaintProvider(strokePaintSubject);
        }

        if (shapeSidesSubject != null) {
            selectedTool.setShapeSidesProvider(shapeSidesSubject);
        }

        if (fontSubject != null) {
            selectedTool.setFontProvider(fontSubject);
        }

        if (fillPaintSubject != null) {
            selectedTool.setFillPaintProvider(fillPaintSubject);
        }

        if (fontColorSubject != null) {
            selectedTool.setFontColorProvider(fontColorSubject);
        }

        if (canvas != null) {
            selectedTool.activate(canvas);
        }

        return selectedTool;
    }
}
