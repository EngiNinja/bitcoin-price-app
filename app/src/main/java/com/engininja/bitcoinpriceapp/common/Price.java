package com.engininja.bitcoinpriceapp.common;

/**
 * This class represents an instance of price history.
 */
public class Price {
    private double hour;
    private double day;
    private double week;
    private double month;
    private double month_3;
    private double month_6;
    private double year;

    public double getDay() {
        return day;
    }

    public double getHour() {
        return hour;
    }

    public double getWeek() {
        return week;
    }

    public double getMonth() {
        return month;
    }

    public double getMonth_3() {
        return month_3;
    }

    public double getMonth_6() {
        return month_6;
    }

    public double getYear() {
        return year;
    }
}
