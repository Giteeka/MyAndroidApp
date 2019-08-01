package com.example.myandroidapp.dagger;

import javax.inject.Inject;

class War {
    private Starks starks;
    private Blotans blotans;

    @Inject
    War(Starks starks, Blotans blotans) {
        this.starks = starks;
        this.blotans = blotans;
    }

    void prepareWar() {
        starks.preparedForWar();
        blotans.preparedForWar();
    }

    void reportForeWar() {
        starks.reportForWar();
        blotans.reportForWar();
    }
}
