package com.secwager.marketdata.dao;

import com.secwager.marketdata.MarketData.Instrument;
import com.secwager.marketdata.MarketData.League;
import java.util.Set;

public interface InstrumentRepo {
  Set<Instrument> findAllActiveInstruments();

}
