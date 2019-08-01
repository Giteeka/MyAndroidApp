package com.example.myandroidapp.dagger;

import javax.inject.Inject;

public class Starks implements House{


    @Inject
    public Starks() {
    }

    @Override
    public void preparedForWar() {
        System.out.println("---------------- Starks are prepared for war ----------------");
    }

    @Override
    public void reportForWar() {
        System.out.println("---------------- Starks are reporting for war ----------------");

    }
}
