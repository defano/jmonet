package com.defano.jmonet.context;

import com.defano.jmonet.model.Interpolation;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

/**
 * An implementation of {@link GraphicsContext} that delegates to an AWT {@link Graphics2D} object.
 */
public class AwtGraphicsContext implements GraphicsContext {

    private final Graphics2D g;

    public AwtGraphicsContext(Graphics2D g) {
        this.g = g;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAntialiasingMode(Interpolation mode) {
        switch (mode) {
            case NONE:
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                break;
            case DEFAULT:
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
                break;
            case NEAREST_NEIGHBOR:
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                break;
            case BICUBIC:
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                break;
            case BILINEAR:
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        g.dispose();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(Shape s) {
        g.draw(s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
        return g.drawImage(img, xform, obs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
        g.drawImage(img, op, x, y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
        g.drawRenderedImage(img, xform);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
        g.drawRenderableImage(img, xform);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawString(String str, int x, int y) {
        g.drawString(str, x, y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawString(String str, float x, float y) {
        g.drawString(str, x, y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawString(AttributedCharacterIterator iterator, int x, int y) {
        g.drawString(iterator, x, y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawString(AttributedCharacterIterator iterator, float x, float y) {
        g.drawString(iterator, x, y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawGlyphVector(GlyphVector glyphVector, float x, float y) {
        g.drawGlyphVector(glyphVector, x, y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fill(Shape s) {
        g.fill(s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
        return g.hit(rect, s, onStroke);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GraphicsConfiguration getDeviceConfiguration() {
        return g.getDeviceConfiguration();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRenderingHint(RenderingHints.Key hintKey, Object hintValue) {
        g.setRenderingHint(hintKey, hintValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getRenderingHint(RenderingHints.Key hintKey) {
        return g.getRenderingHint(hintKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addRenderingHints(Map<?, ?> hints) {
        g.addRenderingHints(hints);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RenderingHints getRenderingHints() {
        return g.getRenderingHints();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRenderingHints(Map<?, ?> hints) {
        g.setRenderingHints(hints);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void translate(int x, int y) {
        g.translate(x, y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void translate(double tx, double ty) {
        g.translate(tx, ty);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rotate(double theta) {
        g.rotate(theta);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rotate(double theta, double x, double y) {
        g.rotate(theta, x, y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void scale(double sx, double sy) {
        g.scale(sx, sy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shear(double shx, double shy) {
        g.shear(shx, shy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void transform(AffineTransform tx) {
        g.transform(tx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AffineTransform getTransform() {
        return g.getTransform();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTransform(AffineTransform tx) {
        g.setTransform(tx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Paint getPaint() {
        return g.getPaint();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaint(Paint paint) {
        g.setPaint(paint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Composite getComposite() {
        return g.getComposite();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setComposite(Composite comp) {
        g.setComposite(comp);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Color getBackground() {
        return g.getBackground();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBackground(Color color) {
        g.setBackground(color);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stroke getStroke() {
        return g.getStroke();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStroke(Stroke s) {
        g.setStroke(s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clip(Shape s) {
        g.clip(s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FontRenderContext getFontRenderContext() {
        return g.getFontRenderContext();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Color getColor() {
        return g.getColor();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setColor(Color c) {
        g.setColor(c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintMode() {
        g.setPaintMode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setXORMode(Color c1) {
        g.setXORMode(c1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Font getFont() {
        return g.getFont();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFont(Font font) {
        g.setFont(font);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FontMetrics getFontMetrics() {
        return g.getFontMetrics();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FontMetrics getFontMetrics(Font f) {
        return g.getFontMetrics(f);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Rectangle getClipBounds() {
        return g.getClipBounds();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clipRect(int x, int y, int width, int height) {
        g.clipRect(x, y, width, height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setClip(int x, int y, int width, int height) {
        g.setClip(x, y, width, height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape getClip() {
        return g.getClip();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setClip(Shape clip) {
        g.setClip(clip);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
        g.copyArea(x, y, width, height, dx, dy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        g.drawLine(x1, y1, x2, y2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillRect(int x, int y, int width, int height) {
        g.fillRect(x, y, width, height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawRect(int x, int y, int width, int height) {
        g.drawRect(x, y, width, height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearRect(int x, int y, int width, int height) {
        g.clearRect(x, y, width, height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        g.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        g.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw3DRect(int x, int y, int width, int height, boolean raised) {
        g.draw3DRect(x, y, width, height, raised);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fill3DRect(int x, int y, int width, int height, boolean raised) {
        g.fill3DRect(x, y, width, height, raised);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawOval(int x, int y, int width, int height) {
        g.drawOval(x, y, width, height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillOval(int x, int y, int width, int height) {
        g.fillOval(x, y, width, height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        g.drawArc(x, y, width, height, startAngle, arcAngle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        g.fillArc(x, y, width, height, startAngle, arcAngle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
        g.drawPolyline(xPoints, yPoints, nPoints);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        g.drawPolygon(xPoints, yPoints, nPoints);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawPolygon(Polygon p) {
        g.drawPolygon(p);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        g.fillPolygon(xPoints, yPoints, nPoints);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillPolygon(Polygon p) {
        g.fillPolygon(p);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawChars(char[] data, int offset, int length, int x, int y) {
        g.drawChars(data, offset, length, x, y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawBytes(byte[] data, int offset, int length, int x, int y) {
        g.drawBytes(data, offset, length, x, y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
        return g.drawImage(img, x, y, observer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
        return g.drawImage(img, x, y, width, height, observer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
        return g.drawImage(img, x, y, bgcolor, observer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
        return g.drawImage(img, x, y, width, height, bgcolor, observer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
        return g.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
        return g.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, observer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hitClip(int x, int y, int width, int height) {
        return g.hitClip(x, y, width, height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Rectangle getClipBounds(Rectangle r) {
        return g.getClipBounds(r);
    }
}
