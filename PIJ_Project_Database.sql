CREATE DATABASE PIJ_Project_Database;
USE PIJ_Project_Database;

drop database PIJ_Project_Database;

CREATE TABLE StateSubsidies (
    state_id INT AUTO_INCREMENT PRIMARY KEY,
    state_name VARCHAR(50) UNIQUE NOT NULL,
    subsidy_percent FLOAT
);

CREATE TABLE Projects (
    project_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    system_size FLOAT,
    cost_per_kw FLOAT,
    state_id INT,
    tariff FLOAT,
    net_metering FLOAT,
    type VARCHAR(50),
    FOREIGN KEY (state_id) REFERENCES StateSubsidies(state_id)
);

CREATE TABLE PowerPurchaseAgreements (
    agreement_id INT AUTO_INCREMENT PRIMARY KEY,
    state_id INT,
    tariff FLOAT,
    escalation_rate FLOAT,
    contract_duration INT,
    FOREIGN KEY (state_id) REFERENCES StateSubsidies(state_id)
);

CREATE TABLE SolarPanelData (
    data_id INT AUTO_INCREMENT PRIMARY KEY,
    state_id INT,
    panel_lifespan INT,
    degradation_rate FLOAT,
    maintenance_cost FLOAT,
    FOREIGN KEY (state_id) REFERENCES StateSubsidies(state_id)
);

CREATE TABLE SolarPanelModels (
    panel_id INT AUTO_INCREMENT PRIMARY KEY,
    brand VARCHAR(100),
    model_name VARCHAR(100),
    cost_per_panel FLOAT,
    length FLOAT,  -- in meters
    width FLOAT,   -- in meters
    power_output FLOAT -- in kW per panel
);

CREATE TABLE SolarIntensity (
    district_id INT PRIMARY KEY AUTO_INCREMENT,
    state_name VARCHAR(100),
    district_name VARCHAR(100),
    latitude DECIMAL(8,6),
    longitude DECIMAL(9,6),
    avg_annual_solar_radiation DECIMAL(5,2),  -- kWh/m²/day
    avg_sunlight_hours_per_day DECIMAL(4,2)
);

CREATE TABLE UserSelectedPanel (
    id INT PRIMARY KEY AUTO_INCREMENT,
    panel_efficiency DECIMAL(5,2),
    panel_area DECIMAL(5,2),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO StateSubsidies (state_id, state_name, subsidy_percent) VALUES
(1, 'Andhra Pradesh', 30.0),
(2, 'Arunachal Pradesh', 30.0),
(3, 'Assam', 25.0),
(4, 'Bihar', 20.0),
(5, 'Chhattisgarh', 22.0),
(6, 'Goa', 25.0),
(7, 'Gujarat', 35.0),
(8, 'Haryana', 28.0),
(9, 'Himachal Pradesh', 27.0),
(10, 'Jharkhand', 18.0),
(11, 'Karnataka', 32.0),
(12, 'Kerala', 29.0),
(13, 'Madhya Pradesh', 25.0),
(14, 'Maharashtra', 30.0),
(15, 'Manipur', 28.0),
(16, 'Meghalaya', 27.0),
(17, 'Mizoram', 26.0),
(18, 'Nagaland', 28.0),
(19, 'Odisha', 23.0),
(20, 'Punjab', 28.0),
(21, 'Rajasthan', 35.0),
(22, 'Sikkim', 29.0),
(23, 'Tamil Nadu', 31.0),
(24, 'Telangana', 29.0),
(25, 'Tripura', 24.0),
(26, 'Uttar Pradesh', 22.0),
(27, 'Uttarakhand', 26.0),
(28, 'West Bengal', 24.0),
(29, 'Andaman and Nicobar Islands', 20.0),
(30, 'Chandigarh', 22.0),
(31, 'Dadra and Nagar Haveli and Daman and Diu', 20.0),
(32, 'Lakshadweep', 35.0),
(33, 'Delhi', 18.0),
(34, 'Puducherry', 23.0),
(35, 'Ladakh', 30.0),
(36, 'Jammu and Kashmir', 32.0);

INSERT INTO Projects (name, system_size, cost_per_kw, state_id, tariff, net_metering, type) VALUES
('Andhra Pradesh Solar Project', 5, 40000, 1, 7.5, 3.5, 'Solar'),
('Arunachal Pradesh Solar Project', 4.5, 41000, 2, 7.4, 3.4, 'Solar'),
('Assam Solar Project', 4, 42000, 3, 7.2, 3.2, 'Solar'),
('Bihar Solar Project', 6, 41000, 4, 7.8, 3.4, 'Solar'),
('Chhattisgarh Solar Project', 5, 40500, 5, 7.6, 3.3, 'Solar'),
('Goa Solar Project', 4.8, 41500, 6, 7.3, 3.35, 'Solar'),
('Gujarat Solar Project', 5, 39000, 7, 7.3, 3.6, 'Solar'),
('Haryana Solar Project', 5.5, 39500, 8, 7.4, 3.7, 'Solar'),
('Himachal Pradesh Solar Project', 5, 40000, 9, 7.5, 3.5, 'Solar'),
('Jharkhand Solar Project', 5.5, 40500, 10, 7.6, 3.6, 'Solar'),
('Karnataka Solar Project', 5.5, 39500, 11, 7.4, 3.7, 'Solar'),
('Kerala Solar Project', 5, 40000, 12, 7.5, 3.5, 'Solar'),
('Madhya Pradesh Solar Project', 5.5, 40500, 13, 7.6, 3.6, 'Solar'),
('Maharashtra Solar Project', 5, 40000, 14, 7.5, 3.5, 'Solar'),
('Manipur Solar Project', 4.6, 41200, 15, 7.3, 3.45, 'Solar'),
('Meghalaya Solar Project', 4.7, 41300, 16, 7.3, 3.48, 'Solar'),
('Mizoram Solar Project', 4.8, 41400, 17, 7.38, 3.5, 'Solar'),
('Nagaland Solar Project', 4.6, 41200, 18, 7.3, 3.45, 'Solar'),
('Odisha Solar Project', 5.5, 40500, 19, 7.6, 3.6, 'Solar'),
('Punjab Solar Project', 5.5, 39500, 20, 7.4, 3.7, 'Solar'),
('Rajasthan Solar Project', 5, 39000, 21, 7.3, 3.6, 'Solar'),
('Sikkim Solar Project', 4.9, 41500, 22, 7.4, 3.5, 'Solar'),
('Tamil Nadu Solar Project', 5.5, 39800, 23, 7.4, 3.55, 'Solar'),
('Telangana Solar Project', 5.2, 39900, 24, 7.45, 3.65, 'Solar'),
('Tripura Solar Project', 4.7, 41400, 25, 7.38, 3.5, 'Solar'),
('Uttar Pradesh Solar Project', 5.3, 40200, 26, 7.55, 3.45, 'Solar'),
('Uttarakhand Solar Project', 5.1, 40100, 27, 7.52, 3.48, 'Solar'),
('West Bengal Solar Project', 5.4, 40300, 28, 7.58, 3.42, 'Solar'),
('Andaman and Nicobar Islands Solar Project', 4.5, 42000, 29, 7.2, 3.2, 'Solar'),
('Chandigarh Solar Project', 4.8, 41800, 30, 7.35, 3.45, 'Solar'),
('Dadra and Nagar Haveli and Daman and Diu Solar Project', 4.7, 41700, 31, 7.32, 3.4, 'Solar'),
('Lakshadweep Solar Project', 4.9, 41600, 32, 7.36, 3.48, 'Solar'),
('Delhi Solar Project', 4.8, 41900, 33, 7.37, 3.46, 'Solar'),
('Puducherry Solar Project', 4.9, 41800, 34, 7.35, 3.45, 'Solar'),
('Ladakh Solar Project', 4.6, 42100, 35, 7.22, 3.3, 'Solar'),
('Jammu and Kashmir Solar Project', 4.7, 41900, 36, 7.3, 3.4, 'Solar');

INSERT INTO PowerPurchaseAgreements (state_id, tariff, escalation_rate, contract_duration) VALUES
(1, 8.0, 3.0, 20),
(2, 7.5, 2.8, 20),
(3, 7.8, 2.9, 20),
(4, 8.1, 3.1, 20),
(5, 7.9, 3.0, 20),
(6, 7.8, 3.0, 20),
(7, 7.9, 3.0, 20),
(8, 8.0, 3.2, 20),
(9, 7.7, 2.8, 20),
(10, 8.2, 3.3, 20),
(11, 8.2, 3.2, 20),
(12, 7.9, 3.1, 20),
(13, 8.0, 3.0, 20),
(14, 8.0, 3.0, 20),
(15, 8.1, 3.1, 20),
(16, 8.0, 3.0, 20),
(17, 8.2, 3.2, 20),
(18, 8.1, 3.1, 20),
(19, 8.1, 3.1, 20),
(20, 7.8, 2.9, 20),
(21, 7.7, 2.8, 20),
(22, 7.9, 3.0, 20),
(23, 8.1, 3.2, 20),
(24, 8.0, 3.0, 20),
(25, 8.0, 3.0, 20),
(26, 8.2, 3.3, 20),
(27, 8.0, 3.1, 20),
(28, 7.9, 3.0, 20),
(29, 7.8, 2.9, 20),
(30, 7.9, 3.0, 20),
(31, 7.8, 2.9, 20),
(32, 8.0, 3.0, 20),
(33, 8.1, 3.1, 20),
(34, 8.0, 3.0, 20),
(35, 7.9, 3.0, 20),
(36, 8.2, 3.2, 20);

INSERT INTO SolarPanelData (state_id, panel_lifespan, degradation_rate, maintenance_cost) VALUES
(1, 25, 0.5, 2000),
(2, 25, 0.6, 2200),
(3, 25, 0.6, 2200),
(4, 25, 0.5, 2100),
(5, 25, 0.55, 2050),
(6, 25, 0.45, 1950),
(7, 25, 0.4, 1900),
(8, 25, 0.45, 1950),
(9, 25, 0.5, 2000),
(10, 25, 0.55, 2150),
(11, 25, 0.45, 2050),
(12, 25, 0.5, 2000),
(13, 25, 0.5, 2100),
(14, 25, 0.45, 1950),
(15, 25, 0.6, 2200),
(16, 25, 0.55, 2150),
(17, 25, 0.6, 2200),
(18, 25, 0.6, 2200),
(19, 25, 0.55, 2150),
(20, 25, 0.45, 1950),
(21, 25, 0.4, 1900),
(22, 25, 0.55, 2150),
(23, 25, 0.5, 2000),
(24, 25, 0.6, 2200),
(25, 25, 0.6, 2200),
(26, 25, 0.55, 2100),
(27, 25, 0.5, 2050),
(28, 25, 0.45, 2000),
(29, 25, 0.55, 2150),
(30, 25, 0.5, 2000),
(31, 25, 0.52, 2075),
(32, 25, 0.53, 2100),
(33, 25, 0.5, 2000),
(34, 25, 0.51, 2050),
(35, 25, 0.4, 1900),
(36, 25, 0.42, 1925);

INSERT INTO SolarPanelModels (brand, model_name, cost_per_panel, length, width, power_output) VALUES
('Tata Power Solar', 'TP300P', 15000, 1.96, 0.99, 0.3),
('Luminous', 'MonoPerc 380W', 17500, 2.1, 1.05, 0.38),
('Adani Solar', 'AS-400M', 19000, 2.0, 1.0, 0.4),
('Vikram Solar', 'Somera 320W', 16000, 1.98, 1.0, 0.32),
('Waaree', 'WS-375', 18500, 2.05, 1.02, 0.375),
('RenewSys', 'Deserv 345W', 17000, 2.1, 1.03, 0.345),
('Goldi Solar', 'GSP 390W', 18000, 2.2, 1.1, 0.39),
('Jakson', 'Elite 400W', 20000, 2.15, 1.05, 0.4),
('Emmvee', 'Dura 325W', 16500, 1.95, 1.0, 0.325),
('Microtek', 'MTK330W', 15500, 1.94, 0.98, 0.33);






