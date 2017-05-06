package com.calculator.app.api;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class VwapCalculator implements Calculator {

    private final Map<Instrument, Map<Market, TwoWayPrice>> cache = new EnumMap<>(Instrument.class);

    VwapCalculator() {
        Arrays.stream(Instrument.values()).forEach(instrument -> cache.put(instrument, new EnumMap<>(Market.class)));
    }

    @Override
    public TwoWayPrice applyMarketUpdate(MarketUpdate marketUpdate) {
        // validation
        checkNotNull(marketUpdate, "Market update cannot be null");
        checkNotNull(marketUpdate.getMarket(), "Market cannot be null");
        checkNotNull(marketUpdate.getTwoWayPrice(), "Two way price cannot be null");
        checkArgument(marketUpdate.getTwoWayPrice().getBidAmount() > 0.0,
                "Bid amount must be greater than 0");
        checkArgument(marketUpdate.getTwoWayPrice().getOfferAmount() > 0.0,
                "Offer amount must be greater than 0");
        checkArgument(marketUpdate.getTwoWayPrice().getBidPrice() > 0.0,
                "Bid price must be greater than 0");
        checkArgument(marketUpdate.getTwoWayPrice().getOfferPrice() > 0.0,
                "Offer price must be greater than 0");

        Market market = marketUpdate.getMarket();
        Instrument instrument = marketUpdate.getTwoWayPrice().getInstrument();

        Map<Market, TwoWayPrice> marketTwoWayPriceMap = cache.get(instrument);
        // overwrite existing contents as only the most recent price is kept
        marketTwoWayPriceMap.put(market, marketUpdate.getTwoWayPrice());

        double vwapBidPriceSum = 0.0;
        double vwapBidAmount = 0.0;
        double vwapOfferPriceSum = 0.0;
        double vwapOfferAmount = 0.0;

        for (TwoWayPrice price : marketTwoWayPriceMap.values()) {
            vwapBidPriceSum += price.getBidPrice() * price.getBidAmount();
            vwapBidAmount += price.getBidAmount();

            vwapOfferPriceSum += price.getOfferPrice() * price.getOfferAmount();
            vwapOfferAmount += price.getOfferAmount();
        }

        // requirement of state is unclear so unused
        return new TwoWayPriceImpl(instrument, marketUpdate.getTwoWayPrice().getState(),
                vwapBidPriceSum / vwapBidAmount,
                vwapBidAmount,
                vwapOfferPriceSum / vwapOfferAmount,
                vwapOfferAmount
        );
    }
}
