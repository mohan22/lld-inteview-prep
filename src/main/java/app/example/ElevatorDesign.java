package app.example;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * 1. Single apartment elevator or office building elevator
 * 2. Single car - single control or multiple cars - single control
 *
 *
 *
 * Car
 * - State enum : moving,idle
 * - current floor int
 * - direction enum : up,down,none
 * - upQueue
 * - downQueue
 *
 *
 * Control
 * - direction enum : up,down
 *
 *
 *
 *
 */

enum State{
    MOVING, IDLE
}

enum Direction{
    UP,DOWN,NONE
}

class Car{
    State state = State.IDLE;
    Direction direction = Direction.NONE;
    int curFloor=1;
    Set<Integer> upWard = new HashSet<>();
    Set<Integer> downWard = new HashSet<>();

    int maxFloor = 10;
    int minFloor = 1;

}

class Controller{
    Car car = new Car();

    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    void init(){
        executorService.scheduleAtFixedRate(this::move,1,2, TimeUnit.SECONDS);
    }


    void summon(int src, int dest, Direction direction){
        if(direction==Direction.UP){
            car.upWard.add(src);
            car.upWard.add(dest);
        }
        else {
            car.downWard.add(src);
            car.downWard.add(dest);
        }

        if(car.direction == Direction.NONE){
            System.out.println("Changing direction from idle to ::" + direction);
            car.direction = direction;
        }
    }

    void move(){
        System.out.println("checking..");
        if(car.direction== Direction.NONE){
            System.out.println("Sitting idle...");
            return;
        }

        if(car.direction == Direction.UP){
            if(car.upWard.contains(car.curFloor)) {
                System.out.println("Stopping at floor :: " + car.curFloor);
                car.upWard.remove(car.curFloor);
            }

            if(car.curFloor == car.maxFloor && !car.downWard.isEmpty()){
                car.direction = Direction.DOWN;
            }

            else if(car.curFloor == car.maxFloor){
                car.direction = Direction.NONE;
            }

            else if(!car.upWard.isEmpty()){
                car.curFloor++;
            }

            else{
                car.direction = Direction.DOWN;
            }
        }

        if(car.direction == Direction.DOWN){
            if(car.downWard.contains(car.curFloor)) {
                System.out.println("Stopping at floor :: " + car.curFloor);
                car.downWard.remove(car.curFloor);
            }

            if(car.curFloor == car.minFloor && !car.upWard.isEmpty()){
                car.direction = Direction.UP;
            }

            else if(car.curFloor == car.minFloor){
                car.direction = Direction.NONE;
            }

            else if(!car.downWard.isEmpty()){
                car.curFloor--;
            }

            else{
                car.direction = Direction.UP;
            }
        }

        if(car.downWard.isEmpty() && car.upWard.isEmpty()){
            System.out.println(" Going idle since no requests");
            car.direction = Direction.NONE;
        }
    }
}



public class ElevatorDesign {

    public static void main(String args[]){
        Controller controller = new Controller();
        System.out.println("init....");
        controller.init();
        System.out.println("requesting....");
        controller.summon(1,3,Direction.UP);
        controller.summon(4,2,Direction.DOWN);
        controller.summon(2,5,Direction.UP);
        controller.summon(5,8,Direction.UP);
    }


}
