package Domain;


public class Parking_spot {
    private static int counter = 0;
    private int spotID;
    private boolean available;
    private int initial_time;
    private Car parked_car;

    public boolean isAvailable() {
        return available;
    }

    public int getTime() {
        return initial_time;
    }

    public int getSpotID()
    {
        return spotID;
    }

    public String toString(){
        if(isAvailable()){
            return Integer.toString(spotID) + ": Empty";
        }
        return Integer.toString(spotID) + ": " + parked_car.getPlate_nr();
    }

    public Parking_spot() {
        counter++;
        this.spotID = counter;
        this.available = true;
        this.parked_car = null;
        this.initial_time = -1;
    }

    public void park(Car car,int time)
    {
        this.parked_car = car;
        this.available = false;
        this.initial_time = time;
    }

    public void empty_park_spot()
    {
        this.available = true;
        this.parked_car = null;
        this.initial_time = -1;
    }

    public Parking_spot(int spotID, boolean available, int initial_time, Car parked_car) {
        this.spotID = spotID;
        this.available = available;
        this.initial_time = initial_time;
        this.parked_car = parked_car;
    }

    public String toFile()
    {
        if(isAvailable())
        {
            return Integer.toString(spotID) +";" + Boolean.toString(available) + ";" + Integer.toString(initial_time)+";"+ "null";
        }

        return Integer.toString(spotID) +";" + Boolean.toString(available) + ";" + Integer.toString(initial_time)+";"+ parked_car.toString();
    }
}
