package com.secwager.cashier.di;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.postgresql.ds.PGPoolingDataSource;

import java.util.Properties;

@Module
public class CashierModule {



  @Provides
  @Singleton
  public DataSource provideDataSource() {
    HikariConfig config = new HikariConfig();
    config.setAutoCommit(false);
    config.setDataSourceClassName(org.postgresql.ds.PGSimpleDataSource.class.getName());
    config.setJdbcUrl(System.getenv("JDBC_URL"));
    config.setUsername(System.getenv("DB_USER"));
    config.setUsername(System.getenv("DB_PASSWORD"));
    HikariDataSource ds = new HikariDataSource(config);
    return ds;
  }


  @Provides
  @Singleton
  public QueryRunner provideQueryRunner(DataSource dataSource){
    return new QueryRunner(dataSource);
  }




}
