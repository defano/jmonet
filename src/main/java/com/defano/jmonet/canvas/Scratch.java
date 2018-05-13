package com.defano.jmonet.canvas;

import com.defano.jmonet.tools.builder.PaintTool;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Scratch {

    int width, height;
    private BufferedImage addScratch;
    private BufferedImage removeScratch;
    private Graphics2D addScratchGraphics, removeScratchGraphics;
    private final PaintTool tool;

    public Scratch() {
        this(null, 1, 1);
    }

    public Scratch(PaintTool tool, int width, int height) {
        this.tool = tool;
        this.width = width;
        this.height = height;

        clear();
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;

        BufferedImage newAddScratch = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics newAddScratchGraphics = newAddScratch.getGraphics();
        newAddScratchGraphics.drawImage(getAddScratch(), 0, 0, null);
        setAddScratch(newAddScratch);
        newAddScratchGraphics.dispose();

        BufferedImage newRemoveScratch = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics newRemoveScratchGraphics = newRemoveScratch.getGraphics();
        newRemoveScratchGraphics.drawImage(getRemoveScratch(), 0, 0, null);
        setRemoveScratch(newRemoveScratch);
        newRemoveScratchGraphics.dispose();
    }

    public void clear() {
        clearAdd();
        clearRemove();
    }

    public void clearRemove() {
        setRemoveScratch(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));

        if (tool != null) {
            tool.applyRenderingHints(removeScratchGraphics);
        }
    }

    public void clearAdd() {
        setAddScratch(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));

        if (tool != null) {
            tool.applyRenderingHints(addScratchGraphics);
        }
    }

    public BufferedImage getAddScratch() {
        return addScratch;
    }

    public Graphics2D getRemoveScratchGraphics() {
        return removeScratchGraphics;
    }

    public Graphics2D getAddScratchGraphics() {
        return addScratchGraphics;
    }

    public void setAddScratch(BufferedImage addScratch) {
        if (addScratchGraphics != null) {
            addScratchGraphics.dispose();
        }

        this.addScratch = addScratch;
        this.addScratchGraphics = this.addScratch.createGraphics();
    }

    public BufferedImage getRemoveScratch() {
        return removeScratch;
    }

    public void setRemoveScratch(BufferedImage removeScratch) {
        if (removeScratchGraphics != null) {
            removeScratchGraphics.dispose();
        }

        this.removeScratch = removeScratch;
        this.removeScratchGraphics = this.removeScratch.createGraphics();
    }

    public ChangeSet getChangeSet() {
        ChangeSet changeSet = new ChangeSet(getRemoveScratch(), AlphaComposite.getInstance(AlphaComposite.DST_OUT, 1.0f));
        changeSet.addChange(getAddScratch(), AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        return changeSet;
    }
}
