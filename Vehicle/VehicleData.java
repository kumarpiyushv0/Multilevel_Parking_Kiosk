package Vehicle;

import java.sql.SQLException;
import java.util.*;

public class VehicleData {

    static String[][][] parkingData = ReadParkingData.Read();

    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a command (add, remove, locate, relocate):");
        String command = scanner.nextLine().trim().toLowerCase();

        switch (command) {
            case "add":
                addVehicle();
                break;
            case "locate":
                locateVehicle();
                break;
            case "remove":
                removeVehicle();
                break;
            case "relocate":
                relocateVehicle();
                break;
            default:
                System.out.println("Invalid command. Please enter: add, remove, locate, or relocate.");
        }

        scanner.close();
    }

    public static void addVehicle() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the vehicle no: ");
        String vehicleNo = scanner.nextLine();

        System.out.print("Enter the vehicle model: ");
        String model = scanner.nextLine();

        System.out.print("Enter the vehicle color: ");
        String color = scanner.nextLine();

        int[] location = closestAvailable(new int[]{0, 0, 0});

        System.out.println("Assigning parking spot: Floor " + location[0] +
                ", Row " + location[1] + ", Column " + location[2]);

        String sql = "INSERT INTO Vehicles (vehicleNo, model, color, location) VALUES (" +
                "'" + vehicleNo + "', " +
                "'" + model + "', " +
                "'" + color + "', " +
                "JSON_ARRAY(" + location[0] + ", " + location[1] + ", " + location[2] + ")" +
                ");";

        if (Db.add(sql)) {
            parkingData[location[0]][location[1]][location[2]] = model + "-" + color;
            WriteParkingData.write(parkingData, location, "car_matrix.txt");
        } else {
            System.out.println("❌ Vehicle insertion failed. Parking data not updated.");
        }
    }

    public static void locateVehicle() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the vehicle no to locate: ");
        String vehicleNo = scanner.nextLine();
        Db.find(vehicleNo);
    }

    public static int[] closestAvailable(int[] start) {
        int[][] upRamps = new int[15][3];
        int[][] downRamps = new int[16][3];

        for (int i = 0; i < 15; i++) {
            upRamps[i] = new int[]{i, 2, 2};
            downRamps[i] = new int[]{i + 1, 2, 2};
        }

        return Dijkstra3D.findNearestEmpty(parkingData, start, upRamps, downRamps);
    }

    public static void removeVehicle() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your vehicle no to exit: ");
        String vehicleNo = sc.nextLine();

        int[] located = Db.find(vehicleNo);
        if (located[0] == -1) {
            System.out.println("❌ Vehicle not found.");
            return;
        }

        if (Db.remove(vehicleNo)) {
            parkingData[located[0]][located[1]][located[2]] = "Empty";
            WriteParkingData.write(parkingData, located, "car_matrix.txt");
        } else {
            System.out.println("❌ Failed to remove vehicle from database.");
        }
    }

    public static void relocateVehicle() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your vehicle no to relocate: ");
        String vehicleNo = sc.nextLine();

        int[] currLocation = Db.find(vehicleNo);
        if (currLocation[0] == -1) {
            System.out.println("❌ Vehicle not found.");
            return;
        }

        int[] newLocation = closestAvailable(currLocation);
        String[] vehicleData = Db.getVehicleData(vehicleNo);

        if (Db.update(vehicleNo, newLocation[0], newLocation[1], newLocation[2])) {
            parkingData[currLocation[0]][currLocation[1]][currLocation[2]] = "Empty";
            parkingData[newLocation[0]][newLocation[1]][newLocation[2]] = vehicleData[0] + "-" + vehicleData[1];
            WriteParkingData.write(parkingData, currLocation, "car_matrix.txt");
            WriteParkingData.write(parkingData, newLocation, "car_matrix.txt");
            System.out.println("✅ Vehicle relocated to Floor " + newLocation[0] + ", Row " + newLocation[1] + ", Col " + newLocation[2]);
        } else {
            System.out.println("❌ Failed to update location in database.");
        }
    }
}
