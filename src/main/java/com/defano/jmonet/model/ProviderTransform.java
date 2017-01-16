package com.defano.jmonet.model;

public interface ProviderTransform<S, T> {
    T transform(S value);
}
