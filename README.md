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
