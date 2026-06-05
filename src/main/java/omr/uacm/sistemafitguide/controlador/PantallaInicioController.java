package omr.uacm.sistemafitguide.controlador;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import omr.uacm.sistemafitguide.*;
import omr.uacm.sistemafitguide.excepciones.*;
import omr.uacm.sistemafitguide.modelos.Usuario;

public class PantallaInicioController implements Initializable {

    @FXML private VBox vboxLogin;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField pfContrasenaLogin;
    @FXML private Button btnIngresaSesion;
    @FXML private Hyperlink linkOlvidoContrasena;

    @FXML private VBox vboxRegistro;
    @FXML private TextField txtUsuarioRegistro;
    @FXML private TextField txtPesoRegistro;
    @FXML private TextField txtAlturaRegistro;
    @FXML private DatePicker dpFechaNacimiento;
    @FXML private PasswordField pfContrasenaRegistro;
    @FXML private Button btnRegistrarmeEnBD;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vboxLogin.setVisible(true);
        vboxRegistro.setVisible(false);
    }

    @FXML private void mostrarPantallaLogin(ActionEvent event) {
        vboxLogin.setVisible(true);
        vboxRegistro.setVisible(false);
    }

    @FXML private void mostrarPantallaRegistro(ActionEvent event) {
        vboxLogin.setVisible(false);
        vboxRegistro.setVisible(true);
    }

    @FXML
    private void accionIniciarSesion(ActionEvent event) {
        try {
            String usuario = txtUsuario.getText();
            String contrasena = pfContrasenaLogin.getText();

            if (usuario == null || usuario.trim().isEmpty() || contrasena == null || contrasena.trim().isEmpty()) {
                throw new CuentaDatosIncompletosException("Faltan datos. Por favor, ingresa tanto tu usuario como tu contraseña.");
            }

            // CONEXIÓN REAL A BASE DE DATOS
            Task<Integer> tareaLogin = new Task<Integer>() {
                @Override
                protected Integer call() throws Exception {
                    return new UsuarioDAO().validarLogin(usuario, contrasena);
                }
            };

            tareaLogin.setOnSucceeded(e -> {
                try {
                    int idUsuario = tareaLogin.getValue();
                    if (idUsuario != -1) {
                        GestionRutina.getInstance().setIdUsuarioLogueado(idUsuario);
                        App.setRoot("PantallaBienvenidoUsuario");
                    } else {
                        // EXCEPCIÓN 1: Credenciales Inválidas
                        throw new CredencialesInvalidasException("Usuario o contraseña incorrectos en la base de datos.");
                    }
                } catch (CredencialesInvalidasException ex) {
                    mostrarAlertaError("Acceso Denegado", ex.getMessage());
                } catch (IOException ex) {
                    mostrarAlertaError("Error", "No se pudo cargar la pantalla principal.");
                }
            });

            Thread hilo = new Thread(tareaLogin);
            hilo.setDaemon(true);
            hilo.start();

        } catch (CuentaDatosIncompletosException e) {
            mostrarAlertaError("Datos Incompletos", e.getMessage());
        }
    }

    @FXML private void recuperarContraseña(ActionEvent event) {}

    @FXML
    private void accionRegistrarmeBaseDatos(ActionEvent event) {
        try {
            String usuario = txtUsuarioRegistro.getText();
            String pesoStr = txtPesoRegistro.getText();
            String alturaStr = txtAlturaRegistro.getText();
            String contrasena = pfContrasenaRegistro.getText();
            LocalDate fechaNacimiento = dpFechaNacimiento.getValue();

            if (usuario == null || usuario.trim().isEmpty() || pesoStr == null || pesoStr.trim().isEmpty() ||
                    alturaStr == null || alturaStr.trim().isEmpty() || contrasena == null || contrasena.trim().isEmpty() ||
                    fechaNacimiento == null) {
                throw new CuentaDatosIncompletosException("Faltan datos. Por favor, llena todos los campos del registro.");
            }

            double peso = Double.parseDouble(pesoStr);
            if (peso < 20 || peso > 200) {
                throw new PesoFueraDeRangoException("El peso es inválido. Tienes que pesar mínimo 20kg.");
            }

            double altura = Double.parseDouble(alturaStr);
            if (altura <= 1.0 || altura > 3.0) {
                throw new EstaturaFueraDeRangoException("La estatura es inválida. Tienes que medir mínimo 1 metro.");
            }

            LocalDate hoy = LocalDate.now();
            int edad = Period.between(fechaNacimiento, hoy).getYears();
            if (edad < 15 || edad > 100) {
                throw new EdadFueraDeRangoException("Para usar FitGuide debes tener mínimo 15 años.");
            }

            Usuario nuevoUsuario = new Usuario(usuario, contrasena, peso, altura, fechaNacimiento.toString(), "No especificado");

            Task<Boolean> tareaRegistro = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    return new UsuarioDAO().registrarUsuario(nuevoUsuario);
                }
            };

            tareaRegistro.setOnSucceeded(e -> {
                try {
                    if (tareaRegistro.getValue()) {
                        mostrarAlertaInfo("Registro Exitoso", "¡Bienvenido a FitGuide! Tu perfil ha sido creado.");
                        mostrarPantallaLogin(null);
                        txtUsuario.setText(usuario);
                    } else {
                        // EXCEPCIÓN 2: Usuario Inválido (Ya existe)
                        throw new UsuarioInvalidoException("El nombre de usuario '" + usuario + "' ya está ocupado.");
                    }
                } catch (UsuarioInvalidoException ex) {
                    mostrarAlertaError("Usuario Inválido", ex.getMessage());
                }
            });

            Thread hiloRegistro = new Thread(tareaRegistro);
            hiloRegistro.setDaemon(true);
            hiloRegistro.start();

        } catch (CuentaDatosIncompletosException | PesoFueraDeRangoException | EstaturaFueraDeRangoException | EdadFueraDeRangoException e) {
            mostrarAlertaError("Error de Validación", e.getMessage());
        } catch (NumberFormatException e) {
            mostrarAlertaError("Formato Incorrecto", "El peso y la altura deben ser numéricos (ej. 70.5).");
        }
    }

    private void mostrarAlertaError(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void mostrarAlertaInfo(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}