package com.secwager.cashier.di

import com.secwager.cashier.CashierServiceImpl
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [CashierModule::class])
interface CashierComponent {
    fun buildCashierService(): CashierServiceImpl?
}