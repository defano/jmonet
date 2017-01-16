package com.defano.jmonet.canvas.surface;

import com.defano.jmonet.model.Provider;

import java.awt.*;

public interface ScalableGridSurface {

    void setImageLocation(Point location);
    Point getImageLocation();

    void setScale(double scale);
    Provider<Double> getScaleProvider();

    void setGridSpacing(int grid);
    Provider<Integer> getGridSpacingProvider();

    int translateX(int x);
    int translateY(int y);
}
