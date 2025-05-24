package edu.uni.vista.categoria;

import edu.uni.recurso.dominio.Ruta;
import edu.uni.recurso.utilidad.Marco;
import edu.uni.recurso.utilidad.Fondo;
import edu.uni.recurso.utilidad.Mensaje;
import edu.uni.recurso.dominio.Configuracion;
import edu.uni.controlador.varios.ControladorFormulario;
import edu.uni.controlador.categoria.ControladorCategoGrabar;
import edu.uni.controlador.categoria.ControladorCategoImagen;
import edu.uni.recurso.utilidad.Ajustar;

import java.io.File;
import javafx.geometry.HPos;

import javafx.geometry.Pos;
import javafx.scene.SubScene;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.text.FontWeight;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Background;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;

public class VistaCategoCrear extends SubScene {

    private final GridPane miGrilla;
    private final StackPane miFormulario;

    private TextField cajaNombre, cajaImagen;
    private ComboBox<String> comboEstado;
    private static ImageView imagPrevi;

    private final double anchoMarco;
    private final double altoMarco;

    private String rutaSeleccionada;

    public VistaCategoCrear(double anchoFormulario, double altoFormulario) {
        super(new StackPane(), anchoFormulario, altoFormulario);

        Background fondo = Fondo.asignarAleatorio(Configuracion.FONDOS);

        miFormulario = (StackPane) getRoot();
        miFormulario.setBackground(fondo);
        miFormulario.setAlignment(Pos.TOP_CENTER);

        anchoMarco = anchoFormulario - (anchoFormulario * 0.3);
        altoMarco = altoFormulario - (altoFormulario * 0.2);
        miGrilla = new GridPane();

        crearMarco();
        crearFormulario();
    }

    public StackPane getMiFormulario() {
        return miFormulario;
    }

    private void crearMarco() {

        Rectangle marco = Marco.crear(anchoMarco, altoMarco,
                Configuracion.DEGRADE_ARREGLO, Configuracion.DEGRADE_BORDE);
        StackPane.setAlignment(marco, Pos.CENTER);
        Marco.ajustar(marco, miFormulario, 0.85, 0.90);
        miFormulario.getChildren().add(marco);
    }

    private FileChooser crearSelectorImagen() {

        File rutaInicial = new File(Ruta.RUTA_USUARIO);

        if (!rutaInicial.exists()) {
            rutaInicial = new File(Ruta.RUTA_PROYECTO);
        }

        FileChooser objSeleccionar = new FileChooser();
        objSeleccionar.setTitle("Agarra la imagen");
        objSeleccionar.setInitialDirectory(rutaInicial);
        objSeleccionar.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("imagenes", "*.png"),
                new FileChooser.ExtensionFilter("fotos", "*.jpg")
        );

        return objSeleccionar;
    }

    private void crearFormulario() {

        miGrilla.setAlignment(Pos.CENTER);
        miGrilla.setHgap(10);
        miGrilla.setVgap(10);

        // para que se ajuste segun el tamaño de la pantalla
        miGrilla.prefWidthProperty().bind(miFormulario.widthProperty().multiply(0.8));
        miGrilla.maxWidthProperty().bind(miFormulario.widthProperty().multiply(0.8));
        miGrilla.prefHeightProperty().bind(miFormulario.heightProperty().multiply(0.9));
        miGrilla.maxHeightProperty().bind(miFormulario.heightProperty().multiply(0.9));

        ColumnConstraints columna1 = new ColumnConstraints();
        columna1.setPercentWidth(40);
        columna1.setHgrow(Priority.SOMETIMES);// que las columnas tambien se ajusten 

        ColumnConstraints columna2 = new ColumnConstraints();
        columna2.setPercentWidth(60);
        columna2.setHgrow(Priority.SOMETIMES);

        miGrilla.getColumnConstraints().add(columna1);
        miGrilla.getColumnConstraints().add(columna2);

        // Titulo de la grilla 
        Text titulo = new Text("Formulario Categorías");
        titulo.setFill(Color.web("#e33067"));
        titulo.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
        GridPane.setHalignment(titulo, HPos.CENTER);
        // Recordar -> miGrilla.add(objeto, columna, fila, coolSpan, rowSpan);
        miGrilla.add(titulo, 0, 0, 2, 1);

        // Fila 2: Nombre
        Label lblNombreCatego = new Label("Nombre Categoría: ");
        lblNombreCatego.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 15));
        miGrilla.add(lblNombreCatego, 0, 2);
        cajaNombre = new TextField();
        ControladorFormulario.cantidadCaracteres(cajaNombre, 30);
        Ajustar.ajustarCampoDeTexto(cajaNombre);
        miGrilla.add(cajaNombre, 1, 2);

        // Fila 3: Estado
        Label lblComboEstadoCargo = new Label("Estado categoría: ");
        lblComboEstadoCargo.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 15));
        miGrilla.add(lblComboEstadoCargo, 0, 3);
        comboEstado = new ComboBox<>();
        Ajustar.ajustarCombo(comboEstado);
        comboEstado.getItems().addAll("Seleccione el estado", "Activo", "Inactivo");
        comboEstado.getSelectionModel().select(0);
        miGrilla.add(comboEstado, 1, 3);

        // Fila 4: Imagen
        Label lblImagen = new Label("Imagen: ");
        lblImagen.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 15));
        miGrilla.add(lblImagen, 0, 4);
        cajaImagen = new TextField();
        cajaImagen.setDisable(true);
        cajaImagen.setMaxWidth(Double.MAX_VALUE);

        FileChooser objSeleccionar = crearSelectorImagen();

        Button btnAgregarImg = new Button("+");
        btnAgregarImg.setMaxWidth(Double.MAX_VALUE);
        btnAgregarImg.setOnAction((e) -> {
            rutaSeleccionada = ControladorCategoImagen.seleccionarImagen(cajaImagen, objSeleccionar);
            if (rutaSeleccionada.isEmpty()) {
                Mensaje.modal(Alert.AlertType.WARNING, null,
                        "Advertencia", "Imagen perdida");
                muestraImagenPredeterminada();
            }
            vistaPreviaImagen(rutaSeleccionada);

        });

        HBox.setHgrow(cajaImagen, Priority.ALWAYS);
        HBox panelHorizontal = new HBox(2);
        panelHorizontal.setAlignment(Pos.BOTTOM_RIGHT);
        panelHorizontal.getChildren().addAll(cajaImagen, btnAgregarImg);
        Ajustar.ajustarHBox(panelHorizontal);// el hgrow se le pone al panel y no a los bottones
        miGrilla.add(panelHorizontal, 1, 4);

        // Fila 5: Imagen prev
        muestraImagenPredeterminada();
        imagPrevi.setFitWidth(150);
        imagPrevi.setFitHeight(150);
        imagPrevi.setPreserveRatio(true);
        GridPane.setHalignment(imagPrevi, HPos.CENTER);
        // Ponerla en un contenedor permite que ajuste su tamaño 
        StackPane contImg = new StackPane(imagPrevi);
        Ajustar.ajustarPanel(contImg);
        contImg.setAlignment(Pos.CENTER);
        miGrilla.add(contImg, 1, 5);

        // Fila 6: grabar
        Button btnGrabar = new Button("Crear categoría");
        Ajustar.ajustarBoton(btnGrabar);
        btnGrabar.setTextFill(Color.web("6C3483"));
        btnGrabar.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));

        btnGrabar.setOnAction((e) -> {
            if (formularoDiligenciado()) {
                System.out.println("listo");

                boolean activoOInactivo;

                if (comboEstado.getValue() == "Activo") {
                    activoOInactivo = true;
                } else {
                    activoOInactivo = false;
                }

                if (ControladorCategoGrabar.grabarCategoria(cajaNombre.getText(), activoOInactivo,
                        cajaImagen.getText(), rutaSeleccionada)) {

                    cajaNombre.setText("");
                    comboEstado.setValue(null);
                    cajaNombre.requestFocus();
                    Mensaje.modal(Alert.AlertType.INFORMATION, null, "Grabado", "Grabado con exito");
                } else {
                    Mensaje.modal(Alert.AlertType.ERROR, null, "Error", "Error al grabar");
                }

                limpiarFormulario();
            }
        });

        miGrilla.add(btnGrabar, 1, 6);

        miFormulario.getChildren().add(miGrilla);
    }

    private void limpiarFormulario() {
        cajaNombre.setText("");
        comboEstado.getSelectionModel().select(0);
        cajaImagen.setText("");
        rutaSeleccionada = "";
        muestraImagenPredeterminada();
        cajaNombre.requestFocus();
    }

    private boolean formularoDiligenciado() {

        boolean correcto = false;
        String nombre = cajaNombre.getText();
        String imagen = cajaImagen.getText();
        int indiceSeleccionado = comboEstado.getSelectionModel().getSelectedIndex();

        if (nombre.isBlank()) {
            cajaNombre.requestFocus();
            Mensaje.modal(Alert.AlertType.WARNING, null,
                    "Advertencia", "El nombre es obligatorio");

        } else {
            if (indiceSeleccionado == 0) {
                Mensaje.modal(Alert.AlertType.WARNING, null,
                        "Advertencia", "Debe seleccionar un estado");

            } else {
                if (imagen.isBlank()) {
                    cajaImagen.requestFocus();
                    Mensaje.modal(Alert.AlertType.WARNING, null,
                            "Advertencia", "Debe agregar una imagen");

                } else {
                    correcto = true;
                }
            }
        }
        return correcto;
    }

    private void muestraImagenPredeterminada() {

        String rutaImg = getClass().getResource(Ruta.RUTA_IMAGENES + "imgNoDisponible.png").toExternalForm();
        Image imgPrev = new Image(rutaImg);
        if (imagPrevi == null) {
            imagPrevi = new ImageView(imgPrev);
        }else{
            imagPrevi.setImage(imgPrev);
        }
    }

    private void vistaPreviaImagen(String ruta) {

        try {
            File rutaImg = new File(ruta);

            if (rutaImg.exists()) {

                Image img = new Image(rutaImg.toURI().toString());
                imagPrevi.setImage(img);

            } else {
                muestraImagenPredeterminada();
            }
        } catch (Exception e) {
            muestraImagenPredeterminada();
        }
    }
}
