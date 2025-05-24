package edu.poo.controlador.varios;

import javafx.scene.control.TextField;

public class ControladorFormulario {

    public static void cantidadCaracteres(TextField caja, int limite) {
        // vo = Valor Observable
        caja.textProperty().addListener((vo) -> {
            if (caja.getText().length() > limite) {
                String cadena = caja.getText().substring(0, limite);
                caja.setText(cadena);
            }

        });
    }

    public static void soloNumeros(TextField caja) {
        caja.textProperty().addListener((ov, anterior, nuevo) -> {
            try {
                Integer.valueOf(nuevo);
            } catch (NumberFormatException e) {
                if (caja.getText().length() > 0) {
                    caja.setText(
                            caja.getText().substring(0, caja.getText().length() - 1));
                }
            }
        });
    }

    public static void soloDecimales(TextField caja) {
        caja.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d{0,2})?")) {
                caja.setText(oldValue);
            }
        });
    }

}
