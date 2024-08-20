package org.designpattern.statepattern;

public class Client {

    public static void main(String[] args){
        GumballMachine gumballMachine = new GumballMachine();
        gumballMachine.insertCoin();
        gumballMachine.turnCrank();
        gumballMachine.turnCrank();
        gumballMachine.insertCoin();
        gumballMachine.turnCrank();
    }
}
