package com.secwager.marketdata.di;

import com.secwager.marketdata.MarketDataServiceImpl;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {MarketDataModule.class})
public interface MarketDataComponent {
  MarketDataServiceImpl buildMarketDataService();
}
