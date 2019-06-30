package com.engininja.bitcoinpriceapp.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.engininja.bitcoinpriceapp.repository.Repository;

/**
 * This class fetches and stores data for the ticker in MainActivity.
 */
public class TickerViewModel extends AndroidViewModel {
    private String TAG = "TickerViewModel";
    private Repository repository;
    private TickerLiveData tickerBtcUsdMutableLiveData;

    public TickerViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance();
        tickerBtcUsdMutableLiveData = repository.fetchTickerData();
    }

    public TickerLiveData getTickerBtcUsdMutableLiveData() {
        return tickerBtcUsdMutableLiveData;
    }

}
