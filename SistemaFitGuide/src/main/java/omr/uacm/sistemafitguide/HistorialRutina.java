/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package omr.uacm.sistemafitguide;

/**
 *
 * @author migue
 */
public class HistorialRutina {
    private int idHistorial;
    private int idUsuario;
    private String fecha;
    private int duracionSegundos;

    public HistorialRutina() {
    }

    public HistorialRutina(int idUsuario, String fecha, int duracionSegundos) {
        this.idUsuario = idUsuario;
        this.fecha = fecha;
        this.duracionSegundos = duracionSegundos;
    }


    public int getIdHistorial() { return idHistorial; }
    public void setIdHistorial(int idHistorial) { this.idHistorial = idHistorial; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public int getDuracionSegundos() { return duracionSegundos; }
    public void setDuracionSegundos(int duracionSegundos) { this.duracionSegundos = duracionSegundos; }
}