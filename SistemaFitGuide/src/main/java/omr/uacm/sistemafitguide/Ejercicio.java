/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package omr.uacm.sistemafitguide;

/**
 * JavaFX App
 */

public class Ejercicio {
    private String nombre;
    private String rutaVideo; 
    private String rutaImagen; 
    private String instrucciones; // corregido: era 'intrucciones'
    private int repeticionesObjetivo;

   
    public Ejercicio(String nombre, String rutaVideo, String rutaImagen, String instrucciones, int repeticionesObjetivo){
        this.nombre = nombre;
        this.rutaVideo = rutaVideo;
        this.rutaImagen = rutaImagen;
        this.instrucciones = instrucciones;
        this.repeticionesObjetivo = repeticionesObjetivo;
    }

    public String getNombre() { return nombre; }

    /** @deprecated Usar {@link #getRutaVideo()} */
    public String getRutavideo() { return rutaVideo; }
    public String getRutaVideo() { return rutaVideo; }

   
    public String getRutaImagen() { return rutaImagen; }

    /** @deprecated Usar {@link #getInstrucciones()} */
    public String getIntrucciones() { return instrucciones; }
    public String getInstrucciones() { return instrucciones; }

    public int getRepeticionesObjetivo() { return repeticionesObjetivo; }

    public void setRepeticionesObjetivo(int repeticionesObjetivo) {
        this.repeticionesObjetivo = repeticionesObjetivo;
    }
    
}
        

