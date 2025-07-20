package Vehicle;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class WriteParkingData {
    public static void write(String[][][] carMatrix, int[] location, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int floor = 0; floor < carMatrix.length; floor++) {
                writer.write("Floor " + floor + ":\n");
                for (int row = 0; row < carMatrix[floor].length; row++) {
                    for (int col = 0; col < carMatrix[floor][row].length; col++) {
                        String val = carMatrix[floor][row][col];
                        writer.write("[" + (val == null ? "Empty" : val) + "] ");
                    }
                    writer.write("\n");
                }
                writer.write("\n"); // Blank line between floors
            }
            System.out.println("Matrix written to file successfully.");
        } catch (IOException e) {
            System.err.println("Error writing parking data: " + e.getMessage());
        }
    }
}
