package com.secwager.marketdata.di;


import com.secwager.marketdata.dao.InstrumentRepo;
import com.secwager.marketdata.dao.InstrumentRepoIgniteImpl;
import dagger.Module;
import dagger.Provides;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.ignite.IgniteJdbcThinDataSource;

import javax.inject.Singleton;
import javax.sql.DataSource;

@Module
public class MarketDataIgniteModule {

    @Provides
    @Singleton
    public DataSource provideDataSource() {
        IgniteJdbcThinDataSource ds = new IgniteJdbcThinDataSource();
        try {
            ds.setUrl("jdbc:ignite:thin://51.15.242.2:30822");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ds.setDistributedJoins(true);
        return ds;
    }


    @Provides
    @Singleton
    public QueryRunner provideQueryRunner(DataSource dataSource) {
        return new QueryRunner(dataSource);
    }

    @Provides
    @Singleton
    public InstrumentRepo provideRepo(QueryRunner queryRunner) {
        return new InstrumentRepoIgniteImpl(queryRunner);
    }

}
