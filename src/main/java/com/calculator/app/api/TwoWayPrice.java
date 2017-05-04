package com.calculator.app.api;

public interface TwoWayPrice {
    Instrument getInstrument();
    State getState();
    // buy
    double getBidPrice();
    double getBidAmount();
    // sell
    double getOfferPrice();
    double getOfferAmount();
}
