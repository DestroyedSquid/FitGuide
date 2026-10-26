package omr.uacm.sistemafitguide;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD sobre la tabla ejercicios_catalogo.
 * Soporta AMBOS estilos de programación: PascalCase y camelCase (Java Standard).
 *
 * @author Coach Module — FitGuide
 */
public class EjercicioCatalogoDao {

    // ═════════════════════════════════════════════════════════════════════════
    //  INICIALIZACIÓN DE TABLA (Ambos Estilos)
    // ═════════════════════════════════════════════════════════════════════════

    /** PascalCase */
    public static void InicializarTabla() {
        String sql = "CREATE TABLE IF NOT EXISTS ejercicios_catalogo ("
                + "id_ejercicio INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nombre TEXT NOT NULL, "
                + "zona_corporal TEXT NOT NULL, "
                + "nivel TEXT NOT NULL, "
                + "tipo_peso TEXT NOT NULL, "
                + "repeticiones INTEGER NOT NULL DEFAULT 10, "
                + "series INTEGER NOT NULL DEFAULT 3, "
                + "duracion INTEGER NOT NULL DEFAULT 0, "
                + "descanso INTEGER NOT NULL DEFAULT 30, "
                + "descripcion TEXT, "
                + "url_imagen TEXT, "
                + "url_video TEXT"
                + ");";

        try (Connection conn = ConexionDB.conectar();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabla ejercicios_catalogo verificada.");
        } catch (SQLException e) {
            System.err.println("Error al crear tabla ejercicios_catalogo: " + e.getMessage());
        }
    }

    /** camelCase (alias) */
    public static void inicializarTabla() {
        InicializarTabla();
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  CONSULTAS (Ambos Estilos)
    // ═════════════════════════════════════════════════════════════════════════

    /** PascalCase */
    public List<EjercicioCoach> ObtenerTodos() {
        String sql = "SELECT * FROM ejercicios_catalogo ORDER BY zona_corporal, nivel, nombre;";
        List<EjercicioCoach> lista = new ArrayList<>();

        try (Connection conn = ConexionDB.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(MapearDesdeResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ejercicios: " + e.getMessage());
        }
        return lista;
    }

    /** camelCase (alias) */
    public List<EjercicioCoach> obtenerTodos() {
        return ObtenerTodos();
    }

    /** PascalCase */
    public List<EjercicioCoach> ObtenerFiltrados(String zonaCorporal, String nivel, String tipoPeso) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ejercicios_catalogo WHERE 1=1");
        List<Object> parametros = new ArrayList<>();

        if (zonaCorporal != null && !zonaCorporal.isEmpty() && !zonaCorporal.equals("Todos")) {
            sql.append(" AND zona_corporal = ?");
            parametros.add(zonaCorporal);
        }
        if (nivel != null && !nivel.isEmpty() && !nivel.equals("Todos")) {
            sql.append(" AND nivel = ?");
            parametros.add(nivel);
        }
        if (tipoPeso != null && !tipoPeso.isEmpty() && !tipoPeso.equals("Todos")) {
            sql.append(" AND tipo_peso = ?");
            parametros.add(tipoPeso);
        }
        sql.append(" ORDER BY zona_corporal, nivel, nombre;");

        List<EjercicioCoach> lista = new ArrayList<>();

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(MapearDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al filtrar ejercicios: " + e.getMessage());
        }
        return lista;
    }

    /** camelCase (alias) */
    public List<EjercicioCoach> obtenerFiltrados(String zonaCorporal, String nivel, String tipoPeso) {
        return ObtenerFiltrados(zonaCorporal, nivel, tipoPeso);
    }

    /** PascalCase */
    public List<EjercicioCoach> BuscarPorNombre(String textoBusqueda) {
        String sql = "SELECT * FROM ejercicios_catalogo WHERE LOWER(nombre) LIKE ? ORDER BY nombre;";
        List<EjercicioCoach> lista = new ArrayList<>();

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + textoBusqueda.toLowerCase() + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(MapearDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar ejercicio: " + e.getMessage());
        }
        return lista;
    }

    /** camelCase (alias) */
    public List<EjercicioCoach> buscarPorNombre(String textoBusqueda) {
        return BuscarPorNombre(textoBusqueda);
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  CRUD (Ambos Estilos)
    // ═════════════════════════════════════════════════════════════════════════

    /** PascalCase */
    public boolean Agregar(EjercicioCoach ejercicio) {
        String sql = "INSERT INTO ejercicios_catalogo "
                + "(nombre, zona_corporal, nivel, tipo_peso, repeticiones, series, "
                + "duracion, descanso, descripcion, url_imagen, url_video) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            EstablecerParametros(ps, ejercicio);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    ejercicio.setIdEjercicio(keys.getInt(1));
                }
            }
            System.out.println("Ejercicio agregado: " + ejercicio.getNombre());
            return true;

        } catch (SQLException e) {
            System.err.println("Error al agregar ejercicio: " + e.getMessage());
            return false;
        }
    }

    /** camelCase (alias) */
    public boolean agregar(EjercicioCoach ejercicio) {
        return Agregar(ejercicio);
    }

    /** PascalCase */
    public boolean Actualizar(EjercicioCoach ejercicio) {
        String sql = "UPDATE ejercicios_catalogo SET "
                + "nombre = ?, zona_corporal = ?, nivel = ?, tipo_peso = ?, "
                + "repeticiones = ?, series = ?, duracion = ?, descanso = ?, "
                + "descripcion = ?, url_imagen = ?, url_video = ? "
                + "WHERE id_ejercicio = ?;";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            EstablecerParametros(ps, ejercicio);
            ps.setInt(12, ejercicio.getIdEjercicio());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Ejercicio actualizado: " + ejercicio.getNombre());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar ejercicio: " + e.getMessage());
        }
        return false;
    }

    /** camelCase (alias) */
    public boolean actualizar(EjercicioCoach ejercicio) {
        return Actualizar(ejercicio);
    }

    /** PascalCase */
    public boolean Eliminar(int idEjercicio) {
        String sql = "DELETE FROM ejercicios_catalogo WHERE id_ejercicio = ?;";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEjercicio);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Ejercicio eliminado con ID: " + idEjercicio);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar ejercicio: " + e.getMessage());
        }
        return false;
    }

    /** camelCase (alias) */
    public boolean eliminar(int idEjercicio) {
        return Eliminar(idEjercicio);
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  UTILIDADES PRIVADAS
    // ═════════════════════════════════════════════════════════════════════════

    private EjercicioCoach MapearDesdeResultSet(ResultSet rs) throws SQLException {
        return new EjercicioCoach(
            rs.getInt("id_ejercicio"),
            rs.getString("nombre"),
            rs.getString("zona_corporal"),
            rs.getString("nivel"),
            rs.getString("tipo_peso"),
            rs.getInt("repeticiones"),
            rs.getInt("series"),
            rs.getInt("duracion"),
            rs.getInt("descanso"),
            rs.getString("descripcion"),
            rs.getString("url_imagen"),
            rs.getString("url_video")
        );
    }

    private void EstablecerParametros(PreparedStatement ps, EjercicioCoach ej) throws SQLException {
        ps.setString(1, ej.getNombre());
        ps.setString(2, ej.getZonaCorporal());
        ps.setString(3, ej.getNivel());
        ps.setString(4, ej.getTipoPeso());
        ps.setInt(5, ej.getRepeticiones());
        ps.setInt(6, ej.getSeries());
        ps.setInt(7, ej.getDuracion());
        ps.setInt(8, ej.getDescanso());
        ps.setString(9, ej.getDescripcion());
        ps.setString(10, ej.getUrlImagen());
        ps.setString(11, ej.getUrlVideo());
    }
}
