package com.example.myandroidapp.dagger;

public class BattleOfBastards {
    public static void main(String[] args) {

        BattelComponent battelComponent = DaggerBattelComponent.builder().bravoModule(new BravoModule(new Cash(), new Soldiers())).build();
//        Starks starks = new Starks();
//        Blotans blotans = new Blotans();
//        War war = new War(starks,blotans);
        battelComponent.getWar().prepareWar();
        battelComponent.getWar().reportForeWar();
        Cash cash = battelComponent.getCash();
        Soldiers soldiers= battelComponent.getSoldiers();
        System.out.println(cash + "----------------");
        System.out.println(soldiers + "----------------");
    }
}
