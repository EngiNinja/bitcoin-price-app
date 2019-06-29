package com.engininja.bitcoinpriceapp.model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.engininja.bitcoinpriceapp.common.HistoricalDataEntry;
import com.engininja.bitcoinpriceapp.webservice.JsonPlaceholderBitcoinAverageTimeApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * This class fetches and stores data for the LineChart.
 */
public class LineChartViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<HistoricalDataEntry>> historicalDataEntries;
    private Retrofit retrofit;

    public LineChartViewModel() {
        this.historicalDataEntries = new MutableLiveData<>();
    }

    public LiveData<ArrayList<HistoricalDataEntry>> getHistoricalDataEntries() {
        fetchData();
        return historicalDataEntries;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    /**
     * Fetches data and saves it in historicalDataEntries.
     */
    void fetchData() {
        final JsonPlaceholderBitcoinAverageTimeApi jsonPlaceholderBitcoinAverageTimeApi
                = retrofit.create(JsonPlaceholderBitcoinAverageTimeApi.class);

        Call<List<HistoricalDataEntry>> call = jsonPlaceholderBitcoinAverageTimeApi.getHistoricalData();

        call.enqueue(new Callback<List<HistoricalDataEntry>>() {
            @Override
            public void onResponse(Call<List<HistoricalDataEntry>> call, Response<List<HistoricalDataEntry>> response) {
                if (!response.isSuccessful()) {
                    Log.e("Line Chart Request", "Code: " + response.code());
                    return;
                }

                List<HistoricalDataEntry> responseBody = response.body();

                // only adds every 15th value because api returns around 1680 results
                ArrayList<HistoricalDataEntry> historicalDataEntries = new ArrayList<>();
                for (int i = 0; i < responseBody.size(); i++) {
                    if (i % 15 == 0) {
                        historicalDataEntries.add(responseBody.get(i));
                    }
                }
                LineChartViewModel.this.historicalDataEntries.setValue(historicalDataEntries);
            }

            @Override
            public void onFailure(Call<List<HistoricalDataEntry>> call, Throwable t) {
                Log.e("Ticker Request", "Code: " + t.getMessage());
            }
        });
    }
}

