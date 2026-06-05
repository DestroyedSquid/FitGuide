/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

package omr.uacm.sistemafitguide.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import java.time.YearMonth;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import omr.uacm.sistemafitguide.App;
import omr.uacm.sistemafitguide.excepciones.RutinaNoConfiguradaException;

/**
 * FXML Controller class
 *
 * @author MiguelDiaz
 */

public class PantallaBienvenidoUsuarioController implements Initializable {

    @FXML private Button btnEmpezar;
    @FXML private Button btnCerrarSesion;
    @FXML private VBox panelNotificacion;
    @FXML private Label lblNotificacionTitulo;
    @FXML private Label lblNotificacionMensaje;
    @FXML private GridPane gridCalendario;
    @FXML private Label lblRachaTexto;

    // VARIABLE CLAVE PARA LA EXCEPCIÓN
    public static boolean rutinaConfigurada = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        generarCalendarioDinamico();
    }    

    @FXML
    private void accionEmpezarRutina() {
        try {
            // Validamos si la bandera sigue en false
            if (!rutinaConfigurada) {
                throw new RutinaNoConfiguradaException("No puedes iniciar el entrenamiento. ¡Primero debes ir a 'PERSONALIZAR RUTINA' para personalizar tus ejercicios!");
            }
            
            // Si ya está configurada, avanza normal
            App.setRoot("PantallaListaEjercicios");
            
        } catch (RutinaNoConfiguradaException e) {
            mostrarAlertaError("Rutina Inexistente", e.getMessage());
        } catch (IOException e) {
            mostrarAlertaError("Error de Navegación", "No se pudo cargar la pantalla de ejercicios.");
        }
    }

    @FXML 
    private void accionCerrarSesion(ActionEvent event) throws IOException {
        App.setRoot("PantallaInicio");
    }
    
    // CONSEJOS DE MOTIVACIÓN Y SALUD

    @FXML public void mostrarNotificacionDisciplina() {
        mostrarTarjeta("La disciplina", "La disciplina vence al talento cuando el talento no se está esforzando.");
    }
    
    @FXML public void mostrarNotificacionRindas() {
        mostrarTarjeta("No te rindas", "Vas muy bien, sigue asi y veras tus grandes resultados.");
    }
    
    @FXML public void mostrarNotificacionHoyCuenta() {
        mostrarTarjeta("¡Hoy y todos los días cuenta!", "Todos tus esfuerzos diarios sin importar que tan grande sean cuentan.");
    }
    
   @FXML public void mostrarNotificacionMaximo() {
        mostrarTarjeta("¡Da tu máximo siempre!", "Recuerda todos los dias siempre puedes dar todo de ti. ");
    }
    
    @FXML public void mostrarNotificacionHidratate() {
        mostrarTarjeta("¡A tomar agua!", "Mantente hidratado durante todo el día, ayuda a la recuperación muscular y a tu nivel de energía.");
    }
    
    @FXML public void mostrarNotificacionDolor() {
        mostrarTarjeta("¡Cuidado con los dolores!", "Es normal sentir un poco de dolor al realizar ejercicio pero si el dolor es grande o punzante acude a un doctor.");
    }
    
    @FXML public void mostrarNotificacionRespiracion() {
        mostrarTarjeta("¡Siempre respira hondo!", "Durante toda tu rutina procura siempre respirar ondo para tener mejor concentración.");
    }
    
    @FXML public void mostrarNotificacionPostura() {
        mostrarTarjeta("¡TU postura importa!", "Procura tener siempre una postura recta al realizar cualquier actividad y más si es fisica.");
    }
    
    @FXML
    private void accionConfigurarRutina() {
        try {
            App.setRoot("PantallaConfiguracionRutina");
        } catch (IOException e) {
            System.err.println("Fallo al intentar cargar configuración de rutina: " + e.getMessage());
        }
    }


    // LOGICA DE NOTIFICACIONES Y CALENDARIO


    private void mostrarTarjeta(String titulo, String mensaje) {
        lblNotificacionTitulo.setText(titulo);
        lblNotificacionMensaje.setText(mensaje);
        panelNotificacion.setVisible(true);
    }

    @FXML
    public void accionCerrarNotificacion() {
        panelNotificacion.setVisible(false);
    }
    
    private void generarCalendarioDinamico() {
        LocalDate fechaActual = LocalDate.now();
        YearMonth mesActual = YearMonth.from(fechaActual);
        int diasEnElMes = mesActual.lengthOfMonth();
        
        LocalDate primerDiaDelMes = mesActual.atDay(1);
        int columnaInicial = primerDiaDelMes.getDayOfWeek().getValue() - 1; 

        int filaActual = 1; 
        int columnaActual = columnaInicial;
        
        int racha = 1; 
        int diaDeHoy = fechaActual.getDayOfMonth();
       if (racha == 1) {
            lblRachaTexto.setText(racha + " DÍA DE RACHA");
        } else {
            lblRachaTexto.setText(racha + " DÍAS DE RACHA");
        }

        for (int dia = 1; dia <= diasEnElMes; dia++) {
            
            Label lblDia = new Label(String.valueOf(dia));
            lblDia.setAlignment(Pos.CENTER);
            lblDia.setPrefSize(28, 28);
            lblDia.setFont(javafx.scene.text.Font.font("System", javafx.scene.text.FontWeight.BOLD, 12));
            
            if (dia == diaDeHoy) {
                lblDia.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 50%; -fx-border-color: #FF9800; -fx-border-radius: 50%; -fx-border-width: 2;");
            } 
            else if (dia >= (diaDeHoy - racha) && dia < diaDeHoy) {
                lblDia.setStyle("-fx-background-color: #2D5A27; -fx-text-fill: white; -fx-background-radius: 5;");
            } 
            else if (dia < diaDeHoy) {
                lblDia.setStyle("-fx-background-color: #E0E0E0; -fx-text-fill: #9E9E9E; -fx-background-radius: 5;");
            } 
            else {
                lblDia.setStyle("-fx-background-color: transparent; -fx-text-fill: #333333;");
            }

            gridCalendario.add(lblDia, columnaActual, filaActual);

            columnaActual++;
            if (columnaActual > 6) {
                columnaActual = 0;
                filaActual++;
            }
        }
    }
    @FXML
    private void accionVerPerfil(ActionEvent event) {
        try {
            App.setRoot("PantallaPerfil");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    // metodo para mostrar las alertas de excepción en esta pantalla
    private void mostrarAlertaError(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
