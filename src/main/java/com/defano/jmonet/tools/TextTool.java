package com.defano.jmonet.tools;


import com.defano.jmonet.canvas.PaintCanvas;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.builder.PaintTool;
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
public class TextTool extends PaintTool implements Consumer {

    private JTextArea textArea;
    private Point textModelLocation;
    private Disposable fontSubscription;
    private Disposable fontColorSubscription;

    public TextTool() {
        super(PaintToolType.TEXT);
        setToolCursor(new Cursor(Cursor.TEXT_CURSOR));
    }

    /** {@inheritDoc} */
    @Override
    public void deactivate() {
        fontSubscription.dispose();
        fontColorSubscription.dispose();

        if (isEditing()) {
            commitTextImage();
            removeTextArea();
        }

        super.deactivate();
    }

    /** {@inheritDoc} */
    @Override
    public void activate(PaintCanvas canvas) {
        super.activate(canvas);

        textArea = new JTextArea();
        textArea.setVisible(true);
        textArea.setOpaque(false);
        textArea.setBackground(new Color(0, 0, 0, 0));
        textArea.setForeground(getFontColor());
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                SwingUtilities.invokeLater(TextTool.this::completeEditing);
            }
        });

        fontSubscription = getFontObservable()
                .subscribeOn(Schedulers.computation())
                .subscribe(font -> textArea.setFont(font));

        fontColorSubscription = getFontColorObservable()
                .subscribeOn(Schedulers.computation())
                .subscribe(color -> textArea.setForeground(color));
    }

    /** {@inheritDoc} */
    @Override
    public void mousePressed(MouseEvent e, Point imageLocation) {
        if (!isEditing()) {
            getScratch().clear();

            textModelLocation = new Point(imageLocation.x,imageLocation.y - getFontAscent());
            Point textViewLocation = getCanvas().convertModelPointToView(textModelLocation);
            Dimension scaledSize = new Dimension(
                    (int)(getCanvas().getCanvasSize().width * getCanvas().getScale()) - textViewLocation.x,
                    (int)(getCanvas().getCanvasSize().height * getCanvas().getScale()) - textViewLocation.y);

            Rectangle textBounds = new Rectangle(textViewLocation, scaledSize);

            addTextArea(textBounds);
            getCanvas().repaint();

        } else {
            completeEditing();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void mouseMoved(MouseEvent e, Point imageLocation) {
        setToolCursor(getToolCursor());
    }

    public boolean isEditing() {
        return textArea.getParent() != null;
    }

    private void removeTextArea() {
        getCanvas().removeComponent(textArea);
    }

    private void addTextArea(Rectangle bounds) {
        textArea.setVisible(true);
        textArea.setBorder(new EmptyBorder(0, 0, 0, 0));
        textArea.setText("");
        textArea.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
        textArea.setFont(getScaledFont());

        getCanvas().addComponent(textArea);
        textArea.requestFocus();
    }

    private BufferedImage rasterizeText() {

        // Clear selection before rasterizing
        textArea.setSelectionStart(0);
        textArea.setSelectionEnd(0);

        textArea.getCaret().setVisible(false);
        textArea.setFont(getFont());

        BufferedImage image = new BufferedImage(textArea.getWidth(), textArea.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        applyRenderingHints(g);
        textArea.printAll(g);
        g.dispose();

        textArea.getCaret().setVisible(true);

        return image;
    }

    private void commitTextImage() {

        // Don't commit if user hasn't entered any text
        if (textArea.getText().trim().length() > 0) {
            BufferedImage text = rasterizeText();

            Graphics g = getScratch().getAddScratchGraphics(new Rectangle(textModelLocation.x, textModelLocation.y, textArea.getWidth(), textArea.getHeight()));
            g.drawImage(text, textModelLocation.x, textModelLocation.y, null);
            getCanvas().commit();
        }
    }

    private Font getScaledFont() {
        return new Font(getFont().getFamily(), getFont().getStyle(), (int) (getFont().getSize() * getCanvas().getScaleObservable().blockingFirst()));
    }

    private int getFontAscent() {
        Graphics g = getScratch().getAddScratchGraphics(null);
        FontMetrics metrics = g.getFontMetrics(getFont());

        return metrics.getAscent();
    }

    private void completeEditing() {
        commitTextImage();
        removeTextArea();
    }

    @Override
    public void accept(Object o) {
        if (o instanceof Font) {
            textArea.setFont((Font) o);
        }

        if (o instanceof Color) {
            textArea.setForeground((Color) o);
        }
    }
}
