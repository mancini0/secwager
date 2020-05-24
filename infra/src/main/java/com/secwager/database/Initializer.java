package com.secwager.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Initializer {

  public static void initializeDatabase(Connection conn) {
    try {
      Statement stmt = conn.createStatement();
      conn.setAutoCommit(true);
      String sql = "CREATE TABLE foo(brother text, sister text)";
      String sql1 = "INSERT INTO foo(brother, sister) values ('mike','lisa')";
      stmt.execute(sql);
      stmt.execute(sql1);
    } catch (SQLException e) {
      System.out.println(e);
    }
  }
}