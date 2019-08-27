package com.secwager.orderentry.di;

import com.secwager.orderentry.OrderEntryServiceImpl;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {OrderEntryModule.class})
public interface OrderEntryComponent {

  OrderEntryServiceImpl buildOrderEntryService();
}
