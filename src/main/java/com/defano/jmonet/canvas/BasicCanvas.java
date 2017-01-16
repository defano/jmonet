package com.defano.jmonet.canvas;

import com.defano.jmonet.canvas.surface.AbstractJPanelSurface;
import com.defano.jmonet.model.Provider;
import com.defano.jmonet.tools.util.Geometry;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BasicCanvas extends AbstractJPanelSurface implements Canvas {

    private Point imageLocation = new Point(0, 0);
    private Provider<Double> scale = new Provider<>(1.0);
    private Provider<Integer> gridSpacing = new Provider<>(1);

    private BufferedImage image;
    private BufferedImage scratch;

    public BasicCanvas() {
        this(null);
    }

    public BasicCanvas(BufferedImage initialImage) {

        if (initialImage == null) {
            image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        } else {
            image = initialImage;
        }

        scratch = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);

        // Don't truncate image if canvas shrinks, but do grow it
        if (width < getCanvasImage().getWidth()) {
            width = getCanvasImage().getWidth();
        }

        if (height < getCanvasImage().getHeight()) {
            height = getCanvasImage().getHeight();
        }

        BufferedImage newScratch = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics newScratchGraphics = newScratch.getGraphics();
        newScratchGraphics.drawImage(getScratchImage(), 0, 0, null);
        setScratchImage(newScratch);

        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics newImageGraphics = newImage.getGraphics();
        newImageGraphics.drawImage(getCanvasImage(), 0, 0, null);
        setCanvasImage(newImage);

        newScratchGraphics.dispose();
        newImageGraphics.dispose();

        invalidateCanvas();
    }

    public void clearCanvas() {
        Graphics2D g2 = (Graphics2D) getScratchImage().getGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());
        commit(AlphaComposite.getInstance(AlphaComposite.DST_OUT, 1.0f));
    }

    protected void overlayImage(BufferedImage source, BufferedImage destination, AlphaComposite composite) {

        Graphics2D g2d = (Graphics2D) destination.getGraphics();
        g2d.setComposite(composite);
        g2d.drawImage(source, 0,0, null);
        g2d.dispose();
    }

    public int translateX(int x) {
        int gridSpacing = getGridSpacingProvider().get();
        double scale = getScaleProvider().get();

        x = Geometry.round(x, (int) (gridSpacing * scale));
        return (int) (getImageLocation().x / scale + (x / scale));
    }

    public int translateY(int y) {
        int gridSpacing = getGridSpacingProvider().get();
        double scale = getScaleProvider().get();

        y = Geometry.round(y, (int) (gridSpacing * scale));
        return (int) (getImageLocation().y / scale + (y / scale));
    }

    public BufferedImage getCanvasImage() {
        return image;
    }

    public BufferedImage getScratchImage() {
        return scratch;
    }

    @Override
    public void setCanvasImage(BufferedImage image) {
        this.image = image;
    }

    @Override
    public void setScratchImage(BufferedImage image) {
        this.scratch = image;
    }

    public void commit() {
        commit(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    /**
     * Copies the image contents of the scratch buffer to the Canvas' image.
     */
    public void commit(AlphaComposite composite) {
        BufferedImage scratchImage = getScratchImage();
        BufferedImage canvasImage = getCanvasImage();

        overlayImage(scratchImage, canvasImage, composite);
        fireObservers(this, scratchImage, canvasImage);

        clearScratch();
        invalidateCanvas();
    }

    /**
     * Creates a clean scratch buffer (replacing all pixels in the graphics context with transparent pixels).
     */
    public void clearScratch() {
        scratch = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public void setImageLocation(Point location) {
        imageLocation = location;
    }

    @Override
    public Point getImageLocation() {
        return imageLocation;
    }

    @Override
    public Provider<Double> getScaleProvider() {
        return scale;
    }

    @Override
    public void setGridSpacing(int grid) {
        this.gridSpacing.set(grid);
    }

    @Override
    public Provider<Integer> getGridSpacingProvider() {
        return gridSpacing;
    }

    @Override
    public void setScale(double scale) {
        this.scale.set(scale);
        invalidateCanvas();
    }

}
