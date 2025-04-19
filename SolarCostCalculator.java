// SolarCostCalculator.java

import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SolarCostCalculator {
    private static double latestPanelEfficiency;
    private static double latestPanelArea;

    public static void setLatestPanelData(double efficiency, double area) {
        latestPanelEfficiency = efficiency;
        latestPanelArea = area;
    }

    public static double getLatestPanelEfficiency() {
        return latestPanelEfficiency;
    }

    public static double getLatestPanelArea() {
        return latestPanelArea;
    }

    public static void calculateAndStorePanelEfficiency(int selectedPanelId) {
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT power_output, length, width FROM SolarPanelModels WHERE panel_id = ?")) {

            pstmt.setInt(1, selectedPanelId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                double powerOutput = rs.getDouble("power_output");
                double length = rs.getDouble("length");
                double width = rs.getDouble("width"); // Fixed extra parenthesis here.

                double panelArea = length * width;
                double panelEfficiency = (powerOutput / panelArea) * 100;

                setLatestPanelData(panelEfficiency, panelArea);

                try (PreparedStatement updateStmt = conn.prepareStatement(
                    "INSERT INTO UserSelectedPanel (panel_efficiency, panel_area) VALUES (?, ?) " +
                    "ON DUPLICATE KEY UPDATE panel_efficiency = VALUES(panel_efficiency), panel_area = VALUES(panel_area)")) {

                    updateStmt.setDouble(1, panelEfficiency);
                    updateStmt.setDouble(2, panelArea);
                    updateStmt.executeUpdate();
                }
            } else {
                System.out.println("Panel ID not found in database.");
            }
