package Vehicle;

import javax.swing.*;
import java.awt.*;

public class KioskUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(KioskUI::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("IntelliPark Multilevel Parking");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(820, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(15, 15));
        frame.getContentPane().setBackground(new Color(245, 248, 252));

        JLabel title = new JLabel("IntelliPark Multilevel Parking", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(24, 45, 84));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        frame.add(title, BorderLayout.NORTH);

        JPanel leftPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        leftPanel.setBackground(new Color(245, 248, 252));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Enter Vehicle Info"));

        JTextField vehicleNoField = styledField();
        JTextField modelField = styledField();
        JTextField colorField = styledField();

        leftPanel.add(styledLabel("Vehicle Number:"));
        leftPanel.add(vehicleNoField);
        leftPanel.add(styledLabel("Model:"));
        leftPanel.add(modelField);
        leftPanel.add(styledLabel("Color:"));
        leftPanel.add(colorField);

        frame.add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new GridLayout(4, 1, 20, 20));
        rightPanel.setBackground(new Color(245, 248, 252));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Actions"));

        JButton addButton = styledButton("Enter parking lot");
        JButton locateButton = styledButton("Locate your vehicle");
        JButton removeButton = styledButton("Exit parking lot");
        JButton relocateButton = styledButton("Relocate your vehicle");

        rightPanel.add(addButton);
        rightPanel.add(locateButton);
        rightPanel.add(removeButton);
        rightPanel.add(relocateButton);

        frame.add(rightPanel, BorderLayout.CENTER);

        JTextArea outputArea = new JTextArea(7, 60);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(30, 30, 30));
        outputArea.setForeground(Color.GREEN);
        outputArea.setMargin(new Insets(10, 10, 10, 10));
        outputArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("System Log"));
        frame.add(scrollPane, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            String no = vehicleNoField.getText().trim();
            String model = modelField.getText().trim();
            String color = colorField.getText().trim();

            if (no.isEmpty() || model.isEmpty() || color.isEmpty()) {
                log(outputArea, "Please fill all fields.", "error");
                return;
            }

            if (!no.matches("^[A-Z0-9\\-]+$")) {
                log(outputArea, "Invalid vehicle number. Use A-Z, 0-9, dashes only.", "warning");
                return;
            }

            try {
                int[] existing = Db.find(no);
                if (existing[0] != -1) {
                    log(outputArea, "Vehicle already exists. Try relocate/remove.", "warning");
                    return;
                }

                System.setIn(new java.io.ByteArrayInputStream((no + "\n" + model + "\n" + color + "\n").getBytes()));
                VehicleData.addVehicle();
                log(outputArea, "Vehicle added successfully.", "success");
            } catch (Exception ex) {
                log(outputArea, "Error: " + ex.getMessage(), "error");
            } finally {
                refresh(vehicleNoField, modelField, colorField);
            }
        });

        locateButton.addActionListener(e -> {
            String no = vehicleNoField.getText().trim();
            if (no.isEmpty()) {
                log(outputArea, "Enter vehicle number to locate.", "error");
                return;
            }

            try {
                int[] loc = Db.find(no);
                if (loc[0] == -1) {
                    log(outputArea, "Vehicle not found.", "warning");
                } else {
                    log(outputArea, "Location: Floor " + loc[0] + ", Row " + loc[1] + ", Column " + loc[2], "info");
                }
            } catch (Exception ex) {
                log(outputArea, "Error: " + ex.getMessage(), "error");
            } finally {
                refresh(vehicleNoField);
            }
        });

        removeButton.addActionListener(e -> {
            String no = vehicleNoField.getText().trim();
            if (no.isEmpty()) {
                log(outputArea, "Enter vehicle number to remove.", "error");
                return;
            }

            try {
                int[] loc = Db.find(no);
                if (loc[0] == -1) {
                    log(outputArea, "Vehicle not found. Nothing to remove.", "warning");
                    return;
                }

                System.setIn(new java.io.ByteArrayInputStream((no + "\n").getBytes()));
                VehicleData.removeVehicle();
                log(outputArea, "Vehicle removed.", "success");
            } catch (Exception ex) {
                log(outputArea, "Error: " + ex.getMessage(), "error");
            } finally {
                refresh(vehicleNoField);
            }
        });

        relocateButton.addActionListener(e -> {
            String no = vehicleNoField.getText().trim();
            if (no.isEmpty()) {
                log(outputArea, "Enter vehicle number to relocate.", "error");
                return;
            }

            try {
                int[] loc = Db.find(no);
                if (loc[0] == -1) {
                    log(outputArea, "Cannot relocate. Vehicle not found.", "warning");
                    return;
                }

                System.setIn(new java.io.ByteArrayInputStream((no + "\n").getBytes()));
                VehicleData.relocateVehicle();
                log(outputArea, "Vehicle relocated.", "success");
            } catch (Exception ex) {
                log(outputArea, "Error: " + ex.getMessage(), "error");
            } finally {
                refresh(vehicleNoField);
            }
        });

        frame.setVisible(true);
    }

    private static JLabel styledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.setForeground(new Color(40, 40, 40));
        return label;
    }

    private static JTextField styledField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        return field;
    }

    private static JButton styledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setBackground(new Color(0, 91, 188));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 70, 140)),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        return button;
    }

    private static void refresh(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    private static void log(JTextArea area, String message, String type) {
        switch (type) {
            case "error" -> area.setForeground(Color.RED);
            case "success" -> area.setForeground(new Color(0, 255, 0));
            case "warning" -> area.setForeground(new Color(255, 165, 0));
            case "info" -> area.setForeground(Color.CYAN);
            default -> area.setForeground(Color.WHITE);
        }
        area.setText(message);
    }
}
