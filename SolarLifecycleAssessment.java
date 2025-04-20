try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT * FROM SolarPanelData spd JOIN StateSubsidies ss ON spd.state_id = ss.state_id WHERE ss.state_name = ?"
             )) {

            pstmt.setString(1, stateName);
            ResultSet rs = pstmt.executeQuery();

            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                System.out.println("State: " + rs.getString("state_name"));
                System.out.println("Panel Lifespan: " + rs.getInt("panel_lifespan") + " years");
                System.out.println("Degradation Rate: " + rs.getFloat("degradation_rate") + "% per year");
                System.out.println("Maintenance Cost: $" + rs.getFloat("maintenance_cost") + "/year");
                System.out.println("--------------------------------------");
            }

            if (!hasResults) {
                System.out.println("No data found for the specified state.");
            }
