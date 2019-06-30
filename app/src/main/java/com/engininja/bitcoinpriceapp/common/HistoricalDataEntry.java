package com.engininja.bitcoinpriceapp.common;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class represents on entity of the list of changes as at https://apiv2.bitcoinaverage.com/#historical-data.
 */
public class HistoricalDataEntry implements Parcelable {
    private String time;
    private double average;

    private HistoricalDataEntry(Parcel in) {
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
        return formatTime(time);
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

    /**
     * This method parses the input time and returns a shorter version of it.
     * https://stackoverflow.com/questions/2201925/converting-iso-8601-compliant-string-to-java-util-date
     */
    private String formatTime(String input) {
        // TODO initialize as attribute
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = formatter.parse(input);
            DateTime dateTime = new DateTime(date);
            return dateTime.toString("MM/dd HH:mm");
        } catch (ParseException e) {
            Log.e("Time parsing error", e.getMessage());
            return input;
        }
    }
}
