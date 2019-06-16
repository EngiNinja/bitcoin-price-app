package com.engininja.bitcoinpriceapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView tvPrice;
    private TextView tvPriceChange;
    Retrofit retrofit;
    String secretKey = "Mjg5YjE5YmU2NjliNDBhNzlhYjhmZTk5NjAwNmFlZmZmZDFhY2ZiNWM3Njc0YmRlOWVmYTk3MjM5Zjc2YmEyYg";
    String publicKey = "YzI4MzFkYjJmMWI1NDRhYThhMzNhYjE3NWI4YjEyYjA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvPrice = findViewById(R.id.tvPrice);
        tvPriceChange = findViewById(R.id.tvPriceChange);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://apiv2.bitcoinaverage.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        runTicker();
    }

    public void runTicker() {
        final JsonPlaceholderBitcoinAverageTimeApi jsonPlaceholderBitcoinAverageTimeApi
                = retrofit.create(JsonPlaceholderBitcoinAverageTimeApi.class);
        new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        runOnUiThread(() -> {
                            Call<TickerBtcUsd> call = jsonPlaceholderBitcoinAverageTimeApi.getCurrentRate();

                            call.enqueue(new Callback<TickerBtcUsd>() {
                                @Override
                                public void onResponse(Call<TickerBtcUsd> call, Response<TickerBtcUsd> response) {
                                    if (!response.isSuccessful()) {
                                       Log.e("Ticker Request","Code: " + response.code());
                                        return;
                                    }

                                    TickerBtcUsd ticker = response.body();
                                    handleResponse(ticker);
                                }

                                @Override
                                public void onFailure(Call<TickerBtcUsd> call, Throwable t) {
                                    tvPrice.setText(t.getMessage());
                                }
                            });
                        });
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    Log.e("MainActivity runTicker", e.getMessage());
                }
            }
        }.start();
    }

    public void showHistoricalData(View v) {

        final JsonPlaceholderBitcoinAverageTimeApi jsonPlaceholderBitcoinAverageTimeApi
                = retrofit.create(JsonPlaceholderBitcoinAverageTimeApi.class);

        Call<List<HistoricalDataEntry>> call = jsonPlaceholderBitcoinAverageTimeApi.getHistoricalData();

        call.enqueue(new Callback<List<HistoricalDataEntry>>() {
            @Override
            public void onResponse(Call<List<HistoricalDataEntry>> call, Response<List<HistoricalDataEntry>> response) {
                if (!response.isSuccessful()) {
                    tvPrice.setText("Code: " + response.code());
                    return;
                }

                List<HistoricalDataEntry> responseBody = response.body();

                // only adds every 30th value because api returns around 1680 results
                List<HistoricalDataEntry> historicalDataEntries = new ArrayList<>();
                for (int i = 0; i < responseBody.size(); i++) {
                    if (i % 15 == 0) {
                        historicalDataEntries.add(responseBody.get(i));
                    }
                }

                Intent intent = new Intent(MainActivity.this, LineChartActivity.class);

                ArrayList<HistoricalDataEntry> historicalDataEntriesArrayList = new ArrayList<>();
                historicalDataEntriesArrayList.addAll(historicalDataEntries);

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("historicalData", historicalDataEntriesArrayList);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<List<HistoricalDataEntry>> call, Throwable t) {
                tvPrice.setText(t.getMessage());
            }
        });
    }

    public void handleResponse(TickerBtcUsd ticker) {
        tvPrice.setText("" + ticker.getLast() + " USD ");

        double dayValueChange = ticker.getDayValueChange();
        int color;
        if (dayValueChange >= 0) {
            color = ContextCompat.getColor(MainActivity.this, R.color.priceIncrease);
        } else {
            color = ContextCompat.getColor(MainActivity.this, R.color.priceDecrease);
        }
        tvPriceChange.setTextColor(color);
        tvPriceChange.setText("$" + dayValueChange + " (" + ticker.getDayPercentChange() + "%)");
    }
}
