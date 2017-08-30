package com.defano.jmonet.algo;

import java.awt.*;

public class GrayscaleQuantizer implements QuantizationFunction {
    private final int graysCount;

    public GrayscaleQuantizer(int graysCount) {
        this.graysCount = graysCount;
    }

    @Override
    public double[] quantize(double[] input) {
        double[] reduced = new double[4];

        float[] hsb = Color.RGBtoHSB((int)(input[0] * 255.0), (int)(input[1] * 255.0), (int)(input[2] * 255.0), null);
        float brightness = Math.round(hsb[2] * (float) graysCount) / (float) graysCount;
        float correctedBrightness = brightness < 0f ? 0f : brightness > 1f ? 1f : brightness;

        Color c = new Color(Color.HSBtoRGB(0, 0, correctedBrightness));

        reduced[0] = c.getRed() / 255f;
        reduced[1] = c.getGreen() / 255f;
        reduced[2] = c.getBlue() / 255f;
        reduced[3] = input[3];

        return reduced;
    }
}
