# LLD Interview problems starting from basic

## How to Use

1. Import the project into your favourite IDE
2. Go to _src/main/java_ and select the class file and run the main method in that class


## Problems


### Simple elevator system design

- System contains Single elevator car
- Single car - single control

### Ride sharing app lld
* Program should take 2 or more drivers and a set of riders reqeusting rides.
* Multiple rides can happen simultaneously.
*
* Algorithm for ride amount charged
* If # of seats booked >=2 : (# of km)X(# of seats)X(0.75)X(rate per km)
* Else :  (# of km)X(rate per km)
*
**Functions**:
1. Register a cab/driver
2. Register a rider
3. Book a ride
4. Create a ride
5. Generate a bill
6. Check the validity of an ongoing ride for a new ride
7. Update the ride status
8. Fetch revenue of all the rides taken by a driver/cab
