package omr.uacm.sistemafitguide;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * Controlador del menú principal de administración para el rol de Coach.
 *
 * @author Coach Module — FitGuide
 */
public class PantallaCoachMenuController implements Initializable {

    @FXML private Label lblTitulo;
    @FXML private Label lblSubtitulo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización del menú de coach
    }

    @FXML
    public void IrACatalogoEjercicios(ActionEvent event) {
        try {
            App.setRoot("PantallaCoachCatalogoEjercicios");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void IrAGestionRutinas(ActionEvent event) {
        try {
            App.setRoot("PantallaCoachGestionRutinas");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void IrAMenuUsuario(ActionEvent event) {
        try {
            App.setRoot("PantallaInicio");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void CerrarSesion(ActionEvent event) {
        try {
            App.setRoot("PantallaLogin");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Alias camelCase
    @FXML public void irACatalogoEjercicios(ActionEvent event) { IrACatalogoEjercicios(event); }
    @FXML public void irAGestionRutinas(ActionEvent event) { IrAGestionRutinas(event); }
    @FXML public void irAMenuUsuario(ActionEvent event) { IrAMenuUsuario(event); }
    @FXML public void cerrarSesion(ActionEvent event) { CerrarSesion(event); }
}