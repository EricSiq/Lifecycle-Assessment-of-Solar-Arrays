//DatabaseHelper.java
import java.sql.*;

public class DatabaseHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/PIJ_Project_Database";
    private static final String USER = "root";
    private static final String PASSWORD = "XlR8#2137";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found! Add the connector JAR.", e);
        }
    }
