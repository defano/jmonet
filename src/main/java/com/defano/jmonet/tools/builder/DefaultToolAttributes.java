package com.defano.jmonet.tools.builder;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.awt.*;
import java.util.Optional;

public class DefaultToolAttributes implements ToolAttributes {

    private Observable<Integer> constrainedAngleObservable = BehaviorSubject.createDefault(15);
    private Observable<Stroke> strokeObservable = BehaviorSubject.createDefault(new BasicStroke(2));
    private Observable<Paint> strokePaintObservable = BehaviorSubject.createDefault(Color.BLACK);
    private Observable<Optional<Paint>> fillPaintObservable = BehaviorSubject.createDefault(Optional.empty());
    private Observable<Optional<Color>> eraseColorObservable = BehaviorSubject.createDefault(Optional.empty());
    private Observable<Integer> shapeSidesObservable = BehaviorSubject.createDefault(5);
    private Observable<Font> fontObservable = BehaviorSubject.createDefault(new Font("Courier", Font.PLAIN, 14));
    private Observable<Color> fontColorObservable = BehaviorSubject.createDefault(Color.BLACK);
    private Observable<Double> intensityObservable = BehaviorSubject.createDefault(0.1);
    private Observable<Boolean> drawMultipleObservable = BehaviorSubject.createDefault(false);
    private Observable<Boolean> drawCenteredObservable = BehaviorSubject.createDefault(false);
    private Observable<Integer> cornerRadiusObservable = BehaviorSubject.createDefault(10);

    void setFontColorObservable(Observable<Color> fontColorObservable) {
        this.fontColorObservable = fontColorObservable;
    }

    void setStrokePaintObservable(Observable<Paint> strokePaintObservable) {
        if (strokePaintObservable != null) {
            this.strokePaintObservable = strokePaintObservable;
        }
    }

    void setStrokeObservable(Observable<Stroke> strokeObservable) {
        if (strokeObservable != null) {
            this.strokeObservable = strokeObservable;
        }
    }

    void setShapeSidesObservable(Observable<Integer> shapeSidesObservable) {
        if (shapeSidesObservable != null) {
            this.shapeSidesObservable = shapeSidesObservable;
        }
    }

    void setFontObservable(Observable<Font> fontObservable) {
        if (fontObservable != null) {
            this.fontObservable = fontObservable;
        }
    }

    void setFillPaintObservable(Observable<Optional<Paint>> fillPaintObservable) {
        this.fillPaintObservable = fillPaintObservable;
    }

    /**
     * Provides an observable color specifying
     * @param eraseColorObservable The color that erased pixels should be assigned (i.e., white).
     */
    void setEraseColorObservable(Observable<Optional<Color>> eraseColorObservable) {
        this.eraseColorObservable = eraseColorObservable;
    }

    void setIntensityObservable(Observable<Double> intensityObservable) {
        this.intensityObservable = intensityObservable;
    }

    void setDrawMultipleObservable(Observable<Boolean> drawMultipleObservable) {
        this.drawMultipleObservable = drawMultipleObservable;
    }

    void setDrawCenteredObservable(Observable<Boolean> drawCenteredObservable) {
        this.drawCenteredObservable = drawCenteredObservable;
    }

    @Override
    public Stroke getStroke() {
        return strokeObservable.blockingFirst();
    }

    @Override
    public Optional<Paint> getFillPaint() {
        return fillPaintObservable.blockingFirst();
    }

    @Override
    public Font getFont() {
        return fontObservable.blockingFirst();
    }

    @Override
    public int getShapeSides() {
        return shapeSidesObservable.blockingFirst() < 3 ? 3 :
                shapeSidesObservable.blockingFirst() > 20 ? 20 :
                        shapeSidesObservable.blockingFirst();
    }

    @Override
    public Paint getStrokePaint() {
        try {
            return strokePaintObservable.blockingFirst();
        } catch (NullPointerException e) {
            return Color.BLACK;
        }
    }

    @Override
    public Color getEraseColor() {
        return eraseColorObservable.blockingFirst().orElse(null);
    }

    @Override
    public Color getFontColor() {
        return fontColorObservable.blockingFirst();
    }

    @Override
    public int getShapesSides() {
        return shapeSidesObservable.blockingFirst();
    }

    @Override
    public double getIntensity() {
        return intensityObservable.blockingFirst();
    }

    @Override
    public boolean isDrawMultiple() {
        return drawMultipleObservable.blockingFirst();
    }

    @Override
    public boolean isDrawCentered() {
        return drawCenteredObservable.blockingFirst();
    }

    @Override
    public int getCornerRadius() {
        return cornerRadiusObservable.blockingFirst();
    }

    public Observable<Optional<Paint>> getFillPaintObservable() {
        return fillPaintObservable;
    }

    public Observable<Optional<Color>> getEraseColorObservable() {
        return eraseColorObservable;
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

    public Observable<Double> getIntensityObservable() {
        return intensityObservable;
    }

    public Observable<Boolean> getDrawCenteredObservable() {
        return drawCenteredObservable;
    }

    public Observable<Boolean> getDrawMultipleObservable() {
        return drawMultipleObservable;
    }

    public Observable<Integer> getCornerRadiusObservable() {
        return cornerRadiusObservable;
    }

    public Observable<Integer> getConstrainedAngleObservable() {
        return constrainedAngleObservable;
    }

    public void setCornerRadiusObservable(Observable<Integer> cornerRadiusObservable) {
        this.cornerRadiusObservable = cornerRadiusObservable;
    }

    @Override
    public int getConstrainedAngle() {
        return constrainedAngleObservable.blockingFirst();
    }

    public void setConstrainedAngleObservable(Observable<Integer> constrainedAngleObservable) {
        this.constrainedAngleObservable = constrainedAngleObservable;
    }

}
