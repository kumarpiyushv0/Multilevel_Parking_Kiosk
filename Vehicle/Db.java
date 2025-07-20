package Vehicle;

import java.sql.*;

public class Db {
    private static final String URL = "jdbc:mysql://localhost:3306/carData";
    private static final String USER = "root";
    private static final String PASSWORD = "pk339900";

    // Method to add, remove, or relocate vehicle data (general update queries)
    public static boolean add(String sql) throws SQLException {
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = con.createStatement()) {

            int result = st.executeUpdate(sql);
            System.out.println("Operation successful. Rows affected: " + result);
            return true;

        } catch (SQLException e) {
            System.out.println("Error executing update: " + e.getMessage());
            return false;
        }
    }

    // Method to find and locate a vehicle using a secure PreparedStatement
    public static int[] find(String vehicleNo) throws SQLException {
        String sql = """
        SELECT 
            JSON_EXTRACT(location, '$[0]') AS floor,
            JSON_EXTRACT(location, '$[1]') AS `row`,
            JSON_EXTRACT(location, '$[2]') AS col
        FROM Vehicles
        WHERE vehicleNo = ?
        """;

        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, vehicleNo);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int floor = rs.getInt("floor");
                    int row = rs.getInt("row");
                    int col = rs.getInt("col");

                    System.out.println("Vehicle " + vehicleNo + " is located at: Floor " +
                            floor + ", Row " + row + ", Column " + col);

                    return new int[]{floor, row, col};
                } else {
                    System.out.println("Vehicle not found.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error locating vehicle: " + e.getMessage());
            throw e;
        }

        return new int[]{-1, -1, -1};
    }

    public static String[] getVehicleData(String vehicleNo) throws SQLException {
        String sql = """
        SELECT 
            model, color
        FROM Vehicles
        WHERE vehicleNo = ?
        """;

        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, vehicleNo);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String model = rs.getString("model");
                    String color = rs.getString("color");

                    System.out.println("Vehicle " + vehicleNo + " Details:");
                    System.out.println("Model: " + model);
                    System.out.println("Color: " + color);

                    return new String[] { model, color };
                } else {
                    System.out.println("Vehicle not found.");
                    return new String[] { null, null };
                }
            }

        } catch (SQLException e) {
            System.out.println("Error fetching vehicle model and color: " + e.getMessage());
            throw e;
        }
    }

    public static boolean update(String vehicleNo, int floor, int row, int col) throws SQLException {
        String location = floor + "" + row + "" + col; // Combine into string like "102"
        String sql = "UPDATE Vehicles SET location = ? WHERE vehicleNo = ?";

        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, location);
            pst.setString(2, vehicleNo);

            int result = pst.executeUpdate();
            if (result > 0) {
                System.out.println("Location updated successfully for vehicle: " + vehicleNo);
                return true;
            } else {
                System.out.println("No vehicle found with vehicleNo: " + vehicleNo);
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error updating location: " + e.getMessage());
            return false;
        }
    }



    public static boolean remove(String vehicleNo) throws SQLException {
        String sql = "DELETE FROM Vehicles WHERE vehicleNo = ?";

        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, vehicleNo);

            int result = pst.executeUpdate();
            if (result > 0) {
                System.out.println("Vehicle " + vehicleNo + " removed successfully.");
                return true;
            } else {
                System.out.println("No vehicle found with vehicleNo: " + vehicleNo);
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error removing vehicle: " + e.getMessage());
            return false;
        }
    }


}
