package com.secwager.cashier.di;

import com.secwager.cashier.CashierServiceImpl;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = CashierModule.class)
public interface CashierComponent {

  CashierServiceImpl buildCashierService();

}
