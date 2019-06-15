package com.engininja.bitcoinpriceapp;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceholderBitcoinAverageTimeApi {
    @GET("indices/global/ticker/BTCUSD")
    Call<TickerBtcUsd> getCurrentRate();
}
