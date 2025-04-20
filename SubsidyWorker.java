//SubsidyWorker.java

import java.sql.*;
import java.util.List;

public class SubsidyWorker extends Thread {
    private String stateName;
    private List<String> validStates;

    public SubsidyWorker(String stateName, List<String> validStates) {
        this.stateName = stateName;
        this.validStates = validStates;
    }

    @Override
    public void run() {
        try {
            // Validate the state
            if (!validStates.contains(stateName)) {
                throw new InvalidStateException("Invalid state name entered. Please enter a valid state.");
            }

            ResultSet rs = DatabaseHelper.getStateSubsidies();
            boolean hasData = false;

            while (rs.next()) {
                String state = rs.getString("state_name");

                // Check if the state matches and if the subsidy is between 15% and 50%
                if (state.equalsIgnoreCase(stateName)) {
                    float subsidyPercent = rs.getFloat("subsidy_percent");

                    if (subsidyPercent >= 15 && subsidyPercent <= 50) {
                        hasData = true;
                        System.out.println("State: " + state);
                        System.out.println("Subsidy Percentage: " + subsidyPercent + "%");
                        System.out.println("--------------------------------------");
                    }
                }
            }

            if (!hasData) {
                System.out.println("No valid subsidy information found for the entered state or the subsidy percentage is outside the range of 15% to 50%.");
            }

