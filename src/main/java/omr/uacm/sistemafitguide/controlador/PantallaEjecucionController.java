/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

package omr.uacm.sistemafitguide.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import omr.uacm.sistemafitguide.*;
import omr.uacm.sistemafitguide.excepciones.MultimediaNoEncontradaException;

/**
 * FXML Controller class
 *
 * @author MiguelDiaz 
 */
public class PantallaEjecucionController implements Initializable {

    @FXML private ImageView idmediaVideo;
    @FXML private Label lblNombreEjercicio;
    @FXML private Label lblMeta;
    @FXML private TextField txtRepeticionesReales;
    @FXML private Button btnSiguiente;
    @FXML private Button btnAyuda;
    @FXML private Label lblInstrucciones;

    @FXML private VBox panelInfoEjercicio;
    @FXML private Label lblInfoMensaje;

    private int repeticionesObjetivo = 15;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (!validarInyeccionesFXML()) {
            System.err.println("[ERROR CRÍTICO] Falla de enlace FXML. Revisa los fx:id.");
            return;
        }

        if (panelInfoEjercicio != null) {
            panelInfoEjercicio.setVisible(false);
        }

        txtRepeticionesReales.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                txtRepeticionesReales.setText(newVal.replaceAll("[^\\d]", ""));
            } else if (newVal.length() > 3) {
                txtRepeticionesReales.setText(oldVal);
            }
        });

        cargarVideoDinamico();
    }

    private boolean validarInyeccionesFXML() {
        return idmediaVideo != null && lblNombreEjercicio != null &&
                lblMeta != null && txtRepeticionesReales != null &&
                btnSiguiente != null && lblInstrucciones != null;
    }

    private void cargarVideoDinamico() {
        GestionRutina gestion = GestionRutina.getInstance();
        Ejercicio ej = gestion.getEjercicioActualData();

        if (ej != null) {
            String nombreEjercicio = ej.getNombre();
            lblNombreEjercicio.setText("Ejercicio " + gestion.getEjercicioActual() + ": " + nombreEjercicio);
            lblInstrucciones.setText(ej.getIntrucciones());

            int repsAsignadas = ej.getRepeticionesObjetivo(); // Valor base por defecto
            
            String musculoEnEjecucion = gestion.getMusculoSeleccionado();

            if (musculoEnEjecucion != null) {
                //buscamos ne el mama que musculo esta entrenando en ese instante
                String llaveActiva = GestionRutina.getInstance().getLlaveRutina();
                Rutina rutinaActiva = PantallaConfiguracionRutinaController.mapaRutinas.get(llaveActiva);

                if (rutinaActiva != null && rutinaActiva.getMapaRepeticiones() != null) {
                    if (rutinaActiva.getMapaRepeticiones().containsKey(nombreEjercicio)) {
                        repsAsignadas = rutinaActiva.getMapaRepeticiones().get(nombreEjercicio);
                        System.out.println("[INFO] Meta personalizada encontrada para " + nombreEjercicio + ": " + repsAsignadas);
                    }
                }
            }

            this.repeticionesObjetivo = repsAsignadas; 
            
            // --- NUEVA LÓGICA: Incorporar el peso en la meta ---
            double pesoExtra = gestion.getPesoExtraKg();
            if (pesoExtra > 0) {
                 // Formatemos el peso para quitar el .0 si es entero (ej. 2.0 -> 2, pero 2.5 se queda igual)
                 String formatoPeso = (pesoExtra == Math.floor(pesoExtra)) 
                                      ? String.valueOf((int) pesoExtra) 
                                      : String.valueOf(pesoExtra);
                 
                 lblMeta.setText("Meta: " + this.repeticionesObjetivo + " Reps con " + formatoPeso + " kg");
            } else {
                 lblMeta.setText("Meta: " + this.repeticionesObjetivo + " Repeticiones");
            }

// cargar gif
            String rutaAnimacion = ej.getRutavideo();
            try {
                URL urlAnimacion = null;
                if (rutaAnimacion != null && !rutaAnimacion.trim().isEmpty()) {
                    if (!rutaAnimacion.startsWith("/")) {
                        rutaAnimacion = "/" + rutaAnimacion;
                    }
                    urlAnimacion = getClass().getResource(rutaAnimacion);
                }

                // EXCEPCIÓN 5: Multimedia No Encontrada
                if (urlAnimacion == null) {
                    throw new MultimediaNoEncontradaException("La animación no existe en la ruta: " + rutaAnimacion);
                }

                idmediaVideo.setImage(new Image(urlAnimacion.toExternalForm()));

            } catch (MultimediaNoEncontradaException ex) {
                System.err.println("[ADVERTENCIA] " + ex.getMessage());
                lblInstrucciones.setText("[ANIMACIÓN NO ENCONTRADA] " + ej.getIntrucciones());
            } catch (Exception ex) {
                System.err.println("Error general al cargar animación: " + ex.getMessage());
            }
        }
    }

    @FXML
    public void registrarYContinuar() {
        int reps = repeticionesObjetivo;
        String input = txtRepeticionesReales.getText();
        if (input != null && !input.trim().isEmpty()) {
            try { reps = Integer.parseInt(input); } catch (NumberFormatException e) { }
        }

        GestionRutina.getInstance().sumarRepeticiones(reps);

        try {
            App.setRoot(GestionRutina.getInstance().obtenerSiguientePantalla());
        } catch (IOException e) {
            System.err.println("Error de navegación: " + e.getMessage());
        }
    }

    @FXML
    public void mostrarInfoEjercicio() {
        GestionRutina gestion = GestionRutina.getInstance();
        Ejercicio actual = gestion.getEjercicioActualData();
        
        if (actual == null) return;
        
        String nombre = actual.getNombre();
        String info = "Trabajas múltiples grupos musculares y mejoras tu resistencia."; 
        
        switch (nombre) {
            case "Flexiones en pared": info = "Enfocado en el pecho, la parte frontal del hombro y los tríceps."; break;
            case "Flexiones cobra": info = "Trabajas tríceps, hombros, espalda baja y estiras el abdomen."; break;
            case "Plancha antebrazo a mano": info = "Fortaleces hombros, tríceps y mejoras la estabilidad de tu core."; break;
            case "Flexiones elevadas": info = "Trabaja la parte inferior del pecho, tríceps y hombros."; break;
            case "Flexiones diamante": info = "Enfoque principal en los tríceps y la parte interna del pecho."; break;
            case "Paso de granjero": info = "Trabajas tu agarre, antebrazos, trapecios y estabilizas el abdomen."; break;
            case "Curl de bíceps": info = "Enfoque total en el bíceps braquial (desarrollo del brazo)."; break;
            case "Elevación de antebrazo": info = "Fortaleces los músculos flexores y extensores de las muñecas."; break;
            case "Curl sostenido": info = "Genera tensión isométrica, fortaleciendo bíceps y antebrazos."; break;
            case "Crunch abdominal": info = "Trabajas principalmente el recto abdominal (abdomen superior)."; break;
            case "Elevación de piernas": info = "Enfocado en el abdomen inferior y los flexores de la cadera."; break;
            case "Plancha": info = "Trabajas todo el core (transverso abdominal), hombros y espalda."; break;
            case "V-Ups (Abdominales en V)": info = "Ejercicio completo: activas abdomen superior e inferior a la vez."; break;
            case "Sentadilla": info = "Fortaleces cuádriceps (muslos), glúteos e isquiotibiales."; break;
            case "Zancadas": info = "Trabajas cuádriceps, glúteos y mejoras tu equilibrio general."; break;
        }
        
        lblInfoMensaje.setText(info);
        panelInfoEjercicio.setVisible(true);
    }

    @FXML
    public void accionCerrarInfo() {
        panelInfoEjercicio.setVisible(false);
    }
}