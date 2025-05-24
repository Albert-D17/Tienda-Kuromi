package edu.poo.recurso.utilidad;

import javafx.stage.Window;
import javafx.scene.control.Alert;

public class Mensaje {

    public static void modal(Alert.AlertType tipo, Window ventanaPadre, String titulo, String texto) {
        Alert msg = new Alert(tipo);
        msg.setTitle(titulo);
        msg.setHeaderText(null);
        msg.setContentText(texto);
        msg.initOwner(ventanaPadre);
        msg.show();
    }
}
