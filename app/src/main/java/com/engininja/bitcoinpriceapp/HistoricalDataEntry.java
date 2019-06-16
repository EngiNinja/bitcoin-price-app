package com.engininja.bitcoinpriceapp;

import android.os.Parcel;
import android.os.Parcelable;

class HistoricalDataEntry implements Parcelable {
    private String time;

    private double average;

    protected HistoricalDataEntry(Parcel in) {
        time = in.readString();
        average = in.readDouble();
    }

    public static final Creator<HistoricalDataEntry> CREATOR = new Creator<HistoricalDataEntry>() {
        @Override
        public HistoricalDataEntry createFromParcel(Parcel in) {
            return new HistoricalDataEntry(in);
        }

        @Override
        public HistoricalDataEntry[] newArray(int size) {
            return new HistoricalDataEntry[size];
        }
    };

    public String getTime() {
        return time;
    }

    public double getAverage() {
        return average;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(time);
        dest.writeDouble(average);
    }
}
