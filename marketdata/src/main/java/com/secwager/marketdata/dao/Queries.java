package com.secwager.marketdata.dao;

public class Queries {
  protected static String ALL_ACTIVE_INSTRUMENTS = "SELECT\n"
      + "\tI.ISIN,\n"
      + "\tCONCAT_WS(' at ',\n"
      + "\tAWAY.LONG_NAME ,\n"
      + "\tHOME.LONG_NAME) AS DESCRIPTION,\n"
      + "\tI.LEAGUE_ID,\n"
      + "\tI.GAME_TIME\n"
      + "FROM\n"
      + "\tINSTRUMENT I\n"
      + "JOIN TEAM AWAY ON\n"
      + "\tI.AWAY_TEAM_ID = AWAY.TEAM_ID\n"
      + "\tAND I.LEAGUE_ID = AWAY.LEAGUE_ID\n"
      + "JOIN TEAM HOME ON\n"
      + "\tI.HOME_TEAM_ID = HOME.TEAM_ID\n"
      + "\tAND I.LEAGUE_ID = HOME.LEAGUE_ID\n"
      + "WHERE I.ACTIVE";


}
