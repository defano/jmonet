package com.defano.jmonet.tools.builder;

import io.reactivex.Observable;

import java.awt.*;
import java.util.Optional;

public interface ToolAttributes {

    Stroke getStroke();

    Paint getStrokePaint();

    Optional<Paint> getFillPaint();

    Font getFont();

    Color getFontColor();

    int getShapesSides();

    int getShapeSides();

    Color getEraseColor();

    int getConstrainedAngle();

    double getIntensity();

    boolean isDrawMultiple();

    boolean isDrawCentered();

    int getCornerRadius();

    Observable<Optional<Color>> getEraseColorObservable();

    Observable<Stroke> getStrokeObservable();

    Observable<Paint> getStrokePaintObservable();

    Observable<Integer> getShapeSidesObservable();

    Observable<Font> getFontObservable();

    Observable<Color> getFontColorObservable();

    Observable<Double> getIntensityObservable();

    Observable<Boolean> getDrawCenteredObservable();

    Observable<Boolean> getDrawMultipleObservable();

    Observable<Integer> getCornerRadiusObservable();

    Observable<Integer> getConstrainedAngleObservable();
}
