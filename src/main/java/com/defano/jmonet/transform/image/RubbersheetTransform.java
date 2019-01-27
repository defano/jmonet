package com.defano.jmonet.transform.image;

import Jama.Matrix;
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

        double dx1, dy1, dx2, dy2, dx3, dy3, dx4, dy4, sx1, sy1, sx2, sy2, sx3, sy3, sx4, sy4;

        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        // Destination image geometry (defined by projection)
        dx1 = Math.abs(projection.getTopLeft().getX());
        dy1 = Math.abs(projection.getTopLeft().getY());
        dx2 = Math.abs(projection.getTopRight().getX());
        dy2 = Math.abs(projection.getTopRight().getY());
        dx3 = Math.abs(projection.getBottomRight().getX());
        dy3 = Math.abs(projection.getBottomRight().getY());
        dx4 = Math.abs(projection.getBottomLeft().getX());
        dy4 = Math.abs(projection.getBottomLeft().getY());

        // Source image geometry (assumed to be the bounds rect of projection)
        sx1 = 0;
        sy1 = 0;
        sx2 = sourceWidth - 1.0;
        sy2 = 0;
        sx3 = sourceWidth - 1.0;
        sy3 = sourceHeight - 1.0;
        sx4 = 0;
        sy4 = sourceHeight - 1.0;

        double[][] arrayA =
                {{dx1 * dy1, dx1, dy1, 1, 0, 0, 0, 0},
                        {dx2 * dy2, dx2, dy2, 1, 0, 0, 0, 0},
                        {dx3 * dy3, dx3, dy3, 1, 0, 0, 0, 0},
                        {dx4 * dy4, dx4, dy4, 1, 0, 0, 0, 0},
                        {0, 0, 0, 0, dx1 * dy1, dx1, dy1, 1},
                        {0, 0, 0, 0, dx2 * dy2, dx2, dy2, 1},
                        {0, 0, 0, 0, dx3 * dy3, dx3, dy3, 1},
                        {0, 0, 0, 0, dx4 * dy4, dx4, dy4, 1}
                };

        double[][] arrayB = {{sx1}, {sx2}, {sx3}, {sx4}, {sy1}, {sy2}, {sy3}, {sy4}};

        Matrix matrixA = new Matrix(arrayA);
        Matrix matrixB = new Matrix(arrayB);
        Matrix solution = matrixA.solve(matrixB);

        double a = solution.get(0, 0); // scale factor in X direction proportional to the multiplication X * Y
        double b = solution.get(1, 0); // fixed scale factor in X direction with scale Y unchanged
        double c = solution.get(2, 0); // scale factor in X direction proportional to Y distance from origin
        double d = solution.get(3, 0); // origin translation in X direction
        double e = solution.get(4, 0); // scale factor in Y direction proportional to the multiplication X * Y
        double f = solution.get(5, 0); // fixed scale factor in Y direction with scale X unchanged
        double g = solution.get(6, 0); // scale factor in Y direction proportional to X distance from origin
        double h = solution.get(7, 0); // origin translation in Y direction

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
