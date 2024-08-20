package org.designpattern.statepattern;

public class NoCoinState  implements State{
    GumballMachine gumballMachine;
    public NoCoinState(GumballMachine gumballMachine){
        this.gumballMachine = gumballMachine;
    }
    @Override
    public void insertCoin() {
        System.out.println("coin inserted!!!");
        gumballMachine.setCurState(gumballMachine.getHasCoinState());
    }

    @Override
    public void turnOnCrank() {
        System.out.println("No coin... No turn on");
    }
}
