/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package omr.uacm.sistemafitguide.excepciones;

/**
 *
 * @author migue
 */
public class MultimediaNoEncontradaException extends Exception {
    public MultimediaNoEncontradaException(String rutaArchivo) {
        super("Recurso faltante: No se pudo cargar la imagen o GIF en la ruta: " + rutaArchivo);
    }
}
