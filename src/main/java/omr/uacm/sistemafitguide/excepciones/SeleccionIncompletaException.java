/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package omr.uacm.sistemafitguide.excepciones;

/**
 *
 * @author migue
 */
public class SeleccionIncompletaException extends Exception {
    public SeleccionIncompletaException(String pasoFaltante) {
        super("Falta información: Debes completar la selección de " + pasoFaltante + " para continuar.");
    }
}
