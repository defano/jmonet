package com.defano.jmonet.algo.transform.image;

import Jama.Matrix;
import com.defano.jmonet.algo.transform.ImageTransform;
import com.defano.jmonet.model.Quadrilateral;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Performs a rubber-sheet projection of the source image onto an arbitrary quadrilateral.
 * <p>
 * This transform implicitly applies a scale transform to the source image to assure its dimensions equal the dimensions
 * of the quadrilateral its being projected onto.
 * <p>
 * The transform further depends on certain geometric limitations that are present in the {@link Quadrilateral} class;
 * the quadrilateral cannot be self-intersecting, for example.
 * <p>
 * This magical incantation of linear algebra is as described by CorrMap:
 * http://www.corrmap.com/features/rubber-sheeting_transformation.php
 */
public class RubbersheetTransform implements ImageTransform {

    private final Quadrilateral projection;

    /**
     * Constructs a rubbersheet transform.
     *
     * @param projection The geometry on which to project the image
     */
    public RubbersheetTransform(Quadrilateral projection) {
        this.projection = projection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BufferedImage apply(BufferedImage input) {
        // Scale the source image to match the dimensions of the projection geometry
        BufferedImage source = new ScaleTransform(new Dimension(projection.getWidth(), projection.getHeight())).apply(input);

        double x1, y1, x2, y2, x3, y3, x4, y4, X1, Y1, X2, Y2, X3, Y3, X4, Y4;

        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        // Destination image geometry (defined by projection)
        x1 = Math.abs(projection.getTopLeft().getX());
        y1 = Math.abs(projection.getTopLeft().getY());
        x2 = Math.abs(projection.getTopRight().getX());
        y2 = Math.abs(projection.getTopRight().getY());
        x3 = Math.abs(projection.getBottomRight().getX());
        y3 = Math.abs(projection.getBottomRight().getY());
        x4 = Math.abs(projection.getBottomLeft().getX());
        y4 = Math.abs(projection.getBottomLeft().getY());

        // Source image geometry (assumed to be the bounds rect of projection)
        X1 = 0;
        Y1 = 0;
        X2 = sourceWidth - 1;
        Y2 = 0;
        X3 = sourceWidth - 1;
        Y3 = sourceHeight - 1;
        X4 = 0;
        Y4 = sourceHeight - 1;

        double M_a[][] =
                {{x1 * y1, x1, y1, 1, 0, 0, 0, 0},
                        {x2 * y2, x2, y2, 1, 0, 0, 0, 0},
                        {x3 * y3, x3, y3, 1, 0, 0, 0, 0},
                        {x4 * y4, x4, y4, 1, 0, 0, 0, 0},
                        {0, 0, 0, 0, x1 * y1, x1, y1, 1},
                        {0, 0, 0, 0, x2 * y2, x2, y2, 1},
                        {0, 0, 0, 0, x3 * y3, x3, y3, 1},
                        {0, 0, 0, 0, x4 * y4, x4, y4, 1}
                };

        double M_b[][] = {{X1}, {X2}, {X3}, {X4}, {Y1}, {Y2}, {Y3}, {Y4}};

        Matrix A = new Matrix(M_a);
        Matrix B = new Matrix(M_b);
        Matrix C = A.solve(B);

        double a = C.get(0, 0); // scale factor in X direction proportional to the multiplication X * Y
        double b = C.get(1, 0); // fixed scale factor in X direction with scale Y unchanged
        double c = C.get(2, 0); // scale factor in X direction proportional to Y distance from origin
        double d = C.get(3, 0); // origin translation in X direction
        double e = C.get(4, 0); // scale factor in Y direction proportional to the multiplication X * Y
        double f = C.get(5, 0); // fixed scale factor in Y direction with scale X unchanged
        double g = C.get(6, 0); // scale factor in Y direction proportional to X distance from origin
        double h = C.get(7, 0); // origin translation in Y direction

        BufferedImage output = new BufferedImage(sourceWidth, sourceHeight, BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < sourceWidth; i++) {
            for (int j = 0; j < sourceHeight; j++) {
                int x = (int) ((a * i * j) + (b * i) + (c * j) + d);
                int y = (int) ((e * i * j) + (f * i) + (g * j) + h);

                if (x > 0 && x < source.getWidth() && y > 0 && y < source.getHeight() && i > 0 && i < output.getWidth() && j > 0 && j < output.getHeight()) {
                    int p = source.getRGB(x, y);
                    output.setRGB(i, j, p);
                }
            }
        }

        return output;
    }
}
