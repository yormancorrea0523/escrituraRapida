package com.example.escriturarapida;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
/**
 * Clase principal de la aplicación JavaFX "Escritura Rápida".
 *
 * <p>Extiende {@link Application}, por lo que JavaFX se encarga de crear la
 * ventana principal ({@link Stage}) y de llamar a {@link #start(Stage)}.
 * Normalmente se inicia a través de {@link Launcher}.</p>
 */
public class HelloApplication extends Application {
    /**
     * Punto de entrada de la interfaz gráfica.
     *
     * <p>Carga la vista {@code hello-view.fxml} (asociada a
     * {@link HelloController}), crea una escena de 320x240 y muestra la
     * ventana.</p>
     *
     * @param stage ventana principal proporcionada por JavaFX
     * @throws IOException si no se puede cargar el archivo FXML
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
