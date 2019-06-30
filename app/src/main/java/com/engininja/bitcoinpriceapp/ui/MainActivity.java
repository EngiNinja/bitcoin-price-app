package com.engininja.bitcoinpriceapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.engininja.bitcoinpriceapp.R;
import com.engininja.bitcoinpriceapp.common.TickerBtcUsd;
import com.engininja.bitcoinpriceapp.model.TickerViewModel;

public class MainActivity extends AppCompatActivity {
    private TextView tvPrice;
    private TextView tvPriceChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvPrice = findViewById(R.id.tvPrice);
        tvPriceChange = findViewById(R.id.tvPriceChange);

        TickerViewModel tickerViewModel = ViewModelProviders.of(this).get(TickerViewModel.class);
        tickerViewModel.getTickerBtcUsdMutableLiveData().observe(this, this::handleUpdate);
    }

    /**
     * Handles ticker's update.
     *
     * @param ticker
     */
    public void handleUpdate(TickerBtcUsd ticker) {
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

    /**
     * Starts LineChartActivity.
     *
     * @param v
     */
    public void showHistoricalData(View v) {
        Intent intent = new Intent(MainActivity.this, LineChartActivity.class);
        startActivity(intent);
    }

}
