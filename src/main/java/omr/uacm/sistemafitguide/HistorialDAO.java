package omr.uacm.sistemafitguide;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import omr.uacm.sistemafitguide.modelos.HistorialEjercicio;
import omr.uacm.sistemafitguide.modelos.HistorialRutina;

public class HistorialDAO {
    public boolean guardarRutinaCompleta(HistorialRutina rutina, List<HistorialEjercicio> ejercicios) {
        String sqlRutina = "INSERT INTO historial_rutinas(id_usuario, fecha, grupo_muscular, nivel) VALUES(?,?,?,?)";
        String sqlEjercicio = "INSERT INTO historial_ejercicios(id_historial, nombre_ejercicio, reps_objetivo, reps_reales) VALUES(?,?,?,?)";

        Connection conn = ConexionSQLite.conectar();
        if (conn == null) return false;

        try {
            conn.setAutoCommit(false);
            int idHistorialGenerado = -1;

            try (PreparedStatement pstmtRutina = conn.prepareStatement(sqlRutina, Statement.RETURN_GENERATED_KEYS)) {
                pstmtRutina.setInt(1, rutina.getIdUsuario());
                pstmtRutina.setString(2, rutina.getFecha());
                pstmtRutina.setString(3, rutina.getGrupoMuscular());
                pstmtRutina.setString(4, rutina.getNivel());
                pstmtRutina.executeUpdate();

                try (ResultSet rsKeys = pstmtRutina.getGeneratedKeys()) {
                    if (rsKeys.next()) idHistorialGenerado = rsKeys.getInt(1);
                }
            }

            if (idHistorialGenerado == -1) {
                conn.rollback();
                return false;
            }

            try (PreparedStatement pstmtEjercicio = conn.prepareStatement(sqlEjercicio)) {
                for (HistorialEjercicio ej : ejercicios) {
                    pstmtEjercicio.setInt(1, idHistorialGenerado);
                    pstmtEjercicio.setString(2, ej.getNombreEjercicio());
                    pstmtEjercicio.setInt(3, ej.getRepsObjetivo());
                    pstmtEjercicio.setInt(4, ej.getRepsReales());
                    pstmtEjercicio.addBatch();
                }
                pstmtEjercicio.executeBatch();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) {}
            return false;
        } finally {
            try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) {}
        }
    }
}