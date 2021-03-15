package Controller;

import Domain.Car;
import Domain.CloseExeption;
import Domain.Parking_spot;
import Repository.Repository;

import java.util.*;


public class Controller {
    Repository repos;
    Queue<Car> queue;
    ArrayList<String> county = new ArrayList<String>();
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public Controller(Repository repos) {
        this.repos = repos;
        this.queue = new ArrayDeque<>();
        this.county.add("AB");
        this.county.add("AR");
        this.county.add("AG");
        this.county.add("BC");
        this.county.add("BH");
        this.county.add("BN");
        this.county.add("BT");
        this.county.add("BV");
        this.county.add("BR");
        this.county.add("B");
        this.county.add("BZ");
        this.county.add("CS");
        this.county.add("CL");
        this.county.add("CJ");
        this.county.add("CT");
        this.county.add("CV");
        this.county.add("DB");
        this.county.add("DJ");
        this.county.add("GL");
        this.county.add("GR");
        this.county.add("GJ");
        this.county.add("HR");
        this.county.add("HD");
        this.county.add("IL");
        this.county.add("IS");
        this.county.add("IF");
        this.county.add("MM");
        this.county.add("MH");
        this.county.add("MS");
        this.county.add("NT");
        this.county.add("OT");
        this.county.add("PH");
        this.county.add("SM");
        this.county.add("SJ");
        this.county.add("SB");
        this.county.add("SV");
        this.county.add("TR");
        this.county.add("TM");
        this.county.add("TL");
        this.county.add("VS");
        this.county.add("VL");
        this.county.add("VN");
    }

    private int generateRandExit()
    {
        Random rand = new Random();
        return 1 + rand.nextInt(100);
    }

    private String generatePlateNr()
    {
        StringBuilder plateNr = new StringBuilder();
        Random rand = new Random();
        int randCounty = rand.nextInt(42);
        int randNr = 1 + rand.nextInt(99);
        String randNrString = String.format("%02d",randNr);
        plateNr.append(county.get(randCounty));
        plateNr.append(" ");
        plateNr.append(randNrString);
        plateNr.append(" ");
        char[] randChar = new char[3];
        for(int i = 0; i < 3; i++) {
            randChar[i] = characters.charAt(rand.nextInt(characters.length()));
        }

        for(int i = 0; i < 3; i++) {
            plateNr.append(randChar[i]);
        }

        return plateNr.toString();
    }

    private Car generateCar()
    {
        return new Car(generatePlateNr());
    }

    public void simulateNextStep()
    {
        try{
            Car car = generateCar();
            if(repos.isFull())
            {
                queue.add(car);
                repos.simulateStep(null,generateRandExit());
            }
            else
            {
                if(queue.isEmpty()){
                    repos.simulateStep(car,generateRandExit());
                }
                else
                {
                    repos.simulateStep(queue.remove(),generateRandExit());
                }
            }
        }
        catch (CloseExeption e){
            queue.clear();
            throw e;
        }
    }

    public ArrayList<Parking_spot> getTotalSpots()
    {
        ArrayList<Parking_spot> totalArray = new ArrayList<>(50);
        totalArray.addAll(repos.getAvailable_spots());
        totalArray.addAll(repos.getOccupied_spots());
        totalArray.sort(new Comparator<Parking_spot>() {
            public int compare(Parking_spot p1, Parking_spot p2) {
                return Integer.compare(p1.getSpotID(), p2.getSpotID());
            }
        });

        return totalArray;
    }

    public String  getTime()
    {
        int div = 7 + repos.getTime()/60;
        int mod = repos.getTime()%60;
        return String.format("%02d",div)+":"+String.format("%02d",mod);
    }

    public String getProfit()
    {
        return repos.getProfit();
    }

    public Queue<Car> getQueue() {
        return queue;
    }

    public void saveToFile() {
        repos.saveToFile();
    }
}
