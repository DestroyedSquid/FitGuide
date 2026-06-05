/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package omr.uacm.sistemafitguide.controlador;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import omr.uacm.sistemafitguide.App;

/*
 * @author migue
 */

public class PantallaResumenController implements Initializable {

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private Button btnInicio;

    @FXML
    private Button btnRegresar;

    @FXML
    private GridPane gridEjercicios; 

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Logica para la base de datos
    }    

    @FXML
    private void accionCerrarSesion(ActionEvent event) throws IOException {
        System.out.println("Cerrando sesión... yendo al Login");
        App.setRoot("PantallaInicio");
    }

    @FXML
    private void accionInicio(ActionEvent event) throws IOException {
        System.out.println("Yendo a la pantalla de Bienvenida...");
        App.setRoot("PantallaBienvenidoUsuario");
    }

    @FXML
    private void accionRegresar(ActionEvent event) throws IOException {
        System.out.println("Regresando a la pantalla anterior...");
        App.setRoot("PantallaListaEjercicios");
    }
}