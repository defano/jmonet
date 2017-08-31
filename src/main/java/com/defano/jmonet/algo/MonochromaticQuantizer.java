package com.defano.jmonet.algo;

import java.awt.*;

public class MonochromaticQuantizer implements QuantizationFunction {
    @Override
    public double[] quantize(double[] input) {
        double[] reduced = new double[4];

        float[] hsb = Color.RGBtoHSB((int)(input[0] * 255.0), (int)(input[1] * 255.0), (int)(input[2] * 255.0), null);

        reduced[0] = hsb[2] > .5 ? 1.0 : 0.0;
        reduced[1] = hsb[2] > .5 ? 1.0 : 0.0;
        reduced[2] = hsb[2] > .5 ? 1.0 : 0.0;
        reduced[3] = input[3];

        return reduced;
    }
}
