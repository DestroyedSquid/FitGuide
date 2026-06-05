package omr.uacm.sistemafitguide.modelos;

public class HistorialRutina {
    private int idHistorial;
    private int idUsuario;
    private String fecha;
    private String grupoMuscular;
    private String nivel;

    public HistorialRutina(int idUsuario, String fecha, String grupoMuscular, String nivel) {
        this.idUsuario = idUsuario; this.fecha = fecha;
        this.grupoMuscular = grupoMuscular; this.nivel = nivel;
    }
    public int getIdHistorial() { return idHistorial; }
    public void setIdHistorial(int idHistorial) { this.idHistorial = idHistorial; }
    public int getIdUsuario() { return idUsuario; }
    public String getFecha() { return fecha; }
    public String getGrupoMuscular() { return grupoMuscular; }
    public String getNivel() { return nivel; }
}