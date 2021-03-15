package Repository;

import Domain.Car;
import Domain.CloseExeption;
import Domain.Parking_lot;
import Domain.Parking_spot;

import java.io.*;
import java.util.ArrayList;


public class Repository
{
    private Parking_lot parkingLot;
    private String historyData = "src/historyData.txt";
    public Repository(Parking_lot p)
    {
        parkingLot = p;

    }
    public void simulateStep(Car enteringCar, int exitID)
    {
        if(parkingLot.getCurrent_time()==parkingLot.getClosing_time()){
            parkingLot.closeParkingLot();
            throw new CloseExeption("Parking lot is closed.");
        }

        parkingLot.exitCar(exitID);

        if(enteringCar!=null) {
            parkingLot.parkCar(enteringCar);
        }
        parkingLot.incrementTime();
    }

    public int getClosingTime()
    {
        return parkingLot.getClosing_time();
    }

    public int getTime()
    {
        return parkingLot.getCurrent_time();
    }

    public String getProfit()
    {
        return parkingLot.getProfit();
    }

    public ArrayList<Parking_spot> getOccupied_spots() {
        return parkingLot.getOccupied_spots();
    }

    public ArrayList<Parking_spot> getAvailable_spots()
    {
        return parkingLot.getAvailable_spots();
    }

    public boolean isFull()
    {
        return parkingLot.isFull();
    }

    public void saveToFile()
    {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(historyData));
            for(Parking_spot p: getOccupied_spots())
            {
                writer.write(p.toFile()+"\n");
            }
            for(Parking_spot p: getAvailable_spots())
            {
                writer.write(p.toFile()+"\n");
            }
            writer.write(Integer.toString(getTime()) + ";"+ getProfit());

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                assert writer != null;
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void getFromFile()
    {
        BufferedReader objReader = null;
    try {
        String strCurrentLine;

        objReader = new BufferedReader(new FileReader(historyData));
        ArrayList<Parking_spot> arr = new ArrayList<>();
        int t= 0;
        double p=0;
        while ((strCurrentLine = objReader.readLine()) != null) {
            String[] arrOfStr = strCurrentLine.split(";", 4);
            if (arrOfStr.length == 2){
                t = Integer.parseInt(arrOfStr[0]);
                p = Double.parseDouble(arrOfStr[1]);
            }
            else if(arrOfStr.length == 4){
                arr.add(new Parking_spot(Integer.parseInt(arrOfStr[0]),Boolean.parseBoolean(arrOfStr[1]),Integer.parseInt(arrOfStr[2]),new Car(arrOfStr[3])));
            }
            else{
                return;
            }
        }

        if(arr.size() != 50){
            return;
        }

        this.parkingLot=new Parking_lot(arr,t,p);
    } catch (IOException e) {

        e.printStackTrace();

    } finally {

        try {
        if (objReader != null)
            objReader.close();
        } catch (IOException ex) {
        ex.printStackTrace();
        }
        }
    }



}
