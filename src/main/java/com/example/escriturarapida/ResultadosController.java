package com.example.escriturarapida;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ResultadosController {

    @FXML
    private Label nivelesLabel;

    @FXML
    private Label tiempoFinalLabel;

    @FXML
    private Label tituloLabel;

    @FXML
    private Button volverJugarButton;

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

    public void mostrarDatos(int nivelesSuperados, int tiempoFinal) {
        nivelesLabel.setText("Niveles superados: " + nivelesSuperados);
        tiempoFinalLabel.setText("Tiempo restante: " + tiempoFinal + " s");
    }

}
