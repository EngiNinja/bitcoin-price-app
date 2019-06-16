package com.engininja.bitcoinpriceapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceholderBitcoinAverageTimeApi {
    @GET("indices/global/ticker/BTCUSD")
    Call<TickerBtcUsd> getCurrentRate();

    //BTCUSD daily
    @GET("indices/global/history/BTCUSD?period=daily&?format=json")
    Call<List<HistoricalDataEntry>> getHistoricalData();
}
