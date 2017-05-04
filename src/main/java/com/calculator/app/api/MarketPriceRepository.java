package com.calculator.app.api;

import java.util.EnumMap;
import java.util.Map;

public class MarketPriceRepository {

    private final Map<Market, TwoWayPrice> marketPriceCache = new EnumMap<Market, TwoWayPrice>(Market.class);

    public MarketPriceRepository() {

    }
}
