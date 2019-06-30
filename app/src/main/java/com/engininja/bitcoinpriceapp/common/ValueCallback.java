package com.engininja.bitcoinpriceapp.common;

/**
 * Async operation callback that delivers a value on success.
 */
public interface ValueCallback<V> {
    void onSuccess(V value);

    void onFailure(String errorMessage);
}
