package omr.uacm.sistemafitguide;

import java.time.LocalDate;
import java.time.Period;

public class BiometriaUtils {

    // 1. Calcula la edad exacta basándose en la fecha de nacimiento
    public static int calcularEdad(String fechaNacimiento) {
        try {
            LocalDate nacimiento = LocalDate.parse(fechaNacimiento);
            return Period.between(nacimiento, LocalDate.now()).getYears();
        } catch (Exception e) {
            return 0;
        }
    }

    // 2. Calcula el IMC (Fórmula estándar: peso / altura^2 en metros)
    public static double calcularIMC(double pesoKg, double alturaM) {
        if (alturaM <= 0) return 0;
        return pesoKg / (alturaM * alturaM);
    }

    // 3. Fórmula de Deurenberg para Grasa Corporal (SOLO ADULTOS)
    public static double calcularPorcentajeGrasa(double imc, int edad, String genero) {
        // RESTRICCIÓN MÉDICA: La OMS no recomienda esta fórmula en menores de 18 años
        if (edad < 18) {
            return -1.0;
        }

        int valorGenero = 0; // 0 para mujeres, 1 para hombres
        if (genero.equalsIgnoreCase("Hombre") || genero.equalsIgnoreCase("Masculino")) {
            valorGenero = 1;
        } else if (genero.equalsIgnoreCase("Mujer") || genero.equalsIgnoreCase("Femenino")) {
            valorGenero = 0;
        } else {
            return -1.0; // Si no hay género especificado, no damos datos falsos
        }

        // Fórmula: (1.20 x IMC) + (0.23 x Edad) - (10.8 x Sexo) - 5.4
        return (1.20 * imc) + (0.23 * edad) - (10.8 * valorGenero) - 5.4;
    }

    public static String obtenerCategoriaIMC(double imc) {
        if (imc < 18.5) return "Bajo peso";
        if (imc >= 18.5 && imc < 24.9) return "Peso normal";
        if (imc >= 25.0 && imc < 29.9) return "Sobrepeso";
        return "Obesidad";
    }
}