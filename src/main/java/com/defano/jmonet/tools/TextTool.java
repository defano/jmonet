package com.defano.jmonet.tools;


import com.defano.jmonet.canvas.PaintCanvas;
import com.defano.jmonet.canvas.observable.SurfaceInteractionObserver;
import com.defano.jmonet.context.AwtGraphicsContext;
import com.defano.jmonet.context.GraphicsContext;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.BasicTool;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Tool for drawing rasterized text on the canvas.
 */
public class TextTool extends BasicTool implements SurfaceInteractionObserver {

    private final JTextArea textArea = new JTextArea();
    private final TextAreaMouseListener textAreaMouseListener = new TextAreaMouseListener();

    private Disposable fontSubscription;
    private Disposable fontColorSubscription;

    public TextTool() {
        super(PaintToolType.TEXT);
    }

    @Override
    public Cursor getDefaultCursor() {
        return new Cursor(Cursor.TEXT_CURSOR);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deactivate() {

        // Stop listening to tool font changes
        if (fontSubscription != null) {
            fontSubscription.dispose();
        }

        // Stop listening to tool font color changes
        if (fontColorSubscription != null) {
            fontColorSubscription.dispose();
        }

        // Stop listening for mouse-clicks in edit area
        textArea.removeMouseListener(textAreaMouseListener);

        if (isEditing()) {
            finishEditing();
        }

        super.deactivate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void activate(PaintCanvas canvas) {
        super.activate(canvas);

        // Monitor for clicks inside text edit area
        textArea.addMouseListener(textAreaMouseListener);

        // Monitor for changes to tool font selection
        fontSubscription = getAttributes().getFontObservable()
                .subscribeOn(Schedulers.computation())
                .subscribe(textArea::setFont);

        // Monitor for changes to tool font color selection
        fontColorSubscription = getAttributes().getFontColorObservable()
                .subscribeOn(Schedulers.computation())
                .subscribe(textArea::setForeground);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mousePressed(MouseEvent e, Point imageLocation) {

        if (isEditing()) {
            finishEditing();
        }

        addTextArea(imageLocation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseMoved(MouseEvent e, Point canvasLoc) {
        setToolCursor(getToolCursor());
    }

    /**
     * Determines if the text tool is currently editing an active selection of text.
     *
     * @return True when an active, mutable selection of text is being edited by the user, false otherwise.
     */
    public boolean isEditing() {
        return textArea.getParent() != null;
    }

    /**
     * Removes the text area component from the canvas.
     */
    private void removeTextArea() {
        getCanvas().removeComponent(textArea);
    }

    /**
     * Configures and adds the JTextArea component to the canvas at the specified bounds. The top-left point of these
     * bounds are typically equal to the location where the mouse was pressed, and the bottom-right point is equal to
     * the bottom-right point of the paint canvas.
     *
     * @param modelLocation Canvas image location where the text area should be added.
     */
    private void addTextArea(Point modelLocation) {
        Rectangle bounds = calculateTextAreaBounds(modelLocation);

        // Configure text area properties
        textArea.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
        textArea.setBorder(new EmptyBorder(0, 0, 0, 0));
        textArea.setText("");
        textArea.setFont(getScaledFont());
        textArea.setOpaque(false);
        textArea.setBackground(new Color(0, 0, 0, 0));      // completely transparent
        textArea.setForeground(getAttributes().getFontColor());
        textArea.setVisible(true);

        // Add it to the canvas and give it focus
        getCanvas().addComponent(textArea);
        textArea.requestFocus();
    }

    private BufferedImage rasterizeText() {

        // Clear selection before rasterizing
        textArea.setSelectionStart(0);
        textArea.setSelectionEnd(0);

        textArea.getCaret().setVisible(false);
        textArea.setFont(getAttributes().getFont());

        BufferedImage image = new BufferedImage(textArea.getWidth(), textArea.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        new AwtGraphicsContext(g).setAntialiasingMode(getAttributes().getAntiAliasing());
        textArea.printAll(g);
        g.dispose();

        textArea.getCaret().setVisible(true);

        return image;
    }

    private void commit() {

        // Don't commit if user hasn't entered any text
        if (!textArea.getText().trim().isEmpty()) {
            BufferedImage text = rasterizeText();
            Point textModelLocation = getCanvas().convertViewPointToModel(textArea.getLocation());

            GraphicsContext g = getScratch().getAddScratchGraphics(this, new Rectangle(textModelLocation.x, textModelLocation.y, textArea.getWidth(), textArea.getHeight()));
            g.drawImage(text, textModelLocation.x, textModelLocation.y, null);
            getCanvas().commit();
        }
    }

    private Font getScaledFont() {
        return new Font(
                getAttributes().getFont().getFamily(),
                getAttributes().getFont().getStyle(),
                (int) (getAttributes().getFont().getSize() * getCanvas().getScaleObservable().blockingFirst())
        );
    }

    private int getFontAscent() {
        GraphicsContext g = getScratch().getAddScratchGraphics(this, null);
        FontMetrics metrics = g.getFontMetrics(getAttributes().getFont());

        return metrics.getAscent();
    }

    private void finishEditing() {
        commit();
        removeTextArea();
    }

    private Rectangle calculateTextAreaBounds(Point imageLocation) {

        // Account for font ascent (characters will be drawn up from this location)
        Point textModelLocation = new Point(imageLocation.x, imageLocation.y - getFontAscent());
        Point textViewLocation = getCanvas().convertModelPointToView(textModelLocation);

        Dimension scaledSize = new Dimension(
                (int) (getCanvas().getCanvasSize().width * getCanvas().getScale()) - textViewLocation.x,
                (int) (getCanvas().getCanvasSize().height * getCanvas().getScale()) - textViewLocation.y
        );

        return new Rectangle(textViewLocation, scaledSize);
    }

    private class TextAreaMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            Point location = getCanvas().convertViewPointToModel(SwingUtilities.convertPoint(textArea, e.getPoint(), getCanvas().getComponent()));
            SwingUtilities.invokeLater(() -> TextTool.this.mousePressed(e, location));
        }
    }

}
