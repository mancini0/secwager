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
    public InstrumentRepo provideRepo(QueryRunner queryRunner) {
        return new InstrumentRepoIgniteImpl(queryRunner);
    }

}