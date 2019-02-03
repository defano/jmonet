package com.defano.jmonet.tools.selection;

import com.defano.jmonet.tools.attributes.FillFunction;
import com.defano.jmonet.transform.image.PixelTransform;
import com.defano.jmonet.transform.image.StaticImageTransform;
import com.defano.jmonet.transform.image.Transformable;
import com.defano.jmonet.transform.image.ApplyPixelTransform;
import com.defano.jmonet.transform.image.FillTransform;

import java.awt.*;

/**
 * Represents a selection in which the pixels of the selected image can be transformed (i.e., change of brightness,
 * opacity, etc.).
 * <p>
 * Differs from TransformableSelection in that these transforms do no change the selection shape (outline) or
 * location on the canvas; only the underlying selected image.
 */
public interface TransformableImageSelection extends MutableSelection, Transformable {

    /**
     * Performs a transformation on the selected image that does not effect the dimensions, bounds or location of the
     * selection.
     *
     * @param transform The transform to perform.
     */
    default void transform(StaticImageTransform transform) {
        if (hasSelection()) {
            setSelectedImage(transform.apply(getSelectedImage()));
            setDirty();
        }
    }

    /**
     * Performs a per-pixel transformation on all pixels bound by the selection.
     *
     * @param transform The transform operation to apply
     */
    default void transform(PixelTransform transform) {
        transform(new ApplyPixelTransform(transform, getIdentitySelectionFrame()));
    }

    /**
     * Fills all transparent pixels in the selection with the given fill paint.
     *
     * @param fillPaint    The paint to fill with.
     * @param fillFunction A method to fill pixels in the selected image
     */
    default void fill(Paint fillPaint, FillFunction fillFunction) {
        transform(new FillTransform(getIdentitySelectionFrame(), fillPaint, fillFunction));
    }

}
