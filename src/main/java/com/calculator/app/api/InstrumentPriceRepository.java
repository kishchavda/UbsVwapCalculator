package com.calculator.app.api;

import java.util.EnumMap;
import java.util.Map;

public class InstrumentPriceRepository {

    private final Map<Instrument, TwoWayPrice> cache = new EnumMap<Instrument, TwoWayPrice>(Instrument.class);
}
