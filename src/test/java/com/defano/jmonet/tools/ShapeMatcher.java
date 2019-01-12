package com.defano.jmonet.tools;

import org.mockito.ArgumentMatcher;

import java.awt.*;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;

public class ShapeMatcher extends ArgumentMatcher<Shape> {

    private final Shape s1;
    private final double precision;

    public ShapeMatcher(Shape shape) {
        this(shape, .0001);
    }

    public ShapeMatcher(Shape shape, double precision) {
        this.s1 = shape;
        this.precision = precision;
    }

    @Override
    public boolean matches(Object o) {
        if (o instanceof Shape) {
            Shape s2 = (Shape) o;

            List<double[]> s1Points = disassemble(s1);
            List<double[]> s2Points = disassemble(s2);

            return isEquivalent(s1Points, s2Points, precision);
        }

        return false;
    }

    private boolean isEquivalent(List<double[]> l1, List<double[]> l2, double precision) {

        if (l1.size() != l2.size()) {
            return false;
        }

        for (int i = 0; i < l1.size(); i++) {
            double[] d1 = l1.get(i);
            double[] d2 = l2.get(i);

            if (d1.length != d2.length) {
                return false;
            }

            for (int j = 0; j < d1.length; j++) {
                if (Math.abs(d1[j] - d2[j]) > precision) {
                    return false;
                }
            }
        }

        return true;
    }

    private List<double[]> disassemble(Shape s) {
        ArrayList<double[]> s1Points = new ArrayList<>();
        double[] coords = new double[6];

        for (PathIterator pi = s.getPathIterator(null); !pi.isDone(); pi.next()) {
            int type = pi.currentSegment(coords);
            s1Points.add(new double[]  {type, coords[0], coords[1]});
        }

        return s1Points;
    }
}
