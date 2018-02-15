package com.defano.jmonet.tools.brushes;

import java.awt.*;

public class BrushBuilder {

    private enum BrushType {
        CIRCLE, OVAL, POLYGON, QUAD, RECTANGLE, SQUARE, SHAPE, TEXT
    }

    private Integer size, width, height;
    private Double rotation;
    private Font font;
    private String text;
    private boolean linearInterpolation = true;
    private BrushType type;

    public SquareBrushBuilder buildCircleBrush() {
        this.type = BrushType.CIRCLE;
        return new SquareBrushBuilder();
    }

    public RectangularBrushBuilder buildOvalBrush() {
        this.type = BrushType.OVAL;
        return new RectangularBrushBuilder();
    }

    public class RectangularBrushBuilder extends BrushBuilderCommon<RectangularBrushBuilder> {
        public RectangularBrushBuilder withHeightAndWidth(int height, int width) {
            BrushBuilder.this.width = width;
            BrushBuilder.this.height = height;
            return this;
        }
    }

    public class SquareBrushBuilder extends BrushBuilderCommon<SquareBrushBuilder> {
        public SquareBrushBuilder withSize(int Size) {
            BrushBuilder.this.size = size;
            return this;
        }
    }

    public class BrushBuilderCommon<T> {

        public T withoutLinearInterpolation() {
            BrushBuilder.this.linearInterpolation = false;
            return (T) this;
        }

    }

}
