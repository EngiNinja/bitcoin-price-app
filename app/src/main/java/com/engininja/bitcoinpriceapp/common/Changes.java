package com.engininja.bitcoinpriceapp.common;

/**
 * This class represents an instance of price change as at https://apiv2.bitcoinaverage.com/#price-data.
 */
public class Changes {
    private Percent percent;
    private Price price;

    public Percent getPercent() {
        return percent;
    }

    public Price getPrice() {
        return price;
    }
}





