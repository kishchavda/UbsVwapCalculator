package com.calculator.app.api;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class VwapCalculator implements Calculator {

    private final Map<Instrument, Map<Market, TwoWayPrice>> cache = new EnumMap<>(Instrument.class);

    VwapCalculator() {
        Arrays.stream(Instrument.values()).forEach(instrument -> {
            cache.put(instrument, new EnumMap<>(Market.class));
        });
    }

    public TwoWayPrice applyMarketUpdate(MarketUpdate twoWayMarketPrice) {
        checkNotNull(twoWayMarketPrice);
        checkNotNull(twoWayMarketPrice.getMarket());
        checkNotNull(twoWayMarketPrice.getTwoWayPrice());
        checkArgument(twoWayMarketPrice.getTwoWayPrice().getBidAmount() > 0.0);
        checkArgument(twoWayMarketPrice.getTwoWayPrice().getOfferAmount() > 0.0);
        checkArgument(twoWayMarketPrice.getTwoWayPrice().getBidPrice() >= 0.0);
        checkArgument(twoWayMarketPrice.getTwoWayPrice().getOfferPrice() >= 0.0);

        Market market = twoWayMarketPrice.getMarket();
        Instrument instrument = twoWayMarketPrice.getTwoWayPrice().getInstrument();

        Map<Market, TwoWayPrice> marketTwoWayPriceMap = cache.get(instrument);
        // overwrite existing contents as only the most recent price is kept
        marketTwoWayPriceMap.put(market, twoWayMarketPrice.getTwoWayPrice());

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
        return new TwoWayPriceImpl(instrument, twoWayMarketPrice.getTwoWayPrice().getState(),
                vwapBidPriceSum / vwapBidAmount,
                vwapBidAmount,
                vwapOfferPriceSum / vwapOfferAmount,
                vwapOfferAmount
        );
    }
}
