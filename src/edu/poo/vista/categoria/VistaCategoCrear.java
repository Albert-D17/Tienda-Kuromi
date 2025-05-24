package edu.poo.vista.categoria;

import edu.poo.controlador.categoria.ControladorCategoGrabar;
import edu.poo.controlador.varios.ControladorFormulario;
import edu.poo.controlador.varios.ControladorImagen;
import edu.poo.recurso.utilidad.Fondo;
import edu.poo.recurso.utilidad.Marco;
import edu.poo.recurso.utilidad.Mensaje;
import edu.poo.recurso.dominio.Configuracion;
import edu.poo.recurso.dominio.Ruta;

import java.io.File;

import javafx.geometry.Pos;
import javafx.scene.SubScene;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.text.FontWeight;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;

public class VistaCategoCrear extends SubScene {

    private final GridPane miGrilla;
    private final StackPane miFormualario;

    private TextField cajaNombre;
    private ComboBox<String> comboEstado;
    private TextField cajaImagenCategoria;
    private String rutaSeleccionadaCategoria;

    public VistaCategoCrear(double anchoFormulario, double altoFormulario) {
        super(new StackPane(), anchoFormulario, altoFormulario);

        Background fondo = Fondo.asignarAleatorio(Configuracion.FONDOS);

        miFormualario = (StackPane) getRoot();
        miFormualario.setBackground(fondo);
        miFormualario.setAlignment(Pos.TOP_CENTER);

        miGrilla = new GridPane();

        crearMarco();
        crearFormulario();
    }

    public StackPane getMiFormualario() {
        return miFormualario;
    }

    private void crearMarco() {
    Rectangle marco = Marco.crear(0, 0, Configuracion.DEGRADE_ARREGLO, 
             Configuracion.DEGRADE_BORDE);
    StackPane.setAlignment(marco, Pos.CENTER);
    marco.widthProperty().bind(miFormualario.widthProperty().multiply(Configuracion.MARCO_ANCHO_PORCENTAJE));
    marco.heightProperty().bind(miFormualario.heightProperty().multiply(Configuracion.MARCO_ALTO_PORCENTAJE));
    
    // Aquí agregamos transparencia
    marco.setOpacity(0.7);  // Puedes ajustar entre 0.0 y 1.0 según necesites

    miFormualario.getChildren().add(marco);
    }

    private FileChooser crearSelectorDeLaImagen() {
        File rutaInicial = new File(Ruta.RUTA_USUARIO);
        if (!rutaInicial.exists()) {
            rutaInicial = new File(Ruta.RUTA_PROYECTO);
        }
        FileChooser objSeleccionar = new FileChooser();
        objSeleccionar.setTitle("Seleccionar Imagen para Categoría");
        objSeleccionar.setInitialDirectory(rutaInicial);
        objSeleccionar.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpg", "*.jpeg")
        );
        return objSeleccionar;
    }

    private void crearFormulario() {
        miGrilla.prefWidthProperty().bind(miFormualario.widthProperty().multiply(0.8));
        miGrilla.maxWidthProperty().bind(miFormualario.widthProperty().multiply(0.8));
        miGrilla.setAlignment(Pos.CENTER);
        miGrilla.setHgap(10);
        miGrilla.setVgap(10);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(40);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(60);
        miGrilla.getColumnConstraints().addAll(col1, col2);

        Text titulo = new Text("Formulario Categorías");
        titulo.setFill(Color.web("#E6E6E6"));
        titulo.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
        miGrilla.add(titulo, 0, 0, 2, 1);

        Label lblNombreCatego = new Label("Nombre categoría:");
        miGrilla.add(lblNombreCatego, 0, 1);
        cajaNombre = new TextField();
        cajaNombre.setMaxWidth(Double.MAX_VALUE);
        ControladorFormulario.cantidadCaracteres(cajaNombre, 25);
        GridPane.setHgrow(cajaNombre, Priority.ALWAYS);
        miGrilla.add(cajaNombre, 1, 1);

        Label lblComboEstadoCargo = new Label("Estado categoría:");
        miGrilla.add(lblComboEstadoCargo, 0, 2);
        comboEstado = new ComboBox<>();
        comboEstado.setMaxWidth(Double.MAX_VALUE);
        comboEstado.getItems().addAll("Seleccione el estado", "Activo", "Inactivo");
        comboEstado.getSelectionModel().select(0);
        GridPane.setHgrow(comboEstado, Priority.ALWAYS);
        miGrilla.add(comboEstado, 1, 2);

        Label lblImagen = new Label("Imagen categoría:");
        miGrilla.add(lblImagen, 0, 3);

        cajaImagenCategoria = new TextField();
        cajaImagenCategoria.setDisable(true);
        cajaImagenCategoria.setPromptText("Nombre de la imagen (público)");

        FileChooser objSelectorImagen = crearSelectorDeLaImagen();

        Button btnAgregarImg = new Button("+");
        btnAgregarImg.setOnAction((ev) -> {
            rutaSeleccionadaCategoria = ControladorImagen.seleccionarImagen(cajaImagenCategoria, objSelectorImagen);
            if (rutaSeleccionadaCategoria.isEmpty()) {
                cajaImagenCategoria.setText("");
            }
        });
        
        

        HBox panelImagen = new HBox(5);
        HBox.setHgrow(cajaImagenCategoria, Priority.ALWAYS);
        panelImagen.getChildren().addAll(cajaImagenCategoria, btnAgregarImg);
        panelImagen.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(panelImagen, Priority.ALWAYS);
        miGrilla.add(panelImagen, 1, 3);

        Button btnGrabar = new Button("Crear Categoría");
        btnGrabar.setMaxWidth(Double.MAX_VALUE);
        btnGrabar.setTextFill(Color.web("#6C3483"));
        btnGrabar.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        GridPane.setHgrow(btnGrabar, Priority.ALWAYS);
        btnGrabar.setOnAction((e) -> {
            if (formularioDiligenciado()) {
                String nombre = cajaNombre.getText();
                boolean estado = comboEstado.getValue().equalsIgnoreCase("Activo");
                String nombrePublicoImg = cajaImagenCategoria.getText();

                if (ControladorCategoGrabar.grabar(nombre, estado, nombrePublicoImg, rutaSeleccionadaCategoria)) {
                    cajaNombre.setText("");
                    comboEstado.getSelectionModel().select(0);
                    cajaImagenCategoria.setText("");
                    rutaSeleccionadaCategoria = "";
                    cajaNombre.requestFocus();
                    Mensaje.modal(Alert.AlertType.INFORMATION, null, "ÉXITO", "Categoría grabada exitosamente.");
                } else {
                    Mensaje.modal(Alert.AlertType.ERROR, null, "ERROR", "No se pudo grabar la categoría.");
                }
            }
        });
        miGrilla.add(btnGrabar, 1, 4);

        miFormualario.getChildren().add(miGrilla);
    }

    private boolean formularioDiligenciado() {
        boolean correcto = false;
        String nombre = cajaNombre.getText();
        int indiceSeleccionado = comboEstado.getSelectionModel().getSelectedIndex();
        String nombrePublicoImagen = cajaImagenCategoria.getText();

        if (nombre.isBlank()) {
            cajaNombre.requestFocus();
            Mensaje.modal(Alert.AlertType.WARNING, null, "Advertencia", "El nombre es obligatorio.");
        } else if (indiceSeleccionado == 0) {
            comboEstado.requestFocus();
            Mensaje.modal(Alert.AlertType.WARNING, null, "Advertencia", "Debe seleccionar un estado.");
        } else if (nombrePublicoImagen.isBlank()) {
            Mensaje.modal(Alert.AlertType.WARNING, null, "Advertencia", "Debe seleccionar una imagen para la categoría.");
        } else {
            correcto = true;
        }
        return correcto;
    }
}
