package com.defano.jmonet.canvas.surface;

import com.defano.jmonet.canvas.Disposable;
import com.defano.jmonet.canvas.observable.ObservableSurface;

import java.awt.*;

public interface Surface extends Scalable, Disposable, SwingSurface, ObservableSurface, ScrollableSurface {

    Point getScrollError();
}
