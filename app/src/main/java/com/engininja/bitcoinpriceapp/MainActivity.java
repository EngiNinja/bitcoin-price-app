package com.engininja.bitcoinpriceapp;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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
        final JsonPlaceholderBitcoinAverageTimeApi jsonPlaceholderBitcoinAverageTimeApi = retrofit.create(JsonPlaceholderBitcoinAverageTimeApi.class);
        new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Call<TickerBtcUsd> call = jsonPlaceholderBitcoinAverageTimeApi.getCurrentRate();

                                call.enqueue(new Callback<TickerBtcUsd>() {
                                    @Override
                                    public void onResponse(Call<TickerBtcUsd> call, Response<TickerBtcUsd> response) {
                                        if (!response.isSuccessful()) {
                                            tvPrice.setText("Code: " + response.code());
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
                            }
                        });
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    Log.e("MainActivity runTicker",e.getMessage());
                }
            }
        }.start();
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
