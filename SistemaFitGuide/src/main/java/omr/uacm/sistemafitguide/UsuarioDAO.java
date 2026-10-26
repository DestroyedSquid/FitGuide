/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package omr.uacm.sistemafitguide;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author migue
 */
public class UsuarioDAO {

    /**
     * Genera el hash SHA-256 de una contraseña en texto plano.
     * Las contraseñas nunca se almacenan ni comparan en texto claro.
     */
    private String hashContrasena(String contrasena) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(contrasena.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hashBytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 no disponible en esta JVM", e);
        }
    }

    /**
     * Registra un nuevo usuario y devuelve el ID generado por la BD.
     * @return el id_usuario generado, o -1 si el registro falló (ej. nombre duplicado).
     */
    public int registrarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre_usuario, contrasena, peso, altura, fecha_nacimiento, genero) VALUES (?, ?, ?, ?, ?, ?);";
        
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, usuario.getNombreUsuario());
            pstmt.setString(2, hashContrasena(usuario.getContrasena())); // hash antes de guardar
            pstmt.setDouble(3, usuario.getPeso());
            pstmt.setDouble(4, usuario.getAltura());
            pstmt.setString(5, usuario.getFechaNacimiento());
            pstmt.setString(6, usuario.getGenero());
            
            pstmt.executeUpdate();
            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1); // devolvemos el ID generado directamente
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al intentar registrar usuario: " + e.getMessage());
        }
        return -1; // fallo: usuario ya existe u otro error
    }

    public int RegistrarUsuario(Usuario usuario) {
        return registrarUsuario(usuario);
    }

    public int verificarLogin(String nombreUsuario, String contrasena) {
        String sql = "SELECT id_usuario FROM usuarios WHERE nombre_usuario = ? AND contrasena = ?;";
        
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nombreUsuario);
            pstmt.setString(2, hashContrasena(contrasena)); // comparar con hash
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_usuario"); //retorno el id del usuario
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar inicio de sesión: " + e.getMessage());
        }
        return -1; // credenciales invalidas
    }

    public int VerificarLogin(String nombreUsuario, String contrasena) {
        return verificarLogin(nombreUsuario, contrasena);
    }
}
