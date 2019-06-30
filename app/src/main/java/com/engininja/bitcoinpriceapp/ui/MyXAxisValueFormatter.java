package com.engininja.bitcoinpriceapp.ui;

/**
 * This class is responsible for formatting the xAxis values.
 */
public class MyXAxisValueFormatter extends com.github.mikephil.charting.formatter.ValueFormatter {
    private String[] values;

    public MyXAxisValueFormatter(String[] values) {
        this.values = values;
    }

    @Override
    public String getFormattedValue(float value) {
        // "value" represents the position of the label on the axis (x or y)
        // only show every 5-th timestamp
        if (value % 5 == 0) {
            return values[(int) value];
        } else {
            return "";
        }
    }
}