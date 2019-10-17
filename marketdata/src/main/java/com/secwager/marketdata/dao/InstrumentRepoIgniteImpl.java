package com.secwager.marketdata.dao;

import com.secwager.marketdata.MarketData.Instrument;

import com.secwager.marketdata.MarketData.League;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InstrumentRepoIgniteImpl implements InstrumentRepo {
  private final QueryRunner queryRunner;
  final Logger log = LoggerFactory.getLogger(InstrumentRepoIgniteImpl.class);
  private final Map<String,League> leagueMap;

  @Inject
  public InstrumentRepoIgniteImpl(QueryRunner queryRunner){
    this.queryRunner=queryRunner;
    leagueMap = new HashMap<>();
    leagueMap.put("EPL",League.ENGLISH_PREMIER_LEAGUE);
    leagueMap.put("LIGUE_1",League.LIGUE_1);
    leagueMap.put("LA_LIGA",League.LA_LIGA);
    leagueMap.put("SERIE_A",League.SERIE_A);
    leagueMap.put("UCL",League.UEFA_CHAMPIONS_LEAGUE);
    leagueMap.put("UEL",League.UEFA_EUROPA_LEAGUE);
  };

  @Override
  public Set<Instrument> findAllActiveInstruments() {
    try {
      List<Map<String, Object>> rows = queryRunner
          .query("SELECT * FROM INSTRUMENT", new MapListHandler());
      log.info("Found {} rows", rows.size());
      return rows.stream().map(row -> Instrument.newBuilder()
          .setDescription("sosa")
          .setIsin((String) row.get("ISIN"))
          .setActive(true)
          .setLeague(leagueMap.get((String)row.get("LEAGUE_ID")))
          .setStartTimeEpochSeconds(((java.sql.Timestamp)row.get("GAME_TIME")).getTime())
          .build()).collect(Collectors.toSet());
    } catch (SQLException e) {
      log.error("Unable to retrieve active instruments due to exception {}", e);
      return Collections.emptySet();
    }
  }
}
