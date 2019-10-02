package com.secwager.marketdata.dao;

import com.secwager.marketdata.MarketData.Instrument;
import com.secwager.marketdata.MarketData.League;
import java.util.Set;

public class InstrumentRepoIgniteImpl implements InstrumentRepo {

  public InstrumentRepoIgniteImpl(){

  };
  @Override
  public Set<Instrument> findActiveInstrumentsByLeague(League league) {
    return null;
  }
}
