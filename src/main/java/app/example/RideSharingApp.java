package app.example;


import java.util.*;

/**
 * Ride sharing app
 *
 * Program should take 2 or more drivers and a set of riders reqeusting rides.
 * Multiple rides can happen simultaneously.
 *
 * Algorithem for ride amount charged
 * If # of seats booked >=2 : # of km * # of seats * 0.75 * rate per km
 * Else :  # of km * rate per km
 *
 * Functions:
 * 1. Register a cab/driver
 * 2. Register a rider
 * 3. Book a ride
 * 4. Create a ride
 * 5. Generate a bill
 * 6. Check the validity of an ongoing ride for a new ride
 * 7. Update the ride status
 * 8. Fetch revenue of all the rides taken by a driver/cab
 *
 *
 *
 *
 *
 *
 *
 */


class User{
    int id;
    String name,email,phone;

}

enum DriverStatus{
    ACTIVE, IN_ACTIVE
}

enum DrivingStatus{
    ACTIVE, IN_ACTIVE
}

class Driver extends User{
    int availableSeats;
    DriverStatus status;
    DrivingStatus drivingStatus;


}

class Rider extends User{

}

class Ride{
    Rider rider;
    int src;
    int dest;

    double amount;
}

enum TripStatus{
    IN_PROGRESS, COMPLETED
}
class Trip{
    Driver driver;
    List<Ride> rideList = new ArrayList<>();
    TripStatus status;
}


class App{

    List<Driver> driverList = new ArrayList<>();
    List<Rider> riderList = new ArrayList<>();

    List<Trip> tripList = new ArrayList<>();
    Driver registerDriver(String name, String email, int seats){
        Driver driver = new Driver();
        driver.availableSeats=seats;
        driver.id= new Random().nextInt();
        driver.email = email;
        driver.name = name;
        driver.drivingStatus = DrivingStatus.IN_ACTIVE;
        driverList.add(driver);
        return driver;
    }

    Rider registerRider(String name, String email){
        Rider rider = new Rider();
        rider.email = email;
        rider.name  = name;
        riderList.add(rider);
        return rider;

    }

//    Rider getRider(String email){
//        Optional<Rider> r =  riderList.stream().filter(rider -> Objects.equals(rider.email, email)).findFirst();
//        return r.orElse(null);
//    }

    Trip book(Rider rider, int src, int dest){
        Ride ride= new Ride();
        ride.src = src;
        ride.dest =dest;
        ride.rider = rider;
        Trip trip = TripFactory.getTrip(src,dest,tripList, driverList);
        trip.rideList.add(ride);
        return trip;
    }

    void completeTrip(Trip trip){
        FareStrategy strategy = FareStrategyFactory.getStrategy(trip);
        strategy.calculate(trip);
        trip.status = TripStatus.COMPLETED;
        trip.driver.drivingStatus = DrivingStatus.IN_ACTIVE;
        System.out.println("Completing the trip :: " + trip.driver.name);
    }


    double fetchRevenue(Driver driver){

        System.out.println("Fetching revenue of driver :: " + driver.name);
        return tripList.stream().
                filter(trip -> trip.driver.id == driver.id)
                .map(trip -> trip.rideList)
                .flatMap(List::stream)
                .map(ride -> ride.amount)
                .mapToDouble(Double::doubleValue)
                .sum();

    }






}

class TripFactory{
    static Trip getTrip(int src, int dest,List<Trip> tripList,List<Driver> driverList){
        Optional<Trip> t = tripList.stream()
                .filter(trip -> trip.status == TripStatus.IN_PROGRESS)
                .filter(trip -> trip.rideList.stream().anyMatch(ride -> ride.dest == dest))
                .findFirst();

        if(t.isPresent()){
            System.out.println("allotted driver :: " + t.get().driver.name);
            return t.get();
        }

//        Optional<Driver> driver = tripList.stream()
//                .filter(trip -> trip.status != TripStatus.IN_PROGRESS)
//                .map(trip -> trip.driver)
//                .findFirst();

        Optional<Driver> driver = driverList.stream()
                .filter(driver1 -> driver1.drivingStatus == DrivingStatus.IN_ACTIVE)
                .findFirst();


        if(driver.isEmpty()){
            System.out.println("No drivers at the moment");
            return null;
        }



        Trip newTrip= new Trip();
        newTrip.driver = driver.get();
        newTrip.status = TripStatus.IN_PROGRESS;
        newTrip.driver.drivingStatus = DrivingStatus.ACTIVE;
        tripList.add(newTrip);
        System.out.println("got driver :: " + newTrip.driver.name);
        return newTrip;

    }
}

interface FareStrategy{
    void calculate(Trip trip);
}

class SimpleStrategy implements FareStrategy{
    int perKm = 10;
    @Override
    public void calculate(Trip trip) {
        int src = trip.rideList.get(0).src;
        int dst = trip.rideList.get(0).dest;

        double amount = (dst - src)*perKm;
        System.out.println(amount);
        trip.rideList.get(0).amount = amount;
    }
}
//81
class ComplexStragy implements FareStrategy{
    int perKm = 12;
    double fraction = 0.75;
    @Override
    public void calculate(Trip trip){
//        double amount = 0.0;

        for(Ride ride  : trip.rideList){
            int src = ride.src;
            int dest = ride.dest;

            ride.amount = (dest-src)*perKm*fraction;
        }
    }
}

class FareStrategyFactory{

    static FareStrategy getStrategy(Trip trip){
        if(trip.rideList.size()>=2)
            return new ComplexStragy();
        else
            return new SimpleStrategy();

    }

}
public class RideSharingApp {
    public static void main(String[] args){
        App app = new App();
        Driver driver = app.registerDriver("driver1","email1", 3);
        Driver driver1 = app.registerDriver("driver2","email1", 3);
        Rider rider = app.registerRider("rider1","email2");
        Rider rider1 = app.registerRider("rider2","email2");
        Rider rider2 = app.registerRider("rider3","email3");

        Trip trip = app.book(rider,1,10);

        Trip trip1 = app.book(rider1,5,10);

        Trip trip2 = app.book(rider2,2,8);

        app.completeTrip(trip);
        app.completeTrip(trip2);

        System.out.println("Revenue :: " + app.fetchRevenue(driver));
        System.out.println("Revenue1 :: " + app.fetchRevenue(driver1));

    }
}
