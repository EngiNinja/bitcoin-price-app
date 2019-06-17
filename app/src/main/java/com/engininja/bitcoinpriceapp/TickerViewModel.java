package com.engininja.bitcoinpriceapp;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * This class fetches and stores data for the ticker in MainActivity.
 */
public class TickerViewModel extends ViewModel {
    private final MutableLiveData<TickerBtcUsd> tickerBtcUsdMutableLiveData;
    private Retrofit retrofit;

    public TickerViewModel() {
        this.tickerBtcUsdMutableLiveData = new MutableLiveData<>();
    }

    public LiveData<TickerBtcUsd> getTickerBtcUsdMutableLiveData() {
        fetchData();
        return tickerBtcUsdMutableLiveData;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    /**
     * Sends request every second to get the fresh BTCUSD rate.
     */
    void fetchData() {
        final JsonPlaceholderBitcoinAverageTimeApi jsonPlaceholderBitcoinAverageTimeApi
                = retrofit.create(JsonPlaceholderBitcoinAverageTimeApi.class);
        new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Call<TickerBtcUsd> call = jsonPlaceholderBitcoinAverageTimeApi.getCurrentRate();

                        call.enqueue(new Callback<TickerBtcUsd>() {
                            @Override
                            public void onResponse(Call<TickerBtcUsd> call, Response<TickerBtcUsd> response) {
                                if (!response.isSuccessful()) {
                                    Log.e("Ticker Request", "Code: " + response.code());
                                    return;
                                }
                                tickerBtcUsdMutableLiveData.setValue(response.body());
                            }

                            @Override
                            public void onFailure(Call<TickerBtcUsd> call, Throwable t) {
                                Log.e("Ticker Failure", t.getMessage());
                            }
                        });
                        // FIXME
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    Log.e("MainActivity runTicker", e.getMessage());
                }
            }
        }.start();
    }
}
