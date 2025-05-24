package edu.poo.vista.producto;

import edu.poo.modelo.Categoria;
import edu.poo.modelo.Producto;
import edu.poo.recurso.dominio.Ruta;
import edu.poo.recurso.utilidad.Fondo;
import edu.poo.recurso.utilidad.Marco;
import edu.poo.recurso.utilidad.Mensaje;
import edu.poo.recurso.dominio.Configuracion;
import edu.poo.recurso.dominio.Contenedor;
import edu.poo.controlador.varios.ControladorImagen;
import edu.poo.controlador.varios.ControladorFormulario;
import edu.poo.controlador.categoria.ControladorCategoListar;
import edu.poo.controlador.producto.ControladorProductoEditar;
import edu.poo.controlador.producto.ControladorProductoVentana;

import java.io.File;
import java.util.List;

import javafx.geometry.Insets; // Importar Insets para el padding
import javafx.geometry.Pos;
import javafx.scene.SubScene;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import javafx.scene.control.Button;
import javafx.scene.text.FontWeight;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.image.Image; // Para el preview de la imagen
import javafx.scene.image.ImageView; // Para el preview de la imagen

public class VistaProductoEditar extends SubScene {

    private final GridPane miGrilla;
    private final StackPane miFormualario;

    // Eliminamos las variables fijas de anchoMarco y altoMarco
    // private final double anchoMarco;
    // private final double altoMarco;

    private String rutaSeleccionada;
    private ImageView imagenPreview; // Para mostrar la imagen actual/seleccionada

    private ComboBox<Categoria> comboCategoria;
    private TextField cajaImagen;

    private int posicion;
    private Producto objProducto;

    private Pane panelCuerpo;
    private BorderPane panelPrincipal;

    public VistaProductoEditar(BorderPane princ, Pane pane, double anchoFormulario, double altoFormulario,
            Producto objProductoExterno, int posicionArchivo) {
        super(new StackPane(), anchoFormulario, altoFormulario);

        Background fondo = Fondo.asignarAleatorio(Configuracion.FONDOS);

        miFormualario = (StackPane) getRoot();
        miFormualario.setBackground(fondo);
        miFormualario.setAlignment(Pos.CENTER); // Alineamos todo el contenido al centro

        // Las variables fijas de anchoMarco y altoMarco ya no son necesarias aquí.
        // anchoMarco = anchoFormulario - (anchoFormulario * 0.3);
        // altoMarco = altoFormulario - (altoFormulario * 0.2);

        miGrilla = new GridPane();
        miGrilla.setAlignment(Pos.CENTER);
        miGrilla.setHgap(10);
        miGrilla.setVgap(10);
        miGrilla.setPadding(new Insets(20)); // Añadimos padding al GridPane

        posicion = posicionArchivo;
        objProducto = objProductoExterno;

        panelPrincipal = princ;
        panelCuerpo = pane;

        crearMarco();
        crearFormulario();
        // Carga la imagen inicial después de que la interfaz esté lista
        actualizarPreviewImagen(Ruta.RUTA_FOTOS + Configuracion.SEPARADOR_CARPETA + objProducto.getNomImgPubProducto());
    }

    public StackPane getMiFormualario() {
        return miFormualario;
    }

    private void crearMarco() {
        Rectangle marco = Marco.crear(0, 0, // Valores iniciales no importan, se bindearán
                Configuracion.DEGRADE_ARREGLO, Configuracion.DEGRADE_BORDE);
        StackPane.setAlignment(marco, Pos.CENTER);

        // El marco se adaptará al tamaño del formulario principal con un porcentaje
        marco.widthProperty().bind(miFormualario.widthProperty().multiply(Configuracion.MARCO_ANCHO_PORCENTAJE));
        marco.heightProperty().bind(miFormualario.heightProperty().multiply(Configuracion.MARCO_ALTO_PORCENTAJE));

        // Añadimos transparencia al marco
        marco.setOpacity(0.7);

        miFormualario.getChildren().add(marco);
    }

    private void armarCombo() {
        int indice, indiceSeleccionado;
        indice = 0;
        indiceSeleccionado = 0;

        comboCategoria = new ComboBox<>();
        List<Categoria> arregloCategoria = ControladorCategoListar.arregloCategoria();
        Categoria primeraOpcion = new Categoria(0, "Selecciona categoría", true);
        comboCategoria.getItems().add(primeraOpcion);

        for (Categoria objCategoria : arregloCategoria) {
            indice++;
            comboCategoria.getItems().add(objCategoria);
            if (objCategoria.getCodCategoria() == objProducto.getCatProducto().getCodCategoria()) {
                indiceSeleccionado = indice;
            }
        }
        comboCategoria.getSelectionModel().select(indiceSeleccionado);
        comboCategoria.setConverter(new StringConverter<Categoria>() {
            @Override
            public String toString(Categoria obj) {
                return obj.getNombreCategoria();
            }

            @Override
            public Categoria fromString(String string) {
                return null;
            }
        });
        // Permitimos que el ComboBox se expanda
        comboCategoria.setMaxWidth(Double.MAX_VALUE);
    }

    private FileChooser crearSelectorDeLaImagen() {
        File rutaInicial = new File(Ruta.RUTA_USUARIO);
        if (!rutaInicial.exists()) {
            rutaInicial = new File(Ruta.RUTA_PROYECTO);
        }
        FileChooser objSeleccionar = new FileChooser();
        objSeleccionar.setTitle("Selecciona la imagen del producto");
        objSeleccionar.setInitialDirectory(rutaInicial);
        objSeleccionar.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes PNG", "*.png"),
                new FileChooser.ExtensionFilter("Fotos JPG", "*.jpg"));
        return objSeleccionar;
    }

    private void crearFormulario() {
        // Restricciones de columna para hacer la grilla responsiva
        ColumnConstraints col1 = new ColumnConstraints();
        col1.percentWidthProperty().bind(miGrilla.widthProperty().multiply(0.30)); // 30% del ancho del grid
        col1.setHgrow(Priority.ALWAYS); // Permite crecimiento horizontal
        ColumnConstraints col2 = new ColumnConstraints();
        col2.percentWidthProperty().bind(miGrilla.widthProperty().multiply(0.70)); // 70% del ancho del grid
        col2.setHgrow(Priority.ALWAYS); // Permite crecimiento horizontal
        miGrilla.getColumnConstraints().addAll(col1, col2);

        // Vinculamos las dimensiones del GridPane a las dimensiones del formulario SubScene
        miGrilla.maxWidthProperty().bind(miFormualario.widthProperty().multiply(Configuracion.MARCO_ANCHO_PORCENTAJE));
        miGrilla.maxHeightProperty().bind(miFormualario.heightProperty().multiply(Configuracion.MARCO_ALTO_PORCENTAJE));

        // Título
        Text titulo = new Text("Formulario Actualizar Producto");
        titulo.setFill(Color.web("#E6E6E6"));
        titulo.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
        GridPane.setHalignment(titulo, javafx.geometry.HPos.CENTER); // Centra el título en las columnas combinadas
        miGrilla.add(titulo, 0, 0, 2, 1);

        // Fila 1: Nombre Producto
        Label lblNombreProducto = new Label("Nombre Producto:");
        lblNombreProducto.setTextFill(Color.BLACK);
        miGrilla.add(lblNombreProducto, 0, 1);
        TextField cajaNombreProducto = new TextField();
        cajaNombreProducto.setText(objProducto.getNomProducto());
        cajaNombreProducto.setMaxWidth(Double.MAX_VALUE); // Permite que se expanda
        ControladorFormulario.cantidadCaracteres(cajaNombreProducto, 30);
        miGrilla.add(cajaNombreProducto, 1, 1);

        // Fila 2: Precio
        Label lblPrecioProducto = new Label("Precio:");
        lblPrecioProducto.setTextFill(Color.BLACK);
        miGrilla.add(lblPrecioProducto, 0, 2);
        TextField cajaPrecioProducto = new TextField();
        cajaPrecioProducto.setText(String.valueOf(objProducto.getPreProducto()));
        ControladorFormulario.cantidadCaracteres(cajaPrecioProducto, 12);
        ControladorFormulario.soloDecimales(cajaPrecioProducto);
        cajaPrecioProducto.setMaxWidth(Double.MAX_VALUE); // Permite que se expanda
        miGrilla.add(cajaPrecioProducto, 1, 2);

        // Fila 3: Cantidad
        Label lblCantidadProducto = new Label("Cantidad:");
        lblCantidadProducto.setTextFill(Color.BLACK);
        miGrilla.add(lblCantidadProducto, 0, 3);
        TextField cajaCantidadProducto = new TextField();
        cajaCantidadProducto.setText("" + objProducto.getCanProducto());
        ControladorFormulario.cantidadCaracteres(cajaCantidadProducto, 7);
        ControladorFormulario.soloNumeros(cajaCantidadProducto);
        cajaCantidadProducto.setMaxWidth(Double.MAX_VALUE); // Permite que se expanda
        miGrilla.add(cajaCantidadProducto, 1, 3);

        // Fila 4: Categoría
        Label lblCategoria = new Label("Categoría: ");
        lblCategoria.setTextFill(Color.BLACK);
        miGrilla.add(lblCategoria, 0, 4);
        armarCombo(); // Configura el ComboBox y le aplica setMaxWidth(Double.MAX_VALUE) internamente
        miGrilla.add(comboCategoria, 1, 4);

        // Fila 5: La imagen
        Label lblImagen = new Label("Imagen: ");
        lblImagen.setTextFill(Color.BLACK);
        miGrilla.add(lblImagen, 0, 5);

        rutaSeleccionada = "";
        cajaImagen = new TextField();
        cajaImagen.setText(objProducto.getNomImgPubProducto());
        cajaImagen.setDisable(true);
        cajaImagen.setMaxWidth(Double.MAX_VALUE); // Permite que se expanda

        FileChooser objSeleccionar = crearSelectorDeLaImagen();

        Button btnAgregarImg = new Button("+"); // Cambiamos el texto del botón a algo más descriptivo
        btnAgregarImg.setOnAction((e) -> {
            rutaSeleccionada = ControladorImagen.seleccionarImagen(cajaImagen, objSeleccionar);
            if (rutaSeleccionada.isEmpty()) {
                Mensaje.modal(Alert.AlertType.WARNING, null,
                        "Advertencia", "No se seleccionó una imagen.");
            } else {
                actualizarPreviewImagen(rutaSeleccionada);
            }
        });
        
        HBox panelHorizontal = new HBox(5); // Espacio de 5 entre TextField y Button
        panelHorizontal.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(cajaImagen, Priority.ALWAYS); // Permite que el TextField crezca
        panelHorizontal.getChildren().addAll(cajaImagen, btnAgregarImg);
        miGrilla.add(panelHorizontal, 1, 5);

        // Fila para el preview de la imagen
        imagenPreview = new ImageView();
        imagenPreview.setFitWidth(100);
        imagenPreview.setFitHeight(100);
        imagenPreview.setPreserveRatio(true);
        imagenPreview.setSmooth(true);
        GridPane.setHalignment(imagenPreview, javafx.geometry.HPos.CENTER); // Centra la imagen en la grilla
        miGrilla.add(imagenPreview, 0, 6, 2, 1); // Ocupa ambas columnas

        // Fila 7: Botón Actualizar
        Button btnEditar = new Button("Actualizar Producto");
        btnEditar.setMaxWidth(Double.MAX_VALUE); // Permite que se expanda
        btnEditar.setTextFill(Color.web("#6C3483"));
        btnEditar.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        GridPane.setHalignment(btnEditar, javafx.geometry.HPos.RIGHT); // Alineamos a la derecha
        miGrilla.add(btnEditar, 1, 7);

        btnEditar.setOnAction(e -> {
            if (formularioDiligenciado(cajaNombreProducto, cajaPrecioProducto, cajaCantidadProducto, comboCategoria)) {
                String nombre = cajaNombreProducto.getText();
                double precio = Double.parseDouble(cajaPrecioProducto.getText());
                int cantidad = Integer.parseInt(cajaCantidadProducto.getText());

                Categoria cate = comboCategoria.getSelectionModel().getSelectedItem();
                String nomImag = cajaImagen.getText();

                int codi = objProducto.getCodProducto();
                String nocu = objProducto.getNomImgOcuProducto(); // Conservamos el nombre oculto

                Producto objProductoEditado = new Producto(codi, nombre, precio, cantidad, cate, nomImag, nocu);

                if (ControladorProductoEditar.actualizar(posicion, objProductoEditado, rutaSeleccionada)) {
                    Mensaje.modal(Alert.AlertType.INFORMATION, null, "ÉXITO", "Producto editado exitosamente");
                    // Opcional: regresar a la vista de administración después de la edición
                    panelCuerpo = ControladorProductoVentana.administrar(panelPrincipal, panelCuerpo, Configuracion.ANCHO_APP,
                            Contenedor.ALTO_CUERPO.getValor());
                    panelPrincipal.setCenter(null);
                    panelPrincipal.setCenter(panelCuerpo);
                } else {
                    Mensaje.modal(Alert.AlertType.ERROR, null, "ERROR", "No se pudo actualizar el producto");
                }
            }
        });

        // Fila 8: Botón Regresar
        Button btnRegresar = new Button("Regresar");
        btnRegresar.setMaxWidth(Double.MAX_VALUE); // Permite que se expanda
        btnRegresar.setTextFill(Color.web("#6C3483"));
        btnRegresar.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));
        GridPane.setHalignment(btnRegresar, javafx.geometry.HPos.RIGHT); // Alineamos a la derecha
        miGrilla.add(btnRegresar, 1, 8); // Ajustamos la fila

        btnRegresar.setOnAction(e -> {
            panelCuerpo = ControladorProductoVentana.administrar(panelPrincipal, panelCuerpo, Configuracion.ANCHO_APP,
                    Contenedor.ALTO_CUERPO.getValor());

            panelPrincipal.setCenter(null);
            panelPrincipal.setCenter(panelCuerpo);
        });

        miFormualario.getChildren().add(miGrilla);
    }

    private void actualizarPreviewImagen(String rutaImagen) {
        File archivoImagen = new File(rutaImagen);
        if (archivoImagen.exists()) {
            imagenPreview.setImage(new Image(archivoImagen.toURI().toString()));
        } else {
            imagenPreview.setImage(null);
            // Opcional: Cargar una imagen placeholder si la original no se encuentra
            // try {
            // imagenPreview.setImage(new Image(getClass().getResourceAsStream("/ruta/a/placeholder.png")));
            // } catch (Exception e) {
            // System.err.println("No se pudo cargar la imagen placeholder: " + e.getMessage());
            // }
        }
    }

    private boolean formularioDiligenciado(TextField cajaNombreProducto, TextField cajaPrecioProducto,
            TextField cajaCantidadProducto, ComboBox comboCategoria) {
        boolean correcto = false;

        String nombre = cajaNombreProducto.getText();
        String precioTexto = cajaPrecioProducto.getText();
        String cantidadTexto = cajaCantidadProducto.getText();
        int indiceSeleccionado = comboCategoria.getSelectionModel().getSelectedIndex();

        if (nombre.isBlank()) {
            cajaNombreProducto.requestFocus();
            Mensaje.modal(Alert.AlertType.WARNING, null, "Advertencia", "El nombre del producto es obligatorio.");
        } else if (precioTexto.isBlank()) {
            cajaPrecioProducto.requestFocus();
            Mensaje.modal(Alert.AlertType.WARNING, null, "Advertencia", "El precio es obligatorio.");
        } else if (cantidadTexto.isBlank()) {
            cajaCantidadProducto.requestFocus();
            Mensaje.modal(Alert.AlertType.WARNING, null, "Advertencia", "La cantidad es obligatoria.");
        } else if (indiceSeleccionado == 0) {
            comboCategoria.requestFocus();
            Mensaje.modal(Alert.AlertType.WARNING, null, "Advertencia", "Debe seleccionar una categoría.");
        } else {
            correcto = true;
        }
        return correcto;
    }
}