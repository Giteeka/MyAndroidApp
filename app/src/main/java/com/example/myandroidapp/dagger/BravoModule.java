package com.example.myandroidapp.dagger;

import dagger.Module;
import dagger.Provides;

@Module
public class BravoModule {

    Cash cash;
    Soldiers soldiers;

    public BravoModule(Cash cash, Soldiers soldiers) {
        this.cash = cash;
        this.soldiers = soldiers;
    }

    @Provides
    Cash provideCash(){
        return cash;
    }

    @Provides
    Soldiers provideSoldiers(){
        return soldiers;
    }
}
