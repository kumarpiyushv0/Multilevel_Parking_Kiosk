package Vehicle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadParkingData {

    public static String[][][] Read() {
        String filePath = "car_matrix.txt";
        int floors = 16;
        int rows = 8;
        int cols = 16;

        String[][][] parkingData = new String[floors][rows][cols];

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int z = -1, x = 0;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("Floor")) {
                    z++;
                    x = 0;
                } else if (line.isEmpty()) {
                    continue;
                } else {
                    String[] tokens = line.split("\\] \\[");
                    for (int y = 0; y < tokens.length; y++) {
                        String value = tokens[y].replace("[", "").replace("]", "").trim();
                        parkingData[z][x][y] = value.equalsIgnoreCase("Empty") ? null : value;
                    }
                    x++;
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        // ✅ Print the matrix to verify
        // for (int z = 0; z < floors; z++) {
        // System.out.println("Floor " + z + ":");
        // for (int x = 0; x < rows; x++) {
        // for (int y = 0; y < cols; y++) {
        // String val = parkingData[z][x][y];
        // System.out.print("[" + (val == null ? "Empty" : val) + "] ");
        // }
        // System.out.println();
        // }
        // System.out.println();
        // }
        return parkingData;
    }
}
