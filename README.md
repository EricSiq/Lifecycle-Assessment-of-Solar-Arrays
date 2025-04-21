# Solar Cost and Energy Calculator

## Description
A Java-based application that estimates solar panel installation costs, efficiency, and lifecycle impact using real-time solar intensity and state-wise subsidy data. It integrates MySQL for data management and supports user-driven analysis via a robust calculation engine.

## Features

- Solar Cost Estimation: Calculates solar installation costs based on user-selected panel models and dynamic state-specific subsidies.
- Efficiency Calculation: Estimates solar panel efficiency by factoring in geographic intensity data and panel specifications.
- Lifecycle Assessment: Evaluates the long-term environmental and financial impacts of installing solar panels.
- Subsidy Management: Automatically fetches and applies relevant state and central government subsidies.
- Power Purchase Agreement (PPA): Models and calculates financial benefits of PPAs for solar projects.
- Exception Handling: Includes custom exceptions for invalid geographic and state data (e.g., latitude/longitude out of range, unsupported states).
- Dynamic Data Interaction: Integrates with MySQL database to store and retrieve project and user-specific solar data.

## Project Structure

### Java Files
- Main.java – Entry point for the application.
- DatabaseHelper.java – Manages JDBC connectivity and SQL queries.
- SolarCostCalculator.java – Handles solar installation cost calculations and updates UserSelectedPanel table.
- SolarEfficiencyCalculator.java – Computes efficiency metrics for selected panels.
- SolarLifecycleAssessment.java – Performs lifecycle impact analysis.
- PowerPurchaseAgreement.java – Manages PPA-related data and calculations.
- StateSubsidy.java, SubsidyWorker.java – Manage state subsidy data and logic.
- InvalidStateException.java, LatitudeOutOfRange.java, LongitudeOutOfRange.java – Custom exception classes for input validation.

### Database
- PIJ_Project_Database with the following tables:
- StateSubsidies
- Projects
- PowerPurchaseAgreements
- SolarPanelData
- SolarPanelModels
- SolarIntensity (populated via CSV import)
- UserSelectedPanel (populated dynamically from the application)

### Connector
- mysql-connector-j-9.2.0.jar – JDBC driver for MySQL connectivity.

## Technologies Used
- Java (Core Java, OOP, JDBC)
- MySQL (Workbench, SQL, Data Import Wizard)
- CSV (for data import into SolarIntensity)
- MySQL Connector/J for JDBC-based database communication

## How to Run the Project
1. Clone the repository: git clone https://github.com/EricSiq/JavaProject-Lifecycle-Assessment-of-Solar-Arrays.git
   
2. Setup Database
- Open MySQL Workbench.
- Run the provided schema PIJ_Project_Database.
- Create the 7 required tables in the provided schema.
- Use the Table Data Import Wizard to populate SolarIntensity with solar_intensity_india.csv.
- Insert given data into other tables via SQL INSERT statements.

3. Configure JDBC
- Add mysql-connector-j-9.2.0.jar to your project's classpath.

4. Run Application
- Navigate to the project directory: cd JavaProject-Lifecycle-Assessment-of-Solar-Arrays
- Compile the Java files: javac *.java
- Run the program: java -cp ".;mysql-connector-j-9.2.0.jar" Main 

