package Vehicle;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class CarMatrix3D {
    public static void main(String[] args) {
        int floors = 16;
        int rows = 8;
        int cols = 16;

        String[][][] carMatrix = new String[floors][rows][cols];

        String[] models = { "Tesla", "BMW", "Toyota", "Kia", "Tata" };
        String[] colors = { "Red", "Black", "White", "Blue", "Silver" };

        Random rand = new Random();

        for (int z = 0; z < floors; z++) {
            for (int x = 0; x < rows; x++) {
                for (int y = 0; y < cols; y++) {
                    if (rand.nextBoolean()) {
                        String model = models[rand.nextInt(models.length)];
                        String color = colors[rand.nextInt(colors.length)];
                        carMatrix[z][x][y] = model + "-" + color;
                    } else {
                        carMatrix[z][x][y] = null;
                    }
                }
            }
        }

        try (FileWriter writer = new FileWriter("car_matrix.txt")) {
            for (int z = 0; z < floors; z++) {
                writer.write("Floor " + z + ":\n");
                for (int x = 0; x < rows; x++) {
                    for (int y = 0; y < cols; y++) {
                        String content = carMatrix[z][x][y] != null ? carMatrix[z][x][y] : "EMPTY";
                        writer.write("[" + content + "] ");
                    }
                    writer.write("\n");
                }
                writer.write("\n");
            }
            System.out.println("Car matrix written to car_matrix.txt");
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }
}
