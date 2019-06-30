package com.engininja.bitcoinpriceapp.common;

import android.util.Log;

/**
 * This class represents the ticker instance as at https://apiv2.bitcoinaverage.com/#price-data.
 */
public class TickerBtcUsd {
    private double last;

    private Changes changes;

    public double getLast() {
        return last;
    }

    public double getDayValueChange() {
        Log.e("changes", " " + changes.getPrice().getDay());
        return changes.getPrice().getDay();
    }

    public double getDayPercentChange() {
        return changes.getPercent().getDay();
    }
}
