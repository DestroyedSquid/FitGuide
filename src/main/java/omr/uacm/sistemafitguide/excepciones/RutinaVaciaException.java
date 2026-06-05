/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package omr.uacm.sistemafitguide.excepciones;

/**
 *
 * @author migue
 */
public class RutinaVaciaException extends Exception {
    public RutinaVaciaException(String nivel) {
        super("Sin ejercicios: No se encontraron ejercicios disponibles para el nivel " + nivel + ".");
    }
}