package omr.uacm.sistemafitguide;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Controlador para la gestión de creación y edición de ejercicios en el catálogo del Coach.
 *
 * @author Coach Module — FitGuide
 */
public class PantallaCoachFormularioEjercicioController implements Initializable {

    public static boolean modoEdicion = false;
    public static EjercicioCoach ejercicioAEditar = null;

    @FXML private Button btnCancelar;
    @FXML private Label lblTituloPantalla;
    @FXML private Label lblSubtituloPantalla;
    @FXML private TextField txtNombre;
    @FXML private ComboBox<String> cmbZonaCorporal;
    @FXML private ComboBox<String> cmbNivel;
    @FXML private ComboBox<String> cmbTipoPeso;
    @FXML private TextField txtRepeticiones;
    @FXML private TextField txtSeries;
    @FXML private TextField txtDescanso;
    @FXML private TextField txtDuracion;
    @FXML private TextField txtUrlImagen;
    @FXML private TextField txtUrlVideo;
    @FXML private TextArea txtDescripcion;
    @FXML private Button btnGuardar;

    private final EjercicioCatalogoDao dao = new EjercicioCatalogoDao();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ConfigurarCombos();
        if (modoEdicion && ejercicioAEditar != null) {
            CargarDatosEdicion(ejercicioAEditar);
        }
    }

    private void ConfigurarCombos() {
        cmbZonaCorporal.getItems().addAll("Brazo", "Abdomen", "Pierna");
        cmbNivel.getItems().addAll("Básico", "Intermedio", "Avanzado");
        cmbTipoPeso.getItems().addAll("Peso Propio", "Peso Extra");
    }

    private void CargarDatosEdicion(EjercicioCoach ejercicio) {
        lblTituloPantalla.setText("Editar Ejercicio");
        lblSubtituloPantalla.setText("Modifica los atributos del ejercicio seleccionado");

        txtNombre.setText(ejercicio.getNombre());
        cmbZonaCorporal.setValue(ejercicio.getZonaCorporal());
        cmbNivel.setValue(ejercicio.getNivel());
        cmbTipoPeso.setValue(ejercicio.getTipoPeso());
        txtRepeticiones.setText(String.valueOf(ejercicio.getRepeticiones()));
        txtSeries.setText(String.valueOf(ejercicio.getSeries()));
        txtDescanso.setText(String.valueOf(ejercicio.getDescanso()));
        txtDuracion.setText(String.valueOf(ejercicio.getDuracion()));
        txtUrlImagen.setText(ejercicio.getUrlImagen());
        txtUrlVideo.setText(ejercicio.getUrlVideo());
        txtDescripcion.setText(ejercicio.getDescripcion());
    }

    @FXML
    public void GuardarEjercicio(ActionEvent event) {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            MostrarAlerta("Validación", "El nombre del ejercicio es obligatorio.", Alert.AlertType.WARNING);
            return;
        }

        int reps = 12;
        int series = 3;
        int descanso = 30;
        int duracion = 0;

        try {
            if (!txtRepeticiones.getText().trim().isEmpty()) {
                reps = Integer.parseInt(txtRepeticiones.getText().trim());
            }
            if (!txtSeries.getText().trim().isEmpty()) {
                series = Integer.parseInt(txtSeries.getText().trim());
            }
            if (!txtDescanso.getText().trim().isEmpty()) {
                descanso = Integer.parseInt(txtDescanso.getText().trim());
            }
            if (!txtDuracion.getText().trim().isEmpty()) {
                duracion = Integer.parseInt(txtDuracion.getText().trim());
            }
        } catch (NumberFormatException e) {
            MostrarAlerta("Error de Formato", "Revisa que los campos numéricos contengan valores válidos.", Alert.AlertType.ERROR);
            return;
        }

        boolean resultado;
        if (modoEdicion && ejercicioAEditar != null) {
            ejercicioAEditar.setNombre(nombre);
            ejercicioAEditar.setZonaCorporal(cmbZonaCorporal.getValue());
            ejercicioAEditar.setNivel(cmbNivel.getValue());
            ejercicioAEditar.setTipoPeso(cmbTipoPeso.getValue());
            ejercicioAEditar.setRepeticiones(reps);
            ejercicioAEditar.setSeries(series);
            ejercicioAEditar.setDescanso(descanso);
            ejercicioAEditar.setDuracion(duracion);
            ejercicioAEditar.setDescripcion(txtDescripcion.getText().trim());
            ejercicioAEditar.setUrlImagen(txtUrlImagen.getText().trim());
            ejercicioAEditar.setUrlVideo(txtUrlVideo.getText().trim());

            resultado = dao.Actualizar(ejercicioAEditar);
        } else {
            EjercicioCoach nuevo = new EjercicioCoach(
                    nombre,
                    cmbZonaCorporal.getValue(),
                    cmbNivel.getValue(),
                    cmbTipoPeso.getValue(),
                    reps,
                    series,
                    duracion,
                    descanso,
                    txtDescripcion.getText().trim(),
                    txtUrlImagen.getText().trim(),
                    txtUrlVideo.getText().trim()
            );
            resultado = dao.Agregar(nuevo);
        }

        if (resultado) {
            MostrarAlerta("Éxito", "Operación realizada correctamente.", Alert.AlertType.INFORMATION);
            RegresarCatalogo();
        } else {
            MostrarAlerta("Error", "No se pudo guardar el ejercicio en la base de datos.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void Cancelar(ActionEvent event) {
        RegresarCatalogo();
    }

    private void RegresarCatalogo() {
        try {
            modoEdicion = false;
            ejercicioAEditar = null;
            App.setRoot("PantallaCoachCatalogoEjercicios");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void MostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Alias camelCase para compatibilidad
    @FXML public void guardarEjercicio(ActionEvent event) { GuardarEjercicio(event); }
    @FXML public void cancelar(ActionEvent event) { Cancelar(event); }
}