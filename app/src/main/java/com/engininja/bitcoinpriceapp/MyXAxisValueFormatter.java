package com.engininja.bitcoinpriceapp;

public class MyXAxisValueFormatter extends com.github.mikephil.charting.formatter.ValueFormatter {

    private String[] mValues;

    public MyXAxisValueFormatter(String[] values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value) {
        // "value" represents the position of the label on the axis (x or y)
        // only show every 5-th timestamp
        if (value%5==0) {
            return mValues[(int) value];
        } else {
            return "";
        }
    }
}