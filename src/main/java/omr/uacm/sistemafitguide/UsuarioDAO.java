package omr.uacm.sistemafitguide;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import omr.uacm.sistemafitguide.modelos.Usuario;

public class UsuarioDAO {

    public void inicializarBaseDeDatos() {
        String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios ("
                + " id_usuario INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " nombre_usuario TEXT NOT NULL UNIQUE,"
                + " contrasena TEXT NOT NULL,"
                + " peso REAL NOT NULL,"
                + " altura REAL NOT NULL,"
                + " fecha_nacimiento TEXT NOT NULL,"
                + " genero TEXT NOT NULL"
                + ");";

        String sqlHistorialRutinas = "CREATE TABLE IF NOT EXISTS historial_rutinas ("
                + " id_historial INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " id_usuario INTEGER NOT NULL,"
                + " fecha TEXT NOT NULL,"
                + " grupo_muscular TEXT NOT NULL,"
                + " nivel TEXT NOT NULL,"
                + " FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)"
                + ");";

        String sqlHistorialEjercicios = "CREATE TABLE IF NOT EXISTS historial_ejercicios ("
                + " id_detalle INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " id_historial INTEGER NOT NULL,"
                + " nombre_ejercicio TEXT NOT NULL,"
                + " reps_objetivo INTEGER NOT NULL,"
                + " reps_reales INTEGER NOT NULL,"
                + " FOREIGN KEY (id_historial) REFERENCES historial_rutinas(id_historial)"
                + ");";

        Connection conn = null;
        try {
            conn = ConexionSQLite.conectar();
            // ¡EL BLINDAJE! Solo creamos las tablas si la conexión no es nula
            if (conn != null) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("PRAGMA journal_mode = WAL");
                    stmt.execute(sqlUsuarios);
                    stmt.execute(sqlHistorialRutinas);
                    stmt.execute(sqlHistorialEjercicios);
                    System.out.println("[SISTEMA] Integridad de BD verificada. Tablas listas.");
                }
            } else {
                System.err.println("[ADVERTENCIA] SQLite devolvió una conexión nula. ¿Permisos?");
            }
        } catch (SQLException e) {
            System.err.println("[FALLA CRÍTICA] Integridad de BD comprometida: " + e.getMessage());
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) {}
            }
        }
    }

    public int validarLogin(String nombreUsuario, String contrasena) {
        String sql = "SELECT id_usuario FROM usuarios WHERE nombre_usuario = ? AND contrasena = ?";
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombreUsuario);
            pstmt.setString(2, contrasena);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt("id_usuario");
            }
        } catch (SQLException e) {
            System.err.println("[ERROR SQL] Login fallido: " + e.getMessage());
        }
        return -1;
    }

    public boolean registrarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios(nombre_usuario, contrasena, peso, altura, fecha_nacimiento, genero) VALUES(?,?,?,?,?,?)";
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario.getNombreUsuario());
            pstmt.setString(2, usuario.getContrasena());
            pstmt.setDouble(3, usuario.getPeso());
            pstmt.setDouble(4, usuario.getAltura());
            pstmt.setString(5, usuario.getFechaNacimiento());
            pstmt.setString(6, usuario.getGenero());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ERROR SQL] Error de inserción: " + e.getMessage());
            return false;
        }
    }
    // Método para jalar todos los datos del perfil
    public Usuario obtenerUsuarioPorId(int idUsuario) {
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";
        try (java.sql.Connection conn = ConexionSQLite.conectar();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setIdUsuario(rs.getInt("id_usuario"));
                    u.setNombreUsuario(rs.getString("nombre_usuario"));
                    u.setPeso(rs.getDouble("peso"));
                    u.setAltura(rs.getDouble("altura"));
                    u.setFechaNacimiento(rs.getString("fecha_nacimiento"));
                    u.setGenero(rs.getString("genero"));
                    return u;
                }
            }
        } catch (java.sql.SQLException e) {
            System.err.println("[ERROR SQL] Fallo al obtener usuario: " + e.getMessage());
        }
        return null;
    }

    // Método para guardar el nuevo peso si el usuario lo edita
    public boolean actualizarPeso(int idUsuario, double nuevoPeso) {
        String sql = "UPDATE usuarios SET peso = ? WHERE id_usuario = ?";
        try (java.sql.Connection conn = ConexionSQLite.conectar();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, nuevoPeso);
            pstmt.setDouble(2, idUsuario);
            return pstmt.executeUpdate() > 0;

        } catch (java.sql.SQLException e) {
            System.err.println("[ERROR SQL] Fallo al actualizar peso: " + e.getMessage());
            return false;
        }
    }
}