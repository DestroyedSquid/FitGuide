package omr.uacm.sistemafitguide;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import javafx.scene.image.Image;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
  
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("PantallaInicio"), 900, 600);
        
        try {
            Image icon = new Image(App.class.getResourceAsStream("/omr/uacm/sistemafitguide/imagenes/logo.jpg"));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            System.err.println("No se encontró el ícono principal.");
        }
        
        stage.setScene(scene);
        stage.setTitle("FitGuide");
        stage.show();
    }    

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        URL resource = App.class.getResource(fxml + ".fxml");
        if (resource == null) {
            throw new IOException("No se encontró el FXML: " + fxml + ".fxml — verifica que el proyecto esté compilado.");
        }
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        try {
            return fxmlLoader.load();
        } catch (IOException e) {
            System.err.println("[ERROR FXML] Fallo al cargar " + fxml + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.err.println("[ERROR FXML] Excepción inesperada cargando " + fxml + ": " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Error al cargar " + fxml + ": " + e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        ConexionDB.inicializarBaseDatos(); // inicializa la base de datos SQLite al arrancar
        
        launch();
    }
}