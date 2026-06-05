/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package omr.uacm.sistemafitguide.excepciones;

/**
 *
 * @author migue
 */
public class DescansoFueraDeRangoException extends Exception {
    public DescansoFueraDeRangoException(int segundos) {
        super("Tiempo ilógico: " + segundos + " segundos no es un descanso saludable. Debe ser entre 10 y 300 segundos.");
    }
}
