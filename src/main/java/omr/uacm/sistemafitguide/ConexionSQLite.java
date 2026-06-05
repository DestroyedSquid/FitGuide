package omr.uacm.sistemafitguide;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionSQLite {
    private static final String URL = "jdbc:sqlite:fitguide.db";

    public static Connection conectar() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URL);
            conexion.createStatement().execute("PRAGMA foreign_keys = ON");
        } catch (SQLException e) {
            System.err.println("[FALLA CRÍTICA] Imposible conectar a SQLite: " + e.getMessage());
        }
        return conexion;
    }
}