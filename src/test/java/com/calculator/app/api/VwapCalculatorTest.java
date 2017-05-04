package com.calculator.app.api;

import org.junit.Before;
import org.junit.Test;

import static com.calculator.app.api.Instrument.INSTRUMENT0;
import static com.calculator.app.api.Market.MARKET0;
import static com.calculator.app.api.Market.MARKET1;
import static com.calculator.app.api.State.FIRM;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class VwapCalculatorTest {

    private Calculator calculator;

    @Before
    public void setUp() {
        calculator = new VwapCalculator();
    }

    @Test
    public void testSingleMarketPriceUpdate() {
        TwoWayPrice price = new TwoWayPriceImpl(INSTRUMENT0, FIRM, 100.5, 10.0, 103.5, 10.0);
        TwoWayPrice expected = new TwoWayPriceImpl(INSTRUMENT0, FIRM, 100.5, 10.0, 103.5, 10.0);
        TwoWayPrice twoWayPrice = calculator.applyMarketUpdate(new MarketUpdateImpl(MARKET0, price));

        assertThat(twoWayPrice, is(expected));
    }

    @Test
    public void testMultipleMarketPriceUpdateSingleInstrument() {
        calculator.applyMarketUpdate(new MarketUpdateImpl(MARKET0,
                new TwoWayPriceImpl(INSTRUMENT0, FIRM, 100.5, 10.0, 103.5, 25.0)));
        TwoWayPrice twoWayPrice = calculator.applyMarketUpdate(new MarketUpdateImpl(MARKET1,
                new TwoWayPriceImpl(INSTRUMENT0, FIRM, 101.5, 20.0, 104.5, 35.0)));
        TwoWayPrice expected = new TwoWayPriceImpl(INSTRUMENT0, FIRM,
                (100.5 * 10 + 101.5 * 20)/30,
                30.0,
                (103.5 * 25 + 104.5 * 35)/60,
                60.0);
        assertThat(twoWayPrice, is(expected));
    }
}
