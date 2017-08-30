package com.defano.jmonet.algo;

public class ColorReductionQuantizer implements QuantizationFunction {

    private final int channelColorCount;

    public ColorReductionQuantizer(int channelColorCount) {
        this.channelColorCount = channelColorCount - 1;
    }

    @Override
    public double[] quantize(double[] input) {
        double[] reduced = new double[4];

        reduced[0] = Math.round(input[0] * (double) channelColorCount) / (double) channelColorCount;
        reduced[1] = Math.round(input[1] * (double) channelColorCount) / (double) channelColorCount;
        reduced[2] = Math.round(input[2] * (double) channelColorCount) / (double) channelColorCount;
        reduced[3] = input[3];

        return reduced;
    }
}
