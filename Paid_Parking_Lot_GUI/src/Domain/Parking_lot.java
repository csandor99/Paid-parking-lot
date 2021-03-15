package Domain;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Parking_lot {
    private ArrayList<Parking_spot> available_spots;
    private ArrayList<Parking_spot> occupied_spots;
    private int current_time;
    private int closing_time = 660;
    private double profit;


    public Parking_lot() {
        current_time = 0;
        profit = 0;
        occupied_spots = new ArrayList<>(50);
        available_spots = new ArrayList<>(50);

        for(int i=0; i<50; i++)
        {
            available_spots.add(new Parking_spot());
        }
    }

    public Parking_lot(ArrayList<Parking_spot> parkingArray, int time, double profit)
    {
        current_time = time;
        this.profit = profit;
        occupied_spots = new ArrayList<>(50);
        available_spots = new ArrayList<>(50);
        for(Parking_spot p: parkingArray)
        {
            if(p.isAvailable())
            {
                available_spots.add(p);
            }
            else {
                occupied_spots.add(p);
            }
        }

    }


    public void parkCar(Car car)
    {
        Parking_spot p = available_spots.get(0);
        available_spots.remove(0);
        p.park(car,current_time);
        occupied_spots.add(p);

    }

    public void exitCar(int spotID){
        for(int index = 0; index<occupied_spots.size(); index++)
        {
            if(occupied_spots.get(index).getSpotID()==spotID)
            {
                Parking_spot p = occupied_spots.get(index);
                occupied_spots.remove(index);
                calculateProfit(p);
                p.empty_park_spot();
                available_spots.add(p);
                return;
            }
        }
    }

    private void calculateProfit(Parking_spot p)
    {
            int x = p.getTime();
            int period = current_time - x;
            if(x >= 300 && x <=480)
            {
                if(current_time <= 480)
                {
                    profit = profit + period * 0.05;
                }
                else
                {
                    double aux = (480-x)*0.05 + (current_time-480)*0.03 ;
                    profit = profit +  aux;
                }
            }
            else if (x <300)
            {
                if(current_time >= 300 && current_time <=480)
                {
                    double aux = (300-x)*0.03 + (current_time-300)*0.05 ;
                    profit = profit +  aux;
                }
                else if( current_time < 300)
                {
                    profit = profit + period * 0.03;
                }
                else
                {
                    double aux = (300-x)*0.03 + (current_time-480)*0.05 + 180*0.05  ;
                    profit = profit +  aux;
                }
            }
            else
            {
                profit = profit + period * 0.03;
            }
    }

    public int getCurrent_time() {
        return current_time;
    }

    public String getProfit() {
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(profit);
    }

    public ArrayList<Parking_spot> getAvailable_spots() {
        return available_spots;
    }

    public ArrayList<Parking_spot> getOccupied_spots() {
        return occupied_spots;
    }

    public int getClosing_time() {
        return closing_time;
    }

    public void checkTimeLimit()
    {
        ArrayList<Integer> arr = new ArrayList<>();
        for(Parking_spot p: occupied_spots)
        {
            if(current_time-p.getTime() > 120) {
                arr.add(p.getSpotID());
            }
        }

        for(int i: arr)
        {
            exitCar(i);
        }
    }

    public boolean isFull()
    {
        return occupied_spots.size() == 50;
    }

    public void incrementTime()
    {
        checkTimeLimit();
        current_time++;
    }

    public void closeParkingLot()
    {
        ArrayList<Integer> arr = new ArrayList<>();
        for(Parking_spot p: occupied_spots)
        {
            arr.add(p.getSpotID());
        }
        for(int i: arr){
            exitCar(i);
        }
    }

}
