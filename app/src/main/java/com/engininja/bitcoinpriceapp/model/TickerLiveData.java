package com.engininja.bitcoinpriceapp.model;

import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.engininja.bitcoinpriceapp.common.TickerBtcUsd;
import com.engininja.bitcoinpriceapp.repository.Repository;
import com.engininja.bitcoinpriceapp.webservice.BitcoinAverageWebService;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TickerLiveData extends LiveData<TickerBtcUsd> {
    private String TAG = "TickerLiveData";
    private Repository repository;
    private Timer timer;
    private final BitcoinAverageWebService webService;

    public TickerLiveData(BitcoinAverageWebService webService) {
        this.repository = Repository.getInstance();
        this.webService = webService;
    }

    @Override
    protected void onActive() {
        timer = new Timer();
        this.startTicker();
    }

    @Override
    protected void onInactive() {
        timer.cancel();
    }

    /**
     * Starts ticker. Sends request every second.
     * TODO replace with a WebSocket.
     */
    private void startTicker() {
        final Handler handler = new Handler();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    try {
                        webService.getCurrentRate("BTCUSD").enqueue(new Callback<TickerBtcUsd>() {
                            @Override
                            public void onResponse(Call<TickerBtcUsd> call, Response<TickerBtcUsd> response) {
                                setValue(response.body());
                            }

                            @Override
                            public void onFailure(Call<TickerBtcUsd> call, Throwable t) {
                                Log.e(TAG, t.getMessage());
                            }
                        });
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 1000);
    }
}
