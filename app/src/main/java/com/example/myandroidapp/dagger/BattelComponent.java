package com.example.myandroidapp.dagger;

import dagger.Component;

@Component(modules = BravoModule.class)
public interface BattelComponent {

    War getWar();

    Cash getCash();

    Soldiers getSoldiers();
}
