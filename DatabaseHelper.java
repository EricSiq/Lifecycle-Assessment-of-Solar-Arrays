import java.sql.*;

public class DatabaseHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/PIJ_Project_Database";
    private static final String USER = "username";
    private static final String PASSWORD = "password";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found! Add the connector JAR.", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static ResultSet getProjects() throws SQLException {
        return executeQuery("SELECT * FROM Projects");
    }

    public static ResultSet getPowerPurchaseAgreements() throws SQLException {
        return executeQuery("SELECT * FROM PowerPurchaseAgreements");
    }

    public static ResultSet getSolarPanelData() throws SQLException {
        return executeQuery("SELECT * FROM SolarPanelData");
    }

    public static ResultSet getStateSubsidies() throws SQLException {
        return executeQuery("SELECT * FROM StateSubsidies");
    }

    public static ResultSet getSolarIntensity() throws SQLException {
       return executeQuery("SELECT * FROM SolarIntensity");
    }

    public static ResultSet getSolarPanelModels() throws SQLException {
       return executeQuery("SELECT panel_id, brand, model_name, power_output, cost_per_panel, length, width FROM SolarPanelModels");
    }

    public static double getSunlightHours(String state, String district) {
        String query = "SELECT avg_sunlight_hours_per_day FROM SolarIntensity WHERE state_name = ? AND district_name = ?";
        return fetchDouble(query, state, district);
    }

    private static ResultSet executeQuery(String query) throws SQLException {
        Connection con = getConnection();
        Statement stmt = con.createStatement();
        return stmt.executeQuery(query);
    }

    public static ResultSet getStateSubsidyByState(String stateName) throws SQLException {
    String query = "SELECT * FROM StateSubsidies WHERE state_name = ?";
    Connection con = getConnection();
    PreparedStatement ps = con.prepareStatement(query);
    ps.setString(1, stateName);
    return ps.executeQuery();
    }
    
    private static double fetchDouble(String query, String param1, String param2) {
        double result = 0.0;
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, param1);
            ps.setString(2, param2);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}

    
public static void addStateSubsidy(String stateName, float subsidyPercent) {
    String query = "INSERT INTO StateSubsidies (state_name, subsidy_percent) VALUES (?, ?)";
    try (Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(query)) {
        ps.setString(1, stateName);
        ps.setFloat(2, subsidyPercent);
        int rows = ps.executeUpdate();
        if (rows > 0) System.out.println("Subsidy added successfully.");
    } catch (SQLException e) {
        System.err.println("Error adding subsidy: " + e.getMessage());
    }
}

public static void viewAllStateSubsidies() {
    String query = "SELECT * FROM StateSubsidies";
    try (Connection con = getConnection();
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {
        System.out.println("\n--- List of State Subsidies ---");
        while (rs.next()) {
            System.out.printf("State: %s | Subsidy: %.2f%%\n", rs.getString("state_name"), rs.getFloat("subsidy_percent"));
        }
    } catch (SQLException e) {
        System.err.println("Error fetching subsidies: " + e.getMessage());
    }
}

public static void updateSubsidyPercentage(String stateName, float newSubsidyPercent) {
    String query = "UPDATE StateSubsidies SET subsidy_percent = ? WHERE state_name = ?";
    try (Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(query)) {
        ps.setFloat(1, newSubsidyPercent);
        ps.setString(2, stateName);
        int rows = ps.executeUpdate();
        if (rows > 0) System.out.println("Subsidy updated successfully.");
        else System.out.println("State not found.");
    } catch (SQLException e) {
        System.err.println("Error updating subsidy: " + e.getMessage());
    }
}

public static void deleteStateSubsidy(String stateName) {
    String query = "DELETE FROM StateSubsidies WHERE state_name = ?";
    try (Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(query)) {
        ps.setString(1, stateName);
        int rows = ps.executeUpdate();
        if (rows > 0) System.out.println("Subsidy deleted successfully.");
        else System.out.println("State not found.");
    } catch (SQLException e) {
        System.err.println("Error deleting subsidy: " + e.getMessage());
    }
}


public static void addProject(String name, float systemSize, float costPerKw, int stateId, float tariff, float netMetering, String type) {
    String query = "INSERT INTO Projects (name, system_size, cost_per_kw, state_id, tariff, net_metering, type) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
        ps.setString(1, name); ps.setFloat(2, systemSize); ps.setFloat(3, costPerKw);
        ps.setInt(4, stateId); ps.setFloat(5, tariff); ps.setFloat(6, netMetering); ps.setString(7, type);
        ps.executeUpdate(); System.out.println("Project added.");
    } catch (SQLException e) { e.printStackTrace(); }
}

public static void viewAllProjects() {
    try (Connection con = getConnection(); Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM Projects")) {
        while (rs.next()) {
            System.out.printf("ID: %d, Name: %s, Tariff: %.2f\n", rs.getInt("project_id"), rs.getString("name"), rs.getFloat("tariff"));
        }
    } catch (SQLException e) { e.printStackTrace(); }
}

public static void updateProjectTariff(int projectId, float newTariff) {
    try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement("UPDATE Projects SET tariff = ? WHERE project_id = ?")) {
        ps.setFloat(1, newTariff); ps.setInt(2, projectId); ps.executeUpdate(); System.out.println("Updated.");
    } catch (SQLException e) { e.printStackTrace(); }
}

public static void deleteProject(int projectId) {
    try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM Projects WHERE project_id = ?")) {
        ps.setInt(1, projectId); ps.executeUpdate(); System.out.println("Deleted.");
    } catch (SQLException e) { e.printStackTrace(); }
}


public static void addPPA(int stateId, float tariff, float escalation, int duration) {
    String query = "INSERT INTO PowerPurchaseAgreements (state_id, tariff, escalation_rate, contract_duration) VALUES (?, ?, ?, ?)";
    try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
        ps.setInt(1, stateId); ps.setFloat(2, tariff); ps.setFloat(3, escalation); ps.setInt(4, duration);
        ps.executeUpdate(); System.out.println("PPA added.");
    } catch (SQLException e) { e.printStackTrace(); }
}

public static void viewAllPPAs() {
    try (Connection con = getConnection(); Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM PowerPurchaseAgreements")) {
        while (rs.next()) {
            System.out.printf("ID: %d, Tariff: %.2f, Duration: %d\n", rs.getInt("agreement_id"), rs.getFloat("tariff"), rs.getInt("contract_duration"));
        }
    } catch (SQLException e) { e.printStackTrace(); }
}

public static void updatePPADuration(int id, int newDuration) {
    try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement("UPDATE PowerPurchaseAgreements SET contract_duration = ? WHERE agreement_id = ?")) {
        ps.setInt(1, newDuration); ps.setInt(2, id); ps.executeUpdate(); System.out.println("Updated.");
    } catch (SQLException e) { e.printStackTrace(); }
}

public static void deletePPA(int id) {
    try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM PowerPurchaseAgreements WHERE agreement_id = ?")) {
        ps.setInt(1, id); ps.executeUpdate(); System.out.println("Deleted.");
    } catch (SQLException e) { e.printStackTrace(); }
}


// CREATE
public static void addSolarPanelData(int stateId, int lifespan, float degradation, float maintenance) {
    String query = "INSERT INTO SolarPanelData (state_id, panel_lifespan, degradation_rate, maintenance_cost) VALUES (?, ?, ?, ?)";
    try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
        ps.setInt(1, stateId);
        ps.setInt(2, lifespan);
        ps.setFloat(3, degradation);
        ps.setFloat(4, maintenance);
        ps.executeUpdate();
        System.out.println("Solar panel data added.");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

// READ
public static void viewAllSolarPanelData() {
    String query = "SELECT * FROM SolarPanelData";
    try (Connection con = getConnection(); Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
        while (rs.next()) {
            System.out.printf("ID: %d | State ID: %d | Lifespan: %d years | Degradation: %.2f%% | Maintenance: Rs. %.2f/year\n",
                rs.getInt("data_id"),
                rs.getInt("state_id"),
                rs.getInt("panel_lifespan"),
                rs.getFloat("degradation_rate"),
                rs.getFloat("maintenance_cost"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

// UPDATE
public static void updateSolarPanelData(int dataId, int newLifespan, float newDegradation, float newMaintenance) {
    String query = "UPDATE SolarPanelData SET panel_lifespan = ?, degradation_rate = ?, maintenance_cost = ? WHERE data_id = ?";
    try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
        ps.setInt(1, newLifespan);
        ps.setFloat(2, newDegradation);
        ps.setFloat(3, newMaintenance);
        ps.setInt(4, dataId);
        int rows = ps.executeUpdate();
        if (rows > 0) System.out.println("Solar panel data updated.");
        else System.out.println("No record found with the given ID.");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

// DELETE
public static void deleteSolarPanelData(int dataId) {
    String query = "DELETE FROM SolarPanelData WHERE data_id = ?";
    try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
        ps.setInt(1, dataId);
        int rows = ps.executeUpdate();
        if (rows > 0) System.out.println("Solar panel data deleted.");
        else System.out.println("No record found with the given ID.");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}



// CREATE
public static void addPanelModel(String brand, String model, float cost, float length, float width, float power) {
    String query = "INSERT INTO SolarPanelModels (brand, model_name, cost_per_panel, length, width, power_output) VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
        ps.setString(1, brand);
        ps.setString(2, model);
        ps.setFloat(3, cost);
        ps.setFloat(4, length);
        ps.setFloat(5, width);
        ps.setFloat(6, power);
        ps.executeUpdate();
        System.out.println("Panel model added.");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

// READ
public static void viewAllPanelModels() {
    try (Connection con = getConnection(); Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM SolarPanelModels")) {
        while (rs.next()) {
            System.out.printf("ID: %d | Brand: %s | Model: %s | Power: %.2f kW | Cost: Rs. %.2f\n",
                    rs.getInt("panel_id"),
                    rs.getString("brand"),
                    rs.getString("model_name"),
                    rs.getFloat("power_output"),
                    rs.getFloat("cost_per_panel"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

// UPDATE
public static void updatePanelModelCost(int panelId, float newCost) {
    String query = "UPDATE SolarPanelModels SET cost_per_panel = ? WHERE panel_id = ?";
    try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
        ps.setFloat(1, newCost);
        ps.setInt(2, panelId);
        int rows = ps.executeUpdate();
        if (rows > 0) System.out.println("Panel model cost updated.");
        else System.out.println("Panel ID not found.");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

// DELETE
public static void deletePanelModel(int panelId) {
    String query = "DELETE FROM SolarPanelModels WHERE panel_id = ?";
    try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
        ps.setInt(1, panelId);
        int rows = ps.executeUpdate();
        if (rows > 0) System.out.println("Panel model deleted.");
        else System.out.println("Panel ID not found.");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}



// CREATE
public static void addSolarIntensity(String state, String district, double lat, double lon, double radiation, double sunlight) {
    String query = "INSERT INTO SolarIntensity (state_name, district_name, latitude, longitude, avg_annual_solar_radiation, avg_sunlight_hours_per_day) VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
        ps.setString(1, state);
        ps.setString(2, district);
        ps.setDouble(3, lat);
        ps.setDouble(4, lon);
        ps.setDouble(5, radiation);
        ps.setDouble(6, sunlight);
        ps.executeUpdate();
        System.out.println("Solar intensity data added.");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

// READ
public static void viewAllSolarIntensity() {
    try (Connection con = getConnection(); Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM SolarIntensity")) {
        while (rs.next()) {
            System.out.printf("ID: %d | %s, %s | Lat: %.4f | Lon: %.4f | Radiation: %.2f | Sunlight: %.2f\n",
                    rs.getInt("district_id"),
                    rs.getString("state_name"),
                    rs.getString("district_name"),
                    rs.getDouble("latitude"),
                    rs.getDouble("longitude"),
                    rs.getDouble("avg_annual_solar_radiation"),
                    rs.getDouble("avg_sunlight_hours_per_day"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

// UPDATE
public static void updateSunlightHours(int districtId, double newHours) {
    String query = "UPDATE SolarIntensity SET avg_sunlight_hours_per_day = ? WHERE district_id = ?";
    try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
        ps.setDouble(1, newHours);
        ps.setInt(2, districtId);
        int rows = ps.executeUpdate();
        if (rows > 0) System.out.println("Sunlight hours updated.");
        else System.out.println("District ID not found.");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

// DELETE
public static void deleteSolarIntensity(int districtId) {
    String query = "DELETE FROM SolarIntensity WHERE district_id = ?";
    try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
        ps.setInt(1, districtId);
        int rows = ps.executeUpdate();
        if (rows > 0) System.out.println("Solar intensity record deleted.");
        else System.out.println("District ID not found.");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}



// CREATE
public static void addUserSelectedPanel(double efficiency, double area) {
    String query = "INSERT INTO UserSelectedPanel (panel_efficiency, panel_area) VALUES (?, ?)";
    try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
        ps.setDouble(1, efficiency);
        ps.setDouble(2, area);
        ps.executeUpdate();
        System.out.println("User-selected panel recorded.");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

// READ
public static void viewAllUserSelectedPanels() {
    try (Connection con = getConnection(); Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM UserSelectedPanel")) {
        while (rs.next()) {
            System.out.printf("ID: %d | Efficiency: %.2f%% | Area: %.2f m² | Timestamp: %s\n",
                    rs.getInt("id"),
                    rs.getDouble("panel_efficiency"),
                    rs.getDouble("panel_area"),
                    rs.getTimestamp("timestamp"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

// UPDATE
public static void updateUserSelectedPanel(int id, double newEfficiency, double newArea) {
    String query = "UPDATE UserSelectedPanel SET panel_efficiency = ?, panel_area = ? WHERE id = ?";
    try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
        ps.setDouble(1, newEfficiency);
        ps.setDouble(2, newArea);
        ps.setInt(3, id);
        int rows = ps.executeUpdate();
        if (rows > 0) System.out.println("Panel selection updated.");
        else System.out.println("Record not found.");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

// DELETE
public static void deleteUserSelectedPanel(int id) {
    String query = "DELETE FROM UserSelectedPanel WHERE id = ?";
    try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
        ps.setInt(1, id);
        int rows = ps.executeUpdate();
        if (rows > 0) System.out.println("Panel selection deleted.");
        else System.out.println("Record not found.");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


