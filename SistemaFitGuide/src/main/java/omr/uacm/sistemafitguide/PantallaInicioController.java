/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package omr.uacm.sistemafitguide;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;
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

/**
 * FXML Controller class
 *
 * @author MiguelDiaz
 */
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
    private String generoSeleccionado = "";
    @FXML private Button btnHombre;
    @FXML private Button btnMujer;
    
    //se instandia lo del usuario DAO
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vboxLogin.setVisible(true);
        vboxRegistro.setVisible(false);
    }

    @FXML
    private void mostrarPantallaLogin(ActionEvent event) {
        vboxLogin.setVisible(true);
        vboxRegistro.setVisible(false);
    }

    @FXML
    private void mostrarPantallaRegistro(ActionEvent event) {
        vboxLogin.setVisible(false);
        vboxRegistro.setVisible(true);
    }

    @FXML
    public void irAModoCoach(ActionEvent event) throws IOException {
        App.setRoot("PantallaCoachMenu");
    }

    @FXML
    public void IrAModoCoach(ActionEvent event) throws IOException {
        irAModoCoach(event);
    }

    // INICIAR SESIOn 
    @FXML
    private void accionIniciarSesion(ActionEvent event) {
        try {
            String usuario = txtUsuario.getText(); 
            String contrasena = pfContrasenaLogin.getText();
        
            // validamos campos vacios
            if (usuario == null || usuario.trim().isEmpty() || contrasena == null || contrasena.trim().isEmpty()) {
                throw new CuentaDatosIncompletosException("Faltan datos. Por favor, ingresa tanto tu usuario como tu contraseña.");
            }
            
            //consultamos la base de datos 
            int idUsuario = usuarioDAO.verificarLogin(usuario, contrasena);
        
            // si nos regresa -1 las credenciales  estal mal
            if (idUsuario == -1) {
                throw new CredencialesInvalidasException("Usuario o contraseña incorrectos. Inténtalo de nuevo.");
            }
            
            // guardamos loa datos del usuario
            Sesion.idUsuarioActivo = idUsuario;
            // cargar las rutinas guardadas de este usuario
            new RutinaGuardadaDAO().cargarRutinasUsuario(idUsuario);
            // si tiene al menos una rutina lo deja pasar
            PantallaBienvenidoUsuarioController.rutinaConfigurada = 
                !PantallaConfiguracionRutinaController.mapaRutinas.isEmpty();
            Sesion.nombreUsuarioActivo = usuario;
            
            System.out.println("Sesión iniciada. Bienvenido, " + usuario + " (ID: " + idUsuario + ")");
        
            // Pasa a la siguiente pantalla
            App.setRoot("PantallaBienvenidoUsuario");
        
        } catch (CuentaDatosIncompletosException e) {
            mostrarAlertaError("Datos Incompletos", e.getMessage());
        } catch (CredencialesInvalidasException e) {
            mostrarAlertaError("Acceso Denegado", e.getMessage());
        } catch (IOException e) {
            mostrarAlertaError("Error de Pantalla", "No se pudo cargar la siguiente pantalla:\n" + e.getMessage());
            e.printStackTrace();
        }
    }   

    @FXML
    private void recuperarContraseña(ActionEvent event) {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Recuperar Contraseña");
        info.setHeaderText(null);
        info.setContentText("Esta función no está disponible aún. Contacta al administrador si olvidaste tu contraseña.");
        info.showAndWait();
    }

    // REGISTRO
    @FXML
    private void accionRegistrarmeBaseDatos(ActionEvent event) {
        try {
            String usuario = txtUsuarioRegistro.getText();
            String pesoStr = txtPesoRegistro.getText();
            String alturaStr = txtAlturaRegistro.getText();
            String contrasena = pfContrasenaRegistro.getText();
            LocalDate fechaNacimiento = dpFechaNacimiento.getValue();

            // validmaos campos vacios
            if (usuario == null || usuario.trim().isEmpty() ||
                pesoStr == null || pesoStr.trim().isEmpty() ||
                alturaStr == null || alturaStr.trim().isEmpty() ||
                contrasena == null || contrasena.trim().isEmpty() ||
                fechaNacimiento == null || 
                generoSeleccionado.isEmpty()) { 
                throw new CuentaDatosIncompletosException("Faltan datos. Por favor, llena todos los campos y selecciona tu género.");
            }

            // Validacion de rango de peso
            double peso = Double.parseDouble(pesoStr);
            if (peso < 20 || peso > 200) {
                throw new PesoFueraDeRangoException("El peso es inválido. Tienes que pesar mínimo 20kg.");
            }

            // Validacion de rango de estatura
            double altura = Double.parseDouble(alturaStr);
            if (altura <= 1.0 || altura > 3.0) {
                throw new EstaturaFueraDeRangoException("La estatura es inválida. Tienes que medir mínimo 1 metro.");
            }

            // Validacion de rango de edad
            LocalDate hoy = LocalDate.now();
            int edad = Period.between(fechaNacimiento, hoy).getYears();
            
            if (edad < 15 || edad > 100) {
                throw new EdadFueraDeRangoException("Para usar FitGuide debes tener mínimo 15 años.");
            }

            // insertamos los datos para la abse de datos
            //contruimos el usuario con su informacion valida
            Usuario nuevoUsuario = new Usuario(usuario, contrasena, peso, altura, fechaNacimiento.toString(), generoSeleccionado);
            
            // Registramos y obtenemos el ID generado en un solo viaje a la BD
            int idUsuarioNuevo = usuarioDAO.registrarUsuario(nuevoUsuario);
            
            if (idUsuarioNuevo == -1) {
                throw new Exception("El nombre de usuario '" + usuario + "' ya existe o hubo un problema con el registro.");
            }

            System.out.println("Usuario registrado exitosamente en la BD. Iniciando sesión automática...");
            
            // Iniciamos sesión con el ID que ya tenemos (sin segundo viaje a BD)
            Sesion.idUsuarioActivo = idUsuarioNuevo;
            Sesion.nombreUsuarioActivo = usuario;
            new RutinaGuardadaDAO().cargarRutinasUsuario(idUsuarioNuevo);

            App.setRoot("PantallaBienvenidoUsuario");

        } catch (CuentaDatosIncompletosException e) {
            mostrarAlertaError("Datos de Registro Incompletos", e.getMessage());
        } catch (PesoFueraDeRangoException e) {
            mostrarAlertaError("Error en Peso", e.getMessage());
        } catch (EstaturaFueraDeRangoException e) {
            mostrarAlertaError("Error en Estatura", e.getMessage());
        } catch (EdadFueraDeRangoException e) {
            mostrarAlertaError("Error en Edad", e.getMessage());
        } catch (NumberFormatException e) {
            mostrarAlertaError("Formato Incorrecto", "El peso y la altura deben ser valores numéricos (ej. 70.5).");
        } catch (Exception e) {
            mostrarAlertaError("Error de Registro", e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void seleccionarHombre() {
        generoSeleccionado = "Hombre";
        btnHombre.setStyle("-fx-background-color: #1a5276; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-border-color: white; -fx-border-radius: 8; -fx-border-width: 2;");
        btnMujer.setStyle("-fx-background-color: #e91e63; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;");
    }
    
    @FXML
    private void seleccionarMujer() {
        generoSeleccionado = "Mujer";
        btnMujer.setStyle("-fx-background-color: #78281f; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-border-color: white; -fx-border-radius: 8; -fx-border-width: 2;");
        btnHombre.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;");
    }
    
    private void mostrarAlertaError(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}