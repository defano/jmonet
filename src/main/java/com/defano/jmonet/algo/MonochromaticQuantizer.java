package com.defano.jmonet.algo;

public class MonochromaticQuantizer implements QuantizationFunction {
    
    @Override
    public double[] quantize(double[] input) {
        double[] reduced = new double[4];
        double intensity = (input[0] + input[1] + input[2]) / 3.0;

        reduced[0] = intensity > .5 ? 1.0 : 0.0;
        reduced[1] = intensity > .5 ? 1.0 : 0.0;
        reduced[2] = intensity > .5 ? 1.0 : 0.0;
        reduced[3] = input[3];

        return reduced;
    }
}
