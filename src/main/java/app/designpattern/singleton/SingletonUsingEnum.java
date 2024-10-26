package app.designpattern.singleton;

public enum SingletonUsingEnum {
    INSTANCE;

    public void costlyMethod(){
        System.out.println("in costly method of enum");
    }
}
