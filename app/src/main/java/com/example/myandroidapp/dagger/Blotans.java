package com.example.myandroidapp.dagger;

import javax.inject.Inject;

public class Blotans implements House{

    @Inject
    public Blotans(){

    }
    @Override
    public void preparedForWar() {
        System.out.println("---------------- Blotans are prepared for war ----------------");
    }

    @Override
    public void reportForWar() {
        System.out.println("---------------- Blotans are reporting for war ----------------");

    }
}
