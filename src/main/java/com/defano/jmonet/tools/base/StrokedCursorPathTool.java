package com.defano.jmonet.tools.base;

import com.defano.jmonet.canvas.PaintCanvas;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.util.CursorFactory;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import java.awt.*;

public abstract class StrokedCursorPathTool extends AbstractPathTool {

    private Disposable subscription;
    private boolean strokeTrackingCursorEnabled = true;

    public StrokedCursorPathTool(PaintToolType type) {
        super(type);
    }

    @Override
    public void deactivate() {
        super.deactivate();
        if (subscription != null) {
            subscription.dispose();
        }
    }

    @Override
    public void activate(PaintCanvas canvas) {
        super.activate(canvas);
        subscription = Observable.merge(getStrokeObservable(), getStrokePaintObservable()).subscribe(o ->
        {
            if (strokeTrackingCursorEnabled) {
                setToolCursor(CursorFactory.makeBrushCursor(getStroke(), getStrokePaint()));
            }
        });
    }

    /**
     * Determines if the cursor associated with this tool is automatically updated to reflect the shape, color and
     * texture of the paint it will apply. See {@link #setStrokeTrackingCursorEnabled(boolean)}.
     *
     * @return True if stroke tracking is enabled; false otherwise.
     */
    public boolean isStrokeTrackingCursorEnabled() {
        return strokeTrackingCursorEnabled;
    }

    /**
     * Sets whether this tool's cursor "tracks" the provisioned stroke and fill, that is, the cursor is automatically
     * updated to represent the shape, color and texture of the paint it will apply.
     *
     * When enabled, any cursor specified via {@link #setToolCursor(Cursor)} will be overridden. Disable when a custom
     * cursor is desired.
     *
     * @param strokeTrackingCursorEnabled When true, the tool cursor represents the shape, color and texture of the
     *                                    paint it will apply; when false, a custom cursor can be specified via
     *                                    {@link #setToolCursor(Cursor)}.
     */
    public void setStrokeTrackingCursorEnabled(boolean strokeTrackingCursorEnabled) {
        this.strokeTrackingCursorEnabled = strokeTrackingCursorEnabled;
    }
}
