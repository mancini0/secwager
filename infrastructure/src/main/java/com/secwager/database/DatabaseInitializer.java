package com.secwager.database;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;

public class DatabaseInitializer {

  public static void initializeDatabase(DataSource dataSource) {
    Flyway.configure().dataSource(dataSource).load().migrate();
  }

}