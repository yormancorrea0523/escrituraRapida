package com.example.escriturarapida;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controlador de la pantalla de resultados (vista
 * {@code resultados-view.fxml}).
 *
 * <p>Se muestra cuando termina la partida. Presenta cuántos niveles superó
 * el jugador y el tiempo restante, y permite volver a la pantalla principal
 * para jugar de nuevo.</p>
 */
public class ResultadosController {

    /** Etiqueta que muestra la cantidad de niveles superados. */
    @FXML
    private Label nivelesLabel;

    /** Etiqueta que muestra el tiempo restante al finalizar. */
    @FXML
    private Label tiempoFinalLabel;

    /** Etiqueta del título "¡Fin del Juego!" (definida en el FXML). */
    @FXML
    private Label tituloLabel;

    /** Botón "Volver a Jugar"; también se usa para obtener la ventana actual. */
    @FXML
    private Button volverJugarButton;

    /**
     * Regresa a la pantalla principal del juego. Se ejecuta al pulsar el
     * botón "Volver a Jugar".
     *
     * <p>Carga de nuevo {@code hello-view.fxml} (lo que crea un
     * {@link HelloController} nuevo, por lo que el juego inicia desde cero)
     * y reemplaza la escena de la ventana actual.</p>
     *
     * <p>Si el archivo FXML no puede cargarse, el error se imprime en la
     * consola.</p>
     */
    @FXML
    private void volverJugar() {
        try {
            FXMLLoader cargador = new FXMLLoader(
                    getClass().getResource("hello-view.fxml")
            );

            Parent ventanaPrincipal = cargador.load();
            Scene escena = new Scene(ventanaPrincipal);

            Stage ventana = (Stage) volverJugarButton.getScene().getWindow();
            ventana.setScene(escena);
            ventana.setTitle("Escritura Rápida");
            ventana.show();

        } catch (IOException e) {
            System.out.println("Error al volver al juego: " + e.getMessage());
        }
    }

    /**
     * Muestra en pantalla los datos de la partida terminada. Es llamado por
     * {@link HelloController} justo antes de mostrar esta vista.
     *
     * @param nivelesSuperados cantidad de niveles que el jugador completó
     * @param tiempoFinal      segundos que quedaban en el reloj al terminar
     */
    public void mostrarDatos(int nivelesSuperados, int tiempoFinal) {
        nivelesLabel.setText("Niveles superados: " + nivelesSuperados);
        tiempoFinalLabel.setText("Tiempo restante: " + tiempoFinal + " s");
    }

}
