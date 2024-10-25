package app.designpattern.statepattern;

public class HasCoinState implements State{

    GumballMachine gumballMachine;

    public HasCoinState(GumballMachine gumballMachine){
        this.gumballMachine = gumballMachine;
    }
    @Override
    public void insertCoin() {
        System.out.println("Already inserted!!!");
    }

    @Override
    public void turnOnCrank() {
        dispense();
        gumballMachine.setCurState(gumballMachine.getNoCoinState());
    }

    private void dispense() {
        System.out.println("Ejecting Stuff...");
    }
}
