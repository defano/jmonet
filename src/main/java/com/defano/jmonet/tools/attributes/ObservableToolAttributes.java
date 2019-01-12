package com.defano.jmonet.tools.attributes;

import com.defano.jmonet.model.Interpolation;
import io.reactivex.Observable;

import java.awt.*;
import java.util.Optional;

public interface ObservableToolAttributes {

    void setEraseColorObservable(Observable<Optional<Color>> observable);
    Observable<Optional<Color>> getEraseColorObservable();

    void setFillPaintObservable(Observable<Optional<Paint>> observable);
    Observable<Optional<Paint>> getFillPaintObservable();

    void setStrokeObservable(Observable<Stroke> observable);
    Observable<Stroke> getStrokeObservable();

    void setStrokePaintObservable(Observable<Paint> observable);
    Observable<Paint> getStrokePaintObservable();

    void setShapeSidesObservable(Observable<Integer> observable);
    Observable<Integer> getShapeSidesObservable();

    void setFontObservable(Observable<Font> observable);
    Observable<Font> getFontObservable();

    void setFontColorObservable(Observable<Color> observable);
    Observable<Color> getFontColorObservable();

    void setIntensityObservable(Observable<Double> observable);
    Observable<Double> getIntensityObservable();

    void setDrawCenteredObservable(Observable<Boolean> observable);
    Observable<Boolean> getDrawCenteredObservable();

    void setDrawMultipleObservable(Observable<Boolean> observable);
    Observable<Boolean> getDrawMultipleObservable();

    void setCornerRadiusObservable(Observable<Integer> observable);
    Observable<Integer> getCornerRadiusObservable();

    void setConstrainedAngleObservable(Observable<Integer> observable);
    Observable<Integer> getConstrainedAngleObservable();

    void setAntiAliasingObservable(Observable<Interpolation> observable);
    Observable<Interpolation> getAntiAliasingObservable();

    void setMinimumScaleObservable(Observable<Double> observable);
    Observable<Double> getMinimumScaleObservable();

    void setMaximumScaleObservable(Observable<Double> observable);
    Observable<Double> getMaximumScaleObservable();

    void setMagnificationStepObservable(Observable<Double> observable);
    Observable<Double> getMagnificationStepObservable();

    void setRecenterOnMagnifyObservable(Observable<Boolean> observable);
    Observable<Boolean> getRecenterOnMagnifyObservable();
}
