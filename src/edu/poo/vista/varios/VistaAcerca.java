package edu.poo.vista.varios;


import edu.poo.recurso.dominio.Ruta;
import edu.poo.recurso.dominio.Configuracion;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class VistaAcerca {

    public static final String LBL_TEXTO = "#2E2E2E";
    public static final String ACERCA_FOTO = "developer.jpg";
    public static final String ACERCA_NOMBRE = "Albert D. Peña";
    public static final String ACERCA_CODIGO = "2020214013";
    public static final String ACERCA_CORREO = "adpena@unimagdalena.edu.co";

    public static void mostrar(double anchoPanel, double altoPanel) {
        Stage nuevoEscenario = new Stage();
        String ruta = Ruta.RUTA_IMAGENES + ACERCA_FOTO;

        VBox miPanel = new VBox(6);
        miPanel.setAlignment(Pos.CENTER);
        miPanel.setPadding(new Insets(10, 10, 10, 10));
        miPanel.setStyle(Configuracion.CABECERA_COLOR_FONDO);

        Image imagen = new Image(ruta);
        ImageView imageView = new ImageView(imagen);
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        Font fuente = Font.font("Verdana", FontWeight.BOLD, 14);
        Label lblNombre = new Label(ACERCA_NOMBRE);
        Label lblCodigo = new Label(ACERCA_CODIGO);
        Label lblCorreo = new Label(ACERCA_CORREO);
        lblCorreo.setTextFill(Color.web(LBL_TEXTO));
        lblNombre.setTextFill(Color.web(LBL_TEXTO));
        lblCodigo.setTextFill(Color.web(LBL_TEXTO));
        lblCorreo.setFont(fuente);
        lblNombre.setFont(fuente);
        lblCodigo.setFont(fuente);

        Button btnCerrar = new Button("Aceptar");
        btnCerrar.setPrefWidth(160);
        btnCerrar.setTextFill(Color.web("#6C3483"));
        btnCerrar.setOnAction(event -> nuevoEscenario.close());

        miPanel.getChildren().addAll(imageView,lblNombre,lblCodigo,lblCorreo, btnCerrar);

        Scene nuevaEscena = new Scene(miPanel, anchoPanel, altoPanel);
        nuevoEscenario.setScene(nuevaEscena);
        nuevoEscenario.initStyle(StageStyle.UTILITY);
        nuevoEscenario.initModality(Modality.APPLICATION_MODAL);
        nuevoEscenario.setTitle("Acerca del desarrollador");
        nuevoEscenario.show();
    }
}
