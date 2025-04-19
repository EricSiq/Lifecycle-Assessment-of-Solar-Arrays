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
