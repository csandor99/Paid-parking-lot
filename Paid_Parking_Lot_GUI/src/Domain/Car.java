package Domain;

public class Car {
    private String plate_nr;
    public Car(String plate_nr)
    {
        this.plate_nr = plate_nr;
    }

    public String getPlate_nr()
    {
        return plate_nr;
    }

    public String toString ()
    {
        return plate_nr;
    }
}
