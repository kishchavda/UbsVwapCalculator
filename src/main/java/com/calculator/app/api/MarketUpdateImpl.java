package com.calculator.app.api;

import lombok.Value;

@Value
class MarketUpdateImpl implements MarketUpdate {
    private final Market market;
    private final TwoWayPrice twoWayPrice;
}
