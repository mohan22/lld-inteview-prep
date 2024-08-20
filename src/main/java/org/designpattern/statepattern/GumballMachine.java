package org.designpattern.statepattern;

public class GumballMachine {
    NoCoinState noCoinState = new NoCoinState(this);
    HasCoinState hasCoinState = new HasCoinState(this);
    State curState;
    public GumballMachine(){
        curState = noCoinState;
    }

    public void insertCoin(){
        curState.insertCoin();
    }

    public void turnCrank(){
        curState.turnOnCrank();
    }

    public void setCurState(State curState) {
        this.curState = curState;
    }

    public State getNoCoinState(){
        return noCoinState;
    }

    public State getHasCoinState(){
        return hasCoinState;
    }
}
