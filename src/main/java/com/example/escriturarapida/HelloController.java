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
/**
 * Controlador de la pantalla principal del juego "Escritura Rápida"
 * (vista {@code hello-view.fxml}).
 *
 * <p>Concentra toda la lógica del juego:</p>
 * <ul>
 *   <li>Selección de palabras o frases según el nivel.</li>
 *   <li>Validación de lo que escribe el jugador.</li>
 *   <li>Control del temporizador de cada ronda.</li>
 *   <li>Progresión de niveles y reducción del tiempo disponible.</li>
 *   <li>Cambio a la pantalla de resultados cuando termina la partida.</li>
 * </ul>
 *
 * <p>Reglas básicas: el jugador debe escribir exactamente el texto mostrado
 * antes de que se agote el tiempo. Cada acierto sube un nivel; cada 5 niveles
 * el tiempo por ronda disminuye 2 segundos (mínimo 2 segundos).</p>
 */

public class HelloController {
    /** Palabras cortas usadas en los niveles 1 a 5. */

    private String[] palabrasFaciles = {
            "gas",
            "sal",
            "faldas",
            "allá",
            "hadas",
            "Saldo",
            "Sallas",
    };
    /** Palabras de dificultad media usadas en los niveles 6 a 10. */

    private String[] palabrasIntermedias = {
            "Viento",
            "fuego",
            "permanente",
            "Crepúsculo",
            "atmósfera",
    };
    /** Frases completas usadas desde el nivel 11 en adelante. */

    private String[] palabrasDificiles = {
        "Mañana será un gran día",
        "La practica hace al maestro.",
        "Lee cuidadosamente la frase antes de comenzar a escribirla.",
        "La tecnología avanza a un ritmo frenético.",
        "Quizás el éxito requiera más constancia que talento.",
        "Cada nivel superado demuestra que tu velocidad y precision mejoran.",
    };

    /** Nivel actual del jugador (empieza en 1). */
    private int nivel=1;

    /** Segundos que le quedan al jugador en la ronda en curso. */
    private int tiempoRestante = 20;

    /**
     * Segundos asignados a cada ronda. Comienza en 20 y disminuye 2 segundos
     * cada 5 niveles, sin bajar de 2.
     */
    private int tiempoPorNivel = 20;

    /** Línea de tiempo de JavaFX que dispara un evento cada segundo. */
    private Timeline temporizador;

    /**
     * Indica si hay una partida en curso. Mientras sea {@code false},
     * {@link #validarRespuesta(ActionEvent)} ignora lo que se escriba.
     */
    private boolean juegoActivo = false;

    /**
     * Clase interna que encapsula la actualización del reloj de cada ronda.
     * Al ser interna, accede directamente a {@code tiempoRestante} y a
     * {@code tiempoLabel} del controlador.
     */
    private class ControlTemporizador {
        /**
         * Resta un segundo a {@code tiempoRestante} y refresca la etiqueta
         * de tiempo en pantalla.
         */
        public void actualizarTiempo() {
            tiempoRestante--;
            tiempoLabel.setText("Tiempo: " + tiempoRestante + "s");
        }
    }

    /** Etiqueta de mensajes de estado (acierto, error, fin del juego...). */
    @FXML
    private Label estadoLabel;

    /** Etiqueta que muestra el nivel actual. */
    @FXML
    private Label nivelLabel;

    /** Etiqueta que muestra la palabra o frase que el jugador debe escribir. */
    @FXML
    private Label palabraLabel;

    /** Campo de texto donde el jugador escribe su respuesta. */
    @FXML
    private TextField respuestaField;

    /** Etiqueta que muestra la cuenta regresiva en segundos. */
    @FXML
    private Label tiempoLabel;

    /**
     * Valida la respuesta del jugador. Se ejecuta al presionar ENTER en el
     * campo de texto ({@code onAction="#validarRespuesta"}).
     *
     * <p>Si no hay partida activa no hace nada. La comparación es exacta
     * ({@code equals}): distingue mayúsculas, tildes, signos de puntuación
     * y espacios.</p>
     *
     * <ul>
     *   <li><b>Acierto:</b> detiene el temporizador, sube de nivel, reduce el
     *       tiempo por ronda cada 5 niveles (al llegar a los niveles 6, 11,
     *       16...), elige una nueva palabra, limpia el campo y reinicia el
     *       temporizador.</li>
     *   <li><b>Error:</b> solo muestra "Inténtalo de nuevo"; no hay penalización
     *       y el texto escrito se conserva.</li>
     * </ul>
     *
     * @param event evento de acción generado por el campo de texto
     */

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

    /**
     * Elige al azar una palabra o frase según el nivel actual y la muestra
     * en {@code palabraLabel}.
     *
     * <ul>
     *   <li>Niveles 1 a 5: {@code palabrasFaciles}.</li>
     *   <li>Niveles 6 a 10: {@code palabrasIntermedias}.</li>
     *   <li>Nivel 11 o más: {@code palabrasDificiles}.</li>
     * </ul>
     *
     * La selección es completamente aleatoria, por lo que una misma palabra
     * puede repetirse en rondas consecutivas.
     */

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

    /**
     * Inicia la partida. Se ejecuta al pulsar el botón COMENZAR.
     *
     * <p>Detiene cualquier temporizador previo, marca el juego como activo,
     * muestra la primera palabra, limpia el campo de respuesta, arranca el
     * temporizador y coloca el cursor en el campo de texto. No reinicia el
     * nivel; para eso existe {@link #reiniciarJuego()}.</p>
     */
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

    /**
     * Arranca la cuenta regresiva de una ronda.
     *
     * <p>Asigna {@code tiempoRestante = tiempoPorNivel} y crea un
     * {@link Timeline} con un {@link KeyFrame} de 1 segundo que se repite
     * indefinidamente. En cada tick se resta un segundo mediante
     * {@link ControlTemporizador#actualizarTiempo()} y, cuando el tiempo
     * llega a 0 (o menos), se llama a {@link #finalizarJuego()}.</p>
     */
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

    /**
     * Se invoca cuando el tiempo de la ronda se agota.
     *
     * <ul>
     *   <li>Si el texto que hay en el campo coincide con la palabra mostrada,
     *       se considera un acierto aunque no se haya presionado ENTER: sube
     *       el nivel, se ajusta el tiempo por ronda y comienza una nueva
     *       ronda.</li>
     *   <li>Si no coincide, la partida termina: se desactiva el juego, se
     *       detiene el temporizador, se bloquea el campo de texto y se
     *       muestra la pantalla de resultados.</li>
     * </ul>
     */
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

    /**
     * Devuelve el juego a su estado inicial. Se ejecuta al pulsar el botón
     * REINICIAR.
     *
     * <p>Detiene el temporizador, restablece nivel (1), tiempo por nivel (20)
     * y tiempo restante (20), actualiza las etiquetas, vacía la palabra
     * mostrada y vuelve a habilitar el campo de texto. Después hay que
     * pulsar COMENZAR para jugar de nuevo.</p>
     */
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

    /**
     * Cambia a la pantalla de resultados ({@code resultados-view.fxml}).
     *
     * <p>Carga la vista con {@link FXMLLoader}, obtiene su
     * {@link ResultadosController} y le envía los niveles superados
     * ({@code nivel - 1}, porque el nivel actual no se logró completar) y el
     * tiempo restante. Luego reemplaza la escena de la ventana actual por la
     * de resultados.</p>
     *
     * <p>Si el archivo FXML no puede cargarse, el error se imprime en la
     * consola.</p>
     */
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
