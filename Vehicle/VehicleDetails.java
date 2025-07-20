package Vehicle;

public class VehicleDetails {
    public String vehicleNo;
    public String model;
    public String color;
    public int location[];

    public VehicleDetails(String vehicleNo, String model, String color, int location[]) {
        this.vehicleNo = vehicleNo;
        this.model = model;
        this.color = color;
        this.location = location;
    }

    @Override
    public String toString() {
        String locStr = (location != null && location.length == 3)
                ? "(" + location[0] + ", " + location[1] + ", " + location[2] + ")"
                : "Location not set";

        return "[Model: " + model +
                ", Color: " + color +
                ", Vehicle No: " + vehicleNo +
                ", Location: " + locStr + "]";
    }

}
