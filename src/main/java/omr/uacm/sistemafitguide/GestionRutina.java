/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

package omr.uacm.sistemafitguide;
import omr.uacm.sistemafitguide.controlador.PantallaConfiguracionRutinaController;

import java.util.List;
import java.util.ArrayList;

/**
 * JavaFX App - Gestor Secuencial de Rutinas
 */
public class GestionRutina {
    private static GestionRutina instancia;
    private List<Ejercicio> listaCargada = new ArrayList<>();
    
    // IMPORTANTÍSIMO: El índice en Java empieza en 0 para acceder a las Listas
    private int ejercicioActual = 0; 
    private int totalRepeticionesReales = 0;
    private String musculoSeleccionado = "";
    private String nivelSeleccionado = "";
    private boolean usaMancuernas = false;
    private String llaveRutina = ""; 
    private double pesoExtraKg = 0.0;
    private int seriesTotales = 2; // por defecto 2 series.
    private int serieActual = 1;   // Solo para mostrar visualmente "Serie 1 de X"
    private int idUsuarioLogueado;
    private GestionRutina() {}

    public static GestionRutina getInstance() {
        if (instancia == null) { instancia = new GestionRutina(); }
        return instancia;
    }

    public void configurarRutina() {
        // 1. Obtenemos los ejercicios base (solo los 3 o 4 originales)
        List<Ejercicio> ejerciciosBase = CatalogoEjercicios.obtenerRutina(musculoSeleccionado, nivelSeleccionado, usaMancuernas);
        
        // Limpiamos todo para iniciar una rutina desde cero
        this.listaCargada.clear();
        this.ejercicioActual = 0; 
        this.serieActual = 1;
        this.totalRepeticionesReales = 0;
        
        // 2. Obtenemos la configuración que guardaste en el mapa
        Rutina configUsuario = PantallaConfiguracionRutinaController.mapaRutinas.get(llaveRutina);
        
        if (configUsuario != null) {
            this.pesoExtraKg = configUsuario.getPesoExtraKg();
            
            // 3. Revisamos las series
            if (configUsuario.getSeries() > 0) {
                this.seriesTotales = configUsuario.getSeries();
            }
            
            // 4. Aplicamos las repeticiones personalizadas a los ejercicios base primero
            if (ejerciciosBase != null && configUsuario.getMapaRepeticiones() != null) {
                for (Ejercicio ej : ejerciciosBase) {
                    if (configUsuario.getMapaRepeticiones().containsKey(ej.getNombre())) {
                        ej.setRepeticionesObjetivo(configUsuario.getMapaRepeticiones().get(ej.getNombre()));
                    }
                }
            }
        } else {
            this.seriesTotales = 2; // Si no hay config, fallback a 2 series.
        }
        
        // 5. ¡LA MAGIA DEL CIRCUITO! Multiplicamos la lista según las series.
        // Si base = [A, B, C] y series = 3, la lista final será [A, B, C, A, B, C, A, B, C]
        if (ejerciciosBase != null && !ejerciciosBase.isEmpty()) {
            for (int s = 0; s < this.seriesTotales; s++) {
                for (Ejercicio ejBase : ejerciciosBase) {
                    // Copiamos el objeto para evitar conflictos de memoria (referencias)
                    Ejercicio copiaEjercicio = new Ejercicio(
                        ejBase.getNombre(), 
                        ejBase.getRutavideo(), 
                        ejBase.getRutaImagen(), 
                        ejBase.getIntrucciones(), 
                        ejBase.getRepeticionesObjetivo()
                    );
                    this.listaCargada.add(copiaEjercicio);
                }
            }
        }
    }

    public int getTiempoDescansoConfigurado() {
        Rutina r = PantallaConfiguracionRutinaController.mapaRutinas.get(llaveRutina);
        return (r != null) ? r.getTiempoDescanso() : 30;
    }

    // Devuelve el ejercicio basado en el índice actual (que empieza en 0)
    public Ejercicio getEjercicioActualData() {
        if (listaCargada != null && !listaCargada.isEmpty() && ejercicioActual < listaCargada.size()) {
            return listaCargada.get(ejercicioActual);
        }
        return null;
    }
    
    // Devuelve el ejercicio siguiente para las pantallas de Descanso
    public Ejercicio getSiguienteEjercicioData() {
        if (listaCargada != null && (ejercicioActual + 1) < listaCargada.size()) {
            return listaCargada.get(ejercicioActual + 1);
        }
        return null;
    }
    
    // Simplificamos la lógica de avance porque la lista ya contiene todo el circuito expandido
    public String obtenerSiguientePantalla() {
    ejercicioActual++; // ← ESTA línea faltaba

    // Actualizamos la serie visual
    List<Ejercicio> base = CatalogoEjercicios.obtenerRutina(
        musculoSeleccionado, nivelSeleccionado, usaMancuernas
    );
    if (base != null && !base.isEmpty()) {
        this.serieActual = (ejercicioActual / base.size()) + 1;
    }

    if (ejercicioActual >= getTotalEjercicios()) {
        return "PantallaResumen";
    }
    return "PantallaDescanso";
}
    
    public int getTotalEjercicios() { return listaCargada != null ? listaCargada.size() : 0; }
    
    // Ojo: Para fines VISUALES (mostrar "Ejercicio 1", "Ejercicio 2"), devolvemos +1
    public int getEjercicioActual() { return ejercicioActual + 1; }
    
    public void avanzarAlSiguienteEjercicio() { 
        if (ejercicioActual < getTotalEjercicios()) { 
            ejercicioActual++; 
            
            // Actualizamos dinámicamente en qué "serie real" va basándonos en el tamaño base
            List<Ejercicio> ejerciciosBase = CatalogoEjercicios.obtenerRutina(musculoSeleccionado, nivelSeleccionado, usaMancuernas);
            if (ejerciciosBase != null && !ejerciciosBase.isEmpty()) {
                this.serieActual = (ejercicioActual / ejerciciosBase.size()) + 1;
            }
        } 
    }
    
    public void sumarRepeticiones(int reps) { if (reps > 0) { this.totalRepeticionesReales += reps; } }
    public int getTotalRepeticionesReales() { return totalRepeticionesReales; }
    public void setSeriesTotales(int seriesTotales) { this.seriesTotales = seriesTotales; }
    public int getSeriesTotales() { return seriesTotales; }
    public int getSerieActual() { return Math.min(serieActual, seriesTotales); } // Para no pasar el límite en la UI

    public void reiniciarRutina() {
            this.ejercicioActual = 0;
            this.serieActual = 1;
            this.totalRepeticionesReales = 0;
            this.listaCargada.clear();
            this.pesoExtraKg = 0.0; 
    }

    public String getMusculoSeleccionado() { return musculoSeleccionado; }
    public String getNivelSeleccionado() { return nivelSeleccionado; }
    public boolean isUsaMancuernas() { return usaMancuernas; }
    public void setMusculoSeleccionado(String m) { this.musculoSeleccionado = m; }
    public void setNivelSeleccionado(String n) { this.nivelSeleccionado = n; }
    public void setUsaMancuernas(boolean u) { this.usaMancuernas = u; }
    public void setLlaveRutina(String l) { this.llaveRutina = l; } 
    public String getLlaveRutina() { return llaveRutina; }
    public int getIdUsuarioLogueado() { return idUsuarioLogueado; }
    public void setIdUsuarioLogueado(int idUsuarioLogueado) { this.idUsuarioLogueado = idUsuarioLogueado; }
    public double getPesoExtraKg() { return pesoExtraKg; }
    public void setPesoExtraKg(double pesoExtraKg) { this.pesoExtraKg = pesoExtraKg; }
}