package com.engininja.bitcoinpriceapp.repository;

import android.util.Log;

import com.engininja.bitcoinpriceapp.common.HistoricalDataEntry;
import com.engininja.bitcoinpriceapp.common.TickerBtcUsd;
import com.engininja.bitcoinpriceapp.common.ValueCallback;
import com.engininja.bitcoinpriceapp.webservice.BitcoinAverageWebService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {
    private Retrofit retrofit;
    private static Repository instance;
    private final BitcoinAverageWebService webService;

    private Repository() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://apiv2.bitcoinaverage.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webService = retrofit.create(BitcoinAverageWebService.class);
    }

    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    public void fetchTickerData(ValueCallback<TickerBtcUsd> callback) {
        Call<TickerBtcUsd> call = webService.getCurrentRate("BTCUSD");

        call.enqueue(new Callback<TickerBtcUsd>() {
            @Override
            public void onResponse(Call<TickerBtcUsd> call, Response<TickerBtcUsd> response) {
                if (!response.isSuccessful()) {
                    Log.e("Ticker Request", "Code: " + response.code());
                    return;
                }
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<TickerBtcUsd> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public void fetchLineChartData(ValueCallback<ArrayList<HistoricalDataEntry>> callback) {
        Call<List<HistoricalDataEntry>> call = webService.getHistoricalData("BTCUSD","daily", "json");

        call.enqueue(new Callback<List<HistoricalDataEntry>>() {
            @Override
            public void onResponse(Call<List<HistoricalDataEntry>> call, Response<List<HistoricalDataEntry>> response) {
                if (!response.isSuccessful()) {
                    Log.e("Line Chart Request", "Code: " + response.code());
                    return;
                }
                callback.onSuccess((ArrayList) response.body());
            }

            @Override
            public void onFailure(Call<List<HistoricalDataEntry>> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }
}
