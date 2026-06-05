package omr.uacm.sistemafitguide.modelos;

public class HistorialEjercicio {
    private int idDetalle;
    private int idHistorial;
    private String nombreEjercicio;
    private int repsObjetivo;
    private int repsReales;

    public HistorialEjercicio(String nombreEjercicio, int repsObjetivo, int repsReales) {
        this.nombreEjercicio = nombreEjercicio;
        this.repsObjetivo = repsObjetivo; this.repsReales = repsReales;
    }
    public int getIdDetalle() { return idDetalle; }
    public void setIdDetalle(int idDetalle) { this.idDetalle = idDetalle; }
    public int getIdHistorial() { return idHistorial; }
    public void setIdHistorial(int idHistorial) { this.idHistorial = idHistorial; }
    public String getNombreEjercicio() { return nombreEjercicio; }
    public int getRepsObjetivo() { return repsObjetivo; }
    public int getRepsReales() { return repsReales; }
}