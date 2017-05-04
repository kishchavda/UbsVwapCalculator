package com.calculator.app.api;

import lombok.Value;

@Value
public class TwoWayPriceImpl implements TwoWayPrice {
    private final Instrument instrument;
    private final State state;
    private final double bidPrice;
    private final double bidAmount;
    private final double offerPrice;
    private final double offerAmount;
}
