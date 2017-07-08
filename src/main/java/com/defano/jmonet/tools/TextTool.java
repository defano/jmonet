package com.defano.jmonet.tools;


import com.defano.jmonet.canvas.PaintCanvas;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.PaintTool;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

/**
 * Tool for drawing rasterized text on the canvas.
 */
public class TextTool extends PaintTool implements Observer {

    private JTextArea textArea;

    private Cursor textCursor = new Cursor(Cursor.TEXT_CURSOR);
    private Point textLocation;

    public TextTool() {
        super(PaintToolType.TEXT);
        setToolCursor(getTextCursor());
    }

    @Override
    public void deactivate() {
        getFontProvider().deleteObserver(this);

        if (isEditing()) {
            commitTextImage();
            removeTextArea();
        }

        super.deactivate();
    }

    @Override
    public void activate(PaintCanvas canvas) {
        super.activate(canvas);

        textArea = new JTextArea();
        textArea.setVisible(true);
        textArea.setOpaque(false);
        textArea.setBackground(new Color(0, 0, 0, 0));

        getFontProvider().addObserver(this);
    }

    @Override
    public void mousePressed(MouseEvent e, Point imageLocation) {
        if (!isEditing()) {
            textLocation = new Point((int)(imageLocation.x * getCanvas().getScale()), (int)((imageLocation.y - getFontAscent()) * getCanvas().getScale()));
            addTextArea(textLocation.x, textLocation.y);
        } else {
            commitTextImage();
            removeTextArea();
        }
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
        textArea.printAll(g);
        g.dispose();

        textArea.getCaret().setVisible(true);

        return image;
    }

    private void commitTextImage() {

        // Don't commit if user hasn't entered any text
        if (textArea.getText().trim().length() > 0) {
            Graphics g = getCanvas().getScratchImage().getGraphics();
            g.drawImage(rasterizeText(), (int)(textLocation.x / getCanvas().getScale()), (int)(textLocation.y / getCanvas().getScale()), null);
            g.dispose();

            getCanvas().commit();
        }
    }

    @Override
    public void update(Observable o, Object newValue) {
        if (newValue instanceof Font) {
            textArea.setFont((Font) newValue);
        }
    }

    private Font getScaledFont() {
        return new Font(getFont().getFamily(), getFont().getStyle(), (int) (getFont().getSize() * getCanvas().getScaleProvider().get()));
    }

    private int getFontAscent() {
        Graphics g = getCanvas().getScratchImage().getGraphics();
        FontMetrics metrics = g.getFontMetrics(getFont());
        g.dispose();

        return metrics.getAscent();
    }

    public Cursor getTextCursor() {
        return textCursor;
    }

    public void setTextCursor(Cursor textCursor) {
        this.textCursor = textCursor;
        setToolCursor(textCursor);
    }
}
