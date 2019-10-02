package com.secwager.marketdata.di;


import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import javax.sql.DataSource;
import org.apache.ignite.IgniteJdbcThinDataSource;
import org.apache.commons.dbutils.QueryRunner;

@Module
public class MarketDataModule {

  @Provides
  @Singleton
  public DataSource provideDataSource() {
    IgniteJdbcThinDataSource ds = new IgniteJdbcThinDataSource();
    try{
      ds.setUrl("jdbc:ignite:thin://51.15.242.2:31698");
    }catch (Exception e){
      throw new RuntimeException(e);
    }
    ds.setDistributedJoins(true);
    return ds;
  }


  @Provides
  @Singleton
  public QueryRunner provideQueryRunner(DataSource dataSource){
    return new QueryRunner(dataSource);
  }



}
