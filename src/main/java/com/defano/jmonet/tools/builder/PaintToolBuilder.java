package com.defano.jmonet.tools.builder;

import com.defano.jmonet.canvas.JFXPaintCanvasNode;
import com.defano.jmonet.canvas.PaintCanvas;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.*;
import com.defano.jmonet.model.Provider;
import com.defano.jmonet.tools.base.PaintTool;

import java.awt.*;

/**
 * A utility for building paint tools.
 */
public class PaintToolBuilder {

    private final PaintToolType type;

    private PaintCanvas canvas;
    private Provider<Stroke> strokeProvider;
    private Provider<Paint> strokePaintProvider;
    private Provider<Paint> fillPaintProvider;
    private Provider<Integer> shapeSidesProvider;
    private Provider<Font> fontProvider;
    private Provider<Color> fontColorProvider;

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
        this.fontProvider = new Provider<>(font);
        return this;
    }

    public PaintToolBuilder withFontProvider(Provider<Font> fontProvider) {
        this.fontProvider = fontProvider;
        return this;
    }

    public PaintToolBuilder withShapeSides(int sides) {
        this.shapeSidesProvider = new Provider<>(sides);
        return this;
    }

    public PaintToolBuilder withShapeSidesProvider(Provider<Integer> shapeSidesProvider) {
        this.shapeSidesProvider = shapeSidesProvider;
        return this;
    }

    public PaintToolBuilder withStroke(Stroke stroke) {
        this.strokeProvider = new Provider<>(stroke);
        return this;
    }

    public PaintToolBuilder withStrokeProvider(Provider<Stroke> strokeProvider) {
        this.strokeProvider = strokeProvider;
        return this;
    }

    public PaintToolBuilder withStrokePaint(Paint strokePaint) {
        this.strokePaintProvider = new Provider<>(strokePaint);
        return this;
    }

    public PaintToolBuilder withStrokePaintProvider(Provider<Paint> strokePaintProvider) {
        this.strokePaintProvider = strokePaintProvider;
        return this;
    }

    public PaintToolBuilder withFillPaint(Paint paint) {
        this.fillPaintProvider = new Provider<>(paint);
        return this;
    }

    public PaintToolBuilder withFillPaintProvider(Provider<Paint> paintProvider) {
        this.fillPaintProvider = paintProvider;
        return this;
    }

    public PaintToolBuilder withFontColor(Color color) {
        this.fontColorProvider = new Provider<>(color);
        return this;
    }

    public PaintToolBuilder withFontColorProvider(Provider<Color> colorProvider) {
        this.fontColorProvider = colorProvider;
        return this;
    }

    public PaintTool build() {

        PaintTool selectedTool = type.getToolInstance();

        if (strokeProvider != null) {
            selectedTool.setStrokeProvider(strokeProvider);
        }

        if (strokePaintProvider != null) {
            selectedTool.setStrokePaintProvider(strokePaintProvider);
        }

        if (shapeSidesProvider != null) {
            selectedTool.setShapeSidesProvider(shapeSidesProvider);
        }

        if (fontProvider != null) {
            selectedTool.setFontProvider(fontProvider);
        }

        if (fillPaintProvider != null) {
            selectedTool.setFillPaintProvider(fillPaintProvider);
        }

        if (fontColorProvider != null) {
            selectedTool.setFontColorProvider(fontColorProvider);
        }

        if (canvas != null) {
            selectedTool.activate(canvas);
        }

        return selectedTool;
    }
}
