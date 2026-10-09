package com.example.escriturarapida;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class HelloController {

    private String[] palabrasFaciles = {
            "gas",
            "sal",
            "faldas",
            "allá",
            "hadas",
            "Saldo",
            "Sallas",
    };

    private String[] palabrasIntermedias = {
            "Viento",
            "fuego",
            "permanente",
            "Crepúsculo",
            "atmósfera",
    };

    private String[] palabrasDificiles = {
        "Mañana será un gran día",
        "La practica hace al maestro.",
        "Lee cuidadosamente la frase antes de comenzar a escribirla.",
        "La tecnología avanza a un ritmo frenético.",
        "Quizás el éxito requiera más constancia que talento.",
        "Cada nivel superado demuestra que tu velocidad y precision mejoran.",
    };

    private int nivel=1;
    private int tiempoRestante = 20;
    private int tiempoPorNivel = 20;
    private Timeline temporizador;
    private boolean juegoActivo = false;
    private class ControlTemporizador {

        public void actualizarTiempo() {
            tiempoRestante--;
            tiempoLabel.setText("Tiempo: " + tiempoRestante + "s");
        }
    }

    @FXML
    private Label estadoLabel;

    @FXML
    private Label nivelLabel;

    @FXML
    private Label palabraLabel;

    @FXML
    private TextField respuestaField;

    @FXML
    private Label tiempoLabel;



    @FXML
    void validarRespuesta(ActionEvent event) {
        if (!juegoActivo) {
            return;
        }

        String respuesta = respuestaField.getText();

        if (respuesta.equals(palabraLabel.getText())) {
            temporizador.stop();

            nivel++;
            nivelLabel.setText("Nivel: " + nivel);

            if ((nivel - 1) % 5 == 0) {
                tiempoPorNivel = tiempoPorNivel - 2;

                if (tiempoPorNivel < 2) {
                    tiempoPorNivel = 2;
                }
            }

            seleccionarPalabra();
            estadoLabel.setText("¡Correcto!");
            respuestaField.clear();
            iniciarTemporizador();
        } else {
            estadoLabel.setText("Inténtalo de nuevo");
        }
    }



    private void seleccionarPalabra() {
        String palabraSeleccionada;

        if (nivel <= 5) {
            int posicion = (int) (Math.random() * palabrasFaciles.length);
            palabraSeleccionada = palabrasFaciles[posicion];
        } else if (nivel <= 10) {
            int posicion = (int) (Math.random() * palabrasIntermedias.length);
            palabraSeleccionada = palabrasIntermedias[posicion];
        } else {
            int posicion = (int) (Math.random() * palabrasDificiles.length);
            palabraSeleccionada = palabrasDificiles[posicion];
        }

        palabraLabel.setText(palabraSeleccionada);
    }


    @FXML
    void comenzarJuego() {
        if (temporizador != null) {
            temporizador.stop();
        }
        juegoActivo = true;
        seleccionarPalabra();
        estadoLabel.setText("¡Escribe la palabra!");
        respuestaField.clear();
        iniciarTemporizador();
        respuestaField.requestFocus();
    }


    private void iniciarTemporizador() {
        tiempoRestante = tiempoPorNivel;
        tiempoLabel.setText("Tiempo: " + tiempoRestante + "s");

        temporizador = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    ControlTemporizador control = new ControlTemporizador();
                    control.actualizarTiempo();

                    if (tiempoRestante <= 0) {
                        finalizarJuego();
                    }
                })
        );

        temporizador.setCycleCount(Timeline.INDEFINITE);
        temporizador.play();
    }


    private void finalizarJuego() {
        String respuesta = respuestaField.getText();

        if (respuesta.equals(palabraLabel.getText())) {
            temporizador.stop();
            juegoActivo = true;

            nivel++;
            nivelLabel.setText("Nivel: " + nivel);

            if ((nivel - 1) % 5 == 0) {
                tiempoPorNivel = tiempoPorNivel - 2;

                if (tiempoPorNivel < 2) {
                    tiempoPorNivel = 2;
                }
            }

            seleccionarPalabra();
            respuestaField.clear();
            estadoLabel.setText("¡Correcto! Nivel superado.");

            iniciarTemporizador();

        } else {
            juegoActivo = false;
            temporizador.stop();
            respuestaField.setDisable(true);
            estadoLabel.setText("¡Tiempo agotado! Fin del juego.");
            mostrarResultados();
        }
    }

    @FXML
    void reiniciarJuego() {
        if (temporizador != null) {
            temporizador.stop();
        }

        nivel = 1;
        tiempoPorNivel = 20;
        tiempoRestante = 20;

        nivelLabel.setText("Nivel: 1");
        tiempoLabel.setText("Tiempo: 20s");
        estadoLabel.setText("Pulsa COMENZAR para jugar");

        palabraLabel.setText("");
        respuestaField.clear();
        respuestaField.setDisable(false);
    }


    private void mostrarResultados() {
        try {
            FXMLLoader cargador = new FXMLLoader(
                    getClass().getResource("resultados-view.fxml")
            );

            Parent ventanaResultados = cargador.load();

            ResultadosController controlador = cargador.getController();
            controlador.mostrarDatos(nivel - 1, tiempoRestante);

            Scene escena = new Scene(ventanaResultados);
            Stage ventana = (Stage) nivelLabel.getScene().getWindow();

            ventana.setScene(escena);
            ventana.setTitle("Resultados - Escritura Rápida");
            ventana.show();

        } catch (IOException e) {
            System.out.println("Error al mostrar los resultados: " + e.getMessage());
        }
    }

}
