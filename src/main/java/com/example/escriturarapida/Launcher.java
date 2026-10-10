package com.example.escriturarapida;

import javafx.application.Application;
/**
 * Clase de arranque del programa.
 *
 * <p>Existe como punto de entrada separado porque la clase principal de una
 * aplicación JavaFX (que extiende {@link Application}) puede dar errores de
 * "faltan componentes de JavaFX" al ejecutarse desde un {@code .jar}. Al
 * tener un {@code main} en una clase que no extiende {@code Application}, se
 * evita ese problema.</p>
 */
public class Launcher {
    /**
     * Método principal. Lanza la aplicación JavaFX {@link HelloApplication}.
     *
     * @param args argumentos de línea de comandos (se pasan a JavaFX)
     */
    public static void main(String[] args) {
        Application.launch(HelloApplication.class, args);
    }
}
