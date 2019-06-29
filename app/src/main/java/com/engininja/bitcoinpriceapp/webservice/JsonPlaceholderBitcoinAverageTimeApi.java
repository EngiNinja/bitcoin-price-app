package com.engininja.bitcoinpriceapp.webservice;

import com.engininja.bitcoinpriceapp.common.HistoricalDataEntry;
import com.engininja.bitcoinpriceapp.common.TickerBtcUsd;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * This class contains the API requests the application sends.
 */
public interface JsonPlaceholderBitcoinAverageTimeApi {
    @GET("indices/global/ticker/BTCUSD")
    Call<TickerBtcUsd> getCurrentRate();

    @GET("indices/global/history/BTCUSD?period=daily&?format=json")
    Call<List<HistoricalDataEntry>> getHistoricalData();
}
