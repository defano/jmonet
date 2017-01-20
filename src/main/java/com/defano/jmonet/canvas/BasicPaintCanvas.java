package com.defano.jmonet.canvas;

import com.defano.jmonet.canvas.surface.AbstractScrollableSurface;
import com.defano.jmonet.canvas.surface.PaintSurface;
import com.defano.jmonet.model.Provider;
import com.defano.jmonet.tools.util.Geometry;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class BasicPaintCanvas extends AbstractScrollableSurface implements PaintCanvas {

    private final PaintSurface surface = new PaintSurface();

    private final java.util.List<CanvasCommitObserver> observers = new ArrayList<>();
    private final Provider<Double> scale = new Provider<>(1.0);
    private final Provider<Integer> gridSpacing = new Provider<>(1);

    private BufferedImage image;
    private BufferedImage scratch;

    public BasicPaintCanvas() {
        this(null);
    }

    public BasicPaintCanvas(BufferedImage initialImage) {
        this.surface.setDrawable(this);
        setSurface(surface);

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
        surface.setPreferredSize(new Dimension((int) (width * scale.get()), (int) (height * scale.get())));

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

        surface.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateScroll();
            }
        });

        invalidateCanvas();
    }

    public void clearCanvas() {
        Graphics2D g2 = (Graphics2D) getScratchImage().getGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());
        commit(new ChangeSet(getScratchImage(), AlphaComposite.getInstance(AlphaComposite.DST_OUT, 1.0f)));
    }

    protected void overlayImage(BufferedImage source, BufferedImage destination, AlphaComposite composite) {
        Graphics2D g2d = (Graphics2D) destination.getGraphics();
        g2d.setComposite(composite);
        g2d.drawImage(source, 0, 0, null);
        g2d.dispose();
    }

    protected void overlayChangset(ChangeSet changeSet, BufferedImage destination) {
        Graphics2D g2d = (Graphics2D) destination.getGraphics();

        for (int index = 0; index < changeSet.size(); index++) {
            g2d.setComposite(changeSet.getComposite(index));
            g2d.drawImage(changeSet.getImage(index), 0, 0, null);
        }

        g2d.dispose();
    }

    @Override
    public Point convertPointToImage(Point p) {
        return new Point(translateX(p.x), translateY(p.y));
    }

    public int translateX(int x) {
        int gridSpacing = getGridSpacingProvider().get();
        double scale = getScaleProvider().get();

        x = Geometry.round(x, (int) (gridSpacing * scale));
        return (int) (x / scale);
    }

    public int translateY(int y) {
        int gridSpacing = getGridSpacingProvider().get();
        double scale = getScaleProvider().get();

        y = Geometry.round(y, (int) (gridSpacing * scale));
        return (int) (y / scale);
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
        commit(new ChangeSet(getScratchImage(), AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)));
    }

    /**
     * Copies the image contents of the scratch buffer to the PaintCanvas' image.
     */
    public void commit(ChangeSet changeSet) {
        BufferedImage canvasImage = getCanvasImage();

        for (int index = 0; index < changeSet.size(); index++) {
            overlayImage(changeSet.getImage(index), canvasImage, changeSet.getComposite(index));
            fireObservers(this, changeSet.getImage(index), canvasImage);
        }

        clearScratch();
        invalidateCanvas();
    }

    @Override
    public void addComponent(Component component) {
        surface.addComponent(component);
    }

    @Override
    public void removeComponent(Component component) {
        surface.removeComponent(component);
    }

    @Override
    public void addSurfaceInteractionObserver(SurfaceInteractionObserver listener) {
        surface.addSurfaceInteractionObserver(listener);
    }

    @Override
    public boolean removeSurfaceInteractionObserver(SurfaceInteractionObserver listener) {
        return surface.removeSurfaceInteractionObserver(listener);
    }

    /**
     * Creates a clean scratch buffer (replacing all pixels in the graphics context with transparent pixels).
     */
    public void clearScratch() {
        scratch = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        invalidateCanvas();
    }

    @Override
    public double getScale() {
        return scale.get();
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
        surface.setPreferredSize(new Dimension((int) (getWidth() * scale), (int) (getHeight() * scale)));
        surface.revalidate();

        invalidateCanvas();
    }

    /**
     * Adds an observer to be notified of scratch buffer commits.
     *
     * @param observer The observer to be registered.
     */
    public void addObserver(CanvasCommitObserver observer) {
        observers.add(observer);
    }

    /**
     * Removes an existing observer.
     *
     * @param observer The observer to be removed.
     * @return True if the given observer was successfully unregistered; false otherwise.
     */
    public boolean removeObserver(CanvasCommitObserver observer) {
        return observers.remove(observer);
    }

    protected void fireObservers(PaintCanvas canvas, BufferedImage committedImage, BufferedImage canvasImage) {
        for (CanvasCommitObserver thisObserver : observers) {
            thisObserver.onCommit(canvas, committedImage, canvasImage);
        }
    }
}
