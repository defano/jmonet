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
    private Point textLocation;
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
            textLocation = new Point((int)(imageLocation.x * getCanvas().getScale()), (int)((imageLocation.y - getFontAscent()) * getCanvas().getScale()));
            addTextArea(textLocation.x, textLocation.y);
            getCanvas().invalidateCanvas();
        } else {
            commitTextImage();
            removeTextArea();
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

    private void addTextArea(int x, int y) {
        int left = getCanvas().getBounds().x + x;
        int top = getCanvas().getBounds().y + y;

        textArea.setVisible(true);
        textArea.setBorder(new EmptyBorder(0, 0, 0, 0));
        textArea.setText("");
        textArea.setBounds(left, top, (int)(getCanvas().getBounds().getWidth() * getCanvas().getScale()) - left, (int)(getCanvas().getBounds().getHeight() * getCanvas().getScale()) - top);
        textArea.setFont(getScaledFont());
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                TextTool.this.mousePressed(e, new Point(0, 0));
            }
        });

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

            Graphics g = getScratch().getAddScratchGraphics(new Rectangle(textLocation.x, textLocation.y, textArea.getWidth(), textArea.getHeight()));
            g.drawImage(text, (int)(textLocation.x / getCanvas().getScale()), (int)(textLocation.y / getCanvas().getScale()), null);
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
