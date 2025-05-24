package edu.poo.controlador.varios;

import edu.poo.recurso.utilidad.Icono;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class ControladorSalida {
    
    public static void verificar(Stage stage){
        Alert msg = new Alert(Alert.AlertType.CONFIRMATION);
        msg.setTitle("AMF");
        msg.initOwner(stage);
        msg.setHeaderText(null);
        msg.getDialogPane().setGraphic(Icono.obtenerIcono("iconByeBye.png",40));
        msg.setContentText("¿Desea cerrar la aplicacion?");
        
        if (msg.showAndWait().get() == ButtonType.OK){
            stage.close();
        }
    }

}
