package omr.uacm.sistemafitguide.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import omr.uacm.sistemafitguide.*;
import omr.uacm.sistemafitguide.excepciones.PesoFueraDeRangoException;
import omr.uacm.sistemafitguide.modelos.Usuario;

public class PantallaPerfilController implements Initializable {

    @FXML private Label lblNombre;
    @FXML private Label lblEdad;
    @FXML private Label lblEstatura;
    @FXML private Label lblIMC;
    @FXML private Label lblCategoriaIMC;
    @FXML private Label lblGrasa;
    @FXML private TextField txtNuevoPeso;

    private Usuario usuarioActual;
    private UsuarioDAO dao = new UsuarioDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatosUsuario();
    }

    private void cargarDatosUsuario() {
        // 1. Obtener quién está usando la app
        int idLogueado = GestionRutina.getInstance().getIdUsuarioLogueado();

        // 2. Extraer todos sus datos de la Base de Datos
        usuarioActual = dao.obtenerUsuarioPorId(idLogueado);

        if (usuarioActual != null) {
            // 3. Pintar datos estáticos
            lblNombre.setText(usuarioActual.getNombreUsuario().toUpperCase());
            lblEstatura.setText(usuarioActual.getAltura() + " m");
            txtNuevoPeso.setText(String.valueOf(usuarioActual.getPeso())); // Lo ponemos en el textfield por si quiere editarlo

            // 4. Cálculos Biométricos
            int edad = BiometriaUtils.calcularEdad(usuarioActual.getFechaNacimiento());
            lblEdad.setText(edad + " años");

            double imc = BiometriaUtils.calcularIMC(usuarioActual.getPeso(), usuarioActual.getAltura());
            lblIMC.setText(String.format("%.2f", imc));
            lblCategoriaIMC.setText(BiometriaUtils.obtenerCategoriaIMC(imc));

            // Restricción Médica de la OMS para porcentaje de grasa
            double grasa = BiometriaUtils.calcularPorcentajeGrasa(imc, edad, usuarioActual.getGenero());
            if (grasa != -1.0) {
                lblGrasa.setText(String.format("%.2f", grasa) + " %");
            } else {
                lblGrasa.setText("No aplicable (Menor de edad o género no definido)");
            }
        }
    }

    @FXML
    private void accionActualizarPeso(ActionEvent event) {
        try {
            double nuevoPeso = Double.parseDouble(txtNuevoPeso.getText().trim());

            // Reutilizamos su excepción genial para blindar la actualización
            if (nuevoPeso < 20 || nuevoPeso > 200) {
                throw new PesoFueraDeRangoException("Peso inválido. Debe estar entre 20 y 200 kg.");
            }

            // Actualizar en SQLite
            boolean exito = dao.actualizarPeso(usuarioActual.getIdUsuario(), nuevoPeso);

            if (exito) {
                mostrarAlertaInfo("Actualización", "¡Tu peso se ha actualizado correctamente en tu expediente!");
                cargarDatosUsuario(); // Volvemos a calcular su IMC y Grasa automáticamente
            } else {
                mostrarAlertaError("Error de BD", "No se pudo actualizar el peso.");
            }

        } catch (PesoFueraDeRangoException e) {
            mostrarAlertaError("Restricción Médica", e.getMessage());
        } catch (NumberFormatException e) {
            mostrarAlertaError("Formato Incorrecto", "Por favor, ingresa solo números (ej. 70.5).");
        }
    }

    @FXML
    private void accionVolverMenu(ActionEvent event) {
        try {
            App.setRoot("PantallaBienvenidoUsuario");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void mostrarAlertaInfo(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo); alerta.setHeaderText(null); alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void mostrarAlertaError(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo); alerta.setHeaderText(null); alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}