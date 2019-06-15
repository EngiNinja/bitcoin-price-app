package com.engininja.bitcoinpriceapp;

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
