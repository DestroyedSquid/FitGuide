/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package omr.uacm.sistemafitguide;

/**
 *
 * @author migue
 */
public class HistorialEjercicio {
    private int idEjercicio;
    private int idHistorial; //llave foranea para historial rutina
    private String nombreEjercicio;
    private int repsObjetivo;
    private int repsReales;

    public HistorialEjercicio() {
    }

    public HistorialEjercicio(int idHistorial, String nombreEjercicio, int repsObjetivo, int repsReales) {
        this.idHistorial = idHistorial;
        this.nombreEjercicio = nombreEjercicio;
        this.repsObjetivo = repsObjetivo;
        this.repsReales = repsReales;
    }

    public int getIdEjercicio() { return idEjercicio; }
    public void setIdEjercicio(int idEjercicio) { this.idEjercicio = idEjercicio; }

    public int getIdHistorial() { return idHistorial; }
    public void setIdHistorial(int idHistorial) { this.idHistorial = idHistorial; }

    public String getNombreEjercicio() { return nombreEjercicio; }
    public void setNombreEjercicio(String nombreEjercicio) { this.nombreEjercicio = nombreEjercicio; }

    public int getRepsObjetivo() { return repsObjetivo; }
    public void setRepsObjetivo(int repsObjetivo) { this.repsObjetivo = repsObjetivo; }

    public int getRepsReales() { return repsReales; }
    public void setRepsReales(int repsReales) { this.repsReales = repsReales; }
}