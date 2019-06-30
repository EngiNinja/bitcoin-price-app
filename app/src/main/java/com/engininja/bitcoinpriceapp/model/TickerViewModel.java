package com.engininja.bitcoinpriceapp.model;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.engininja.bitcoinpriceapp.common.TickerBtcUsd;
import com.engininja.bitcoinpriceapp.common.ValueCallback;
import com.engininja.bitcoinpriceapp.repository.Repository;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This class fetches and stores data for the ticker in MainActivity.
 */
public class TickerViewModel extends AndroidViewModel {
    private String TAG = "TickerViewModel";
    private Repository repository;
    private MutableLiveData<TickerBtcUsd> tickerBtcUsdMutableLiveData;

    public TickerViewModel(@NonNull Application application) {
        super(application);
        tickerBtcUsdMutableLiveData = new MutableLiveData<>();
        this.repository = Repository.getInstance();
        this.startTicker();
    }

    public MutableLiveData<TickerBtcUsd> getTickerBtcUsdMutableLiveData() {
        return tickerBtcUsdMutableLiveData;
    }

    /**
     * Starts the ticker. Sends request every second.
     */
    private void startTicker() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    try {
                        repository.fetchTickerData(new ValueCallback<TickerBtcUsd>() {
                            @Override
                            public void onSuccess(TickerBtcUsd result) {
                                tickerBtcUsdMutableLiveData.setValue(result);
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                new Handler(Looper.getMainLooper())
                                        .post(() -> Toast.makeText(getApplication(), errorMessage, Toast.LENGTH_LONG).show());
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
