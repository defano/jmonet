package com.defano.jmonet.tools.util;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

/**
 * A utility for creating custom tool cursors.
 */
public class CursorFactory {

    /**
     * Creates a magnifying glass cursor.
     *
     * @return The magnifying glass cursor
     */
    public static Cursor makeLassoCursor() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(CursorFactory.class.getResource("/cursors/lasso.png"));
        Point hotspot = new Point(5, 16);
        return toolkit.createCustomCursor(image, hotspot, "lasso");
    }


    /**
     * Creates a magnifying glass cursor.
     *
     * @return The magnifying glass cursor
     */
    public static Cursor makeZoomCursor() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(CursorFactory.class.getResource("/cursors/magnifier.png"));
        Point hotspot = new Point(8, 7);
        return toolkit.createCustomCursor(image, hotspot, "zoom");
    }

    /**
     * Creates a zoom-out, magnifying glass with "-" icon cursor.
     *
     * @return The zoom-out cursor.
     */
    public static Cursor makeZoomOutCursor() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(CursorFactory.class.getResource("/cursors/magnifier_minus.png"));
        Point hotspot = new Point(8, 7);
        return toolkit.createCustomCursor(image, hotspot, "zoom-out");
    }

    /**
     * Creates a zoom-in, magnifying glass with "+" icon cursor.
     *
     * @return The zoom-in cursor.
     */
    public static Cursor makeZoomInCursor() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(CursorFactory.class.getResource("/cursors/magnifier_plus.png"));
        Point hotspot = new Point(8, 7);
        return toolkit.createCustomCursor(image, hotspot, "zoom-in");
    }

    /**
     * Creates a pencil-icon cursor.
     *
     * @return The pencil icon cursor.
     */
    public static Cursor makePencilCursor() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(CursorFactory.class.getResource("/cursors/pencil.png"));
        Point hotspot = new Point(6, 17);
        return toolkit.createCustomCursor(image, hotspot, "pencil");
    }

    /**
     * Creates a paint bucket-icon cursor.
     *
     * @return The paint bucket icon cursor.
     */
    public static Cursor makeBucketCursor() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(CursorFactory.class.getResource("/cursors/fill.png"));
        Point hotspot = new Point(16, 17);
        return toolkit.createCustomCursor(image, hotspot, "bucket");
    }

    /**
     * Creates a cursor of the given stroke filled with a provided paint.
     *
     * @param stroke The stroke whose shape should become the cursor
     * @param fill   The paint or texture with which to fill the stroke
     * @return The filled, stroked cursor
     */
    public static Cursor makeBrushCursor(Stroke stroke, Paint fill) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Shape strokedShape = stroke.createStrokedShape(new Line2D.Float(0, 0, 0, 0));

        BufferedImage cursorImage = new BufferedImage(strokedShape.getBounds().width, strokedShape.getBounds().height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = cursorImage.createGraphics();
        g.setPaint(fill);
        g.setStroke(stroke);
        g.drawLine(strokedShape.getBounds().width / 2, strokedShape.getBounds().height / 2, strokedShape.getBounds().width / 2, strokedShape.getBounds().height / 2);
        g.dispose();

        Point hotspot = new Point(strokedShape.getBounds().width / 2, strokedShape.getBounds().height / 2 - 1);
        return toolkit.createCustomCursor(cursorImage, hotspot, stroke.toString());
    }

}
