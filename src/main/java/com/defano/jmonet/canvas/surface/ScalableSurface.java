package com.defano.jmonet.canvas.surface;

import com.defano.jmonet.model.Provider;

import java.awt.*;

public interface ScalableSurface {
    void setImageLocation(Point location);
    Point getImageLocation();

    void setScale(double scale);
    Provider<Double> getScaleProvider();
}
