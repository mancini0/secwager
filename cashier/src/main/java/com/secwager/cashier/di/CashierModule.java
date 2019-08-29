package com.secwager.cashier.di;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariConfig;
import org.apache.commons.dbutils.QueryRunner;

@Module
public class CashierModule {


  @Provides
  @Singleton
  public DataSource provideDataSource(){
    HikariConfig config = new HikariConfig();
    config.setUsername(System.getenv("DATABASE_USER"));
    config.setPassword(System.getenv("DATABASE_PASSWORD"));
    config.setJdbcUrl("jdbc:postgresql://my-db-svc:5432/postgres");
    config.setMaximumPoolSize(10);
    config.setConnectionTimeout(30000);
    return new HikariDataSource(config);
  }


  @Provides
  @Singleton
  public QueryRunner provideQueryRunner(DataSource dataSource){
    return new QueryRunner(dataSource);
  }




}
