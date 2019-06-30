package com.engininja.bitcoinpriceapp.webservice;

import com.engininja.bitcoinpriceapp.common.HistoricalDataEntry;
import com.engininja.bitcoinpriceapp.common.TickerBtcUsd;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * This class contains the API requests the application sends.
 */
public interface BitcoinAverageWebService {
    @GET("indices/global/ticker/{exchange}")
    Call<TickerBtcUsd> getCurrentRate(@Path("exchange") String exchange);

    @GET("indices/global/history/{exchange}")
    Call<List<HistoricalDataEntry>> getHistoricalData(@Path("exchange") String exchange, @Query("period") String period, @Query("format") String format);
}
