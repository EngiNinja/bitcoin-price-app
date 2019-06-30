package com.engininja.bitcoinpriceapp.model;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.engininja.bitcoinpriceapp.common.HistoricalDataEntry;
import com.engininja.bitcoinpriceapp.common.ValueCallback;
import com.engininja.bitcoinpriceapp.repository.Repository;

import java.util.ArrayList;

/**
 * This class fetches and stores data for the LineChart.
 */
public class LineChartViewModel extends AndroidViewModel {
    private final MutableLiveData<ArrayList<HistoricalDataEntry>> historicalDataEntries;
    private Repository repository;


    public LineChartViewModel(@NonNull Application application) {
        super(application);
        historicalDataEntries = new MutableLiveData<>();
        this.repository = Repository.getInstance();
        this.fetchData();
    }

    public LiveData<ArrayList<HistoricalDataEntry>> getHistoricalDataEntries() {
        fetchData();
        return historicalDataEntries;
    }

    /**
     * Fetches data and saves it in historicalDataEntries.
     */
    private void fetchData() {
        repository.fetchLineChartData(new ValueCallback<ArrayList<HistoricalDataEntry>>() {
            @Override
            public void onSuccess(ArrayList<HistoricalDataEntry> result) {
                historicalDataEntries.setValue(result);
            }

            @Override
            public void onFailure(final String errorMessage) {
                new Handler(Looper.getMainLooper())
                        .post(() -> Toast.makeText(getApplication(), errorMessage, Toast.LENGTH_LONG).show());
            }
        });
    }
}

