package com.defano.jmonet.model;

/**
 * A transformation from one value to another.
 * @param <S> The source value's type
 * @param <T> The resulting value's type
 */
public interface ProviderTransform<S, T> {
    T transform(S value);
}
