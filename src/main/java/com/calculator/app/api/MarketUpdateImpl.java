package com.calculator.app.api;

public class MarketUpdateImpl implements MarketUpdate {

    private final Market market;
    private final TwoWayPrice twoWayPrice;

    public MarketUpdateImpl(Market market, TwoWayPrice twoWayPrice) {
        this.market = market;
        this.twoWayPrice = twoWayPrice;
    }

    public Market getMarket() {
        return market;
    }

    public TwoWayPrice getTwoWayPrice() {
        return twoWayPrice;
    }
}
