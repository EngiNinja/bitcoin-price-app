package com.engininja.bitcoinpriceapp.common;

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
        return changes.getPrice().getDay();
    }

    public double getDayPercentChange() {
        return changes.getPercent().getDay();
    }
}
