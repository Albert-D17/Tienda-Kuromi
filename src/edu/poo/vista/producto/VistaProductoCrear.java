package edu.poo.vista.producto;

import edu.poo.modelo.Categoria;
import edu.poo.recurso.dominio.Ruta;
import edu.poo.recurso.utilidad.Fondo;
import edu.poo.recurso.utilidad.Marco;
import edu.poo.recurso.utilidad.Mensaje;
import edu.poo.recurso.dominio.Configuracion;
import edu.poo.controlador.varios.ControladorImagen;
import edu.poo.controlador.varios.ControladorFormulario;
import edu.poo.controlador.categoria.ControladorCategoListar;
import edu.poo.controlador.producto.ControladorProductoGrabar;

import java.io.File;
import java.util.List;

import javafx.geometry.Pos;
import javafx.geometry.Insets; // Importar Insets para el padding
import javafx.scene.SubScene;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.layout.HBox;
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
import javafx.scene.layout.ColumnConstraints;

public class VistaProductoCrear extends SubScene {

    private final GridPane miGrilla;
    private final StackPane miFormualario;

    // Eliminamos las variables fijas de anchoMarco y altoMarco, ya no son necesarias
    // private final double anchoMarco;
    // private final double altoMarco;

    private String rutaSeleccionada;

    private ComboBox<Categoria> comboCategoria;
    private TextField cajaImagen;

    public VistaProductoCrear(double anchoFormulario, double altoFormulario) {
        super(new StackPane(), anchoFormulario, altoFormulario);

        Background fondo = Fondo.asignarAleatorio(Configuracion.FONDOS);

        miFormualario = (StackPane) getRoot();
        miFormualario.setBackground(fondo);
        miFormualario.setAlignment(Pos.CENTER); // Centrar el formulario completo

        miGrilla = new GridPane();

        crearMarco();
        crearFormulario();
    }

    public StackPane getMiFormualario() {
        return miFormualario;
    }

    private void crearMarco() {
        Rectangle marco = Marco.crear(0, 0, Configuracion.DEGRADE_ARREGLO, Configuracion.DEGRADE_BORDE); // Valores iniciales no importan, se bindearán
        StackPane.setAlignment(marco, Pos.CENTER);
        
        // El marco se adaptará al tamaño del formulario principal con un porcentaje
        marco.widthProperty().bind(miFormualario.widthProperty().multiply(Configuracion.MARCO_ANCHO_PORCENTAJE));
        marco.heightProperty().bind(miFormualario.heightProperty().multiply(Configuracion.MARCO_ALTO_PORCENTAJE));
        
        // Añadimos transparencia al marco
        marco.setOpacity(0.7);

        miFormualario.getChildren().add(marco);
    }

    private void armarCombo() {
        comboCategoria = new ComboBox<>();
        List<Categoria> arregloCategoria = ControladorCategoListar.cargarDatos();
        Categoria primeraOpcion = new Categoria(0, "Selecciona categoría", true); // Texto más amigable
        comboCategoria.getItems().add(primeraOpcion);
        
        // Solo agregar categorías activas al combo box
        for (Categoria objCategoria : arregloCategoria) {
            if (objCategoria.isEstadoCategoria()) {
                comboCategoria.getItems().add(objCategoria);
            }
        }
        comboCategoria.getSelectionModel().select(0);
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
    }

    private FileChooser crearSelectorDeLaImagen() {
        File rutaInicial = new File(Ruta.RUTA_USUARIO);
        if (!rutaInicial.exists()) {
            rutaInicial = new File(Ruta.RUTA_PROYECTO);
        }
        FileChooser objSeleccionar = new FileChooser();
        objSeleccionar.setTitle("Seleccionar imagen de producto");
        objSeleccionar.setInitialDirectory(rutaInicial);
        objSeleccionar.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes PNG", "*.png"),
                new FileChooser.ExtensionFilter("Fotos JPG", "*.jpg"));
        return objSeleccionar;
    }

    private void crearFormulario() {
        miGrilla.setAlignment(Pos.CENTER);
        miGrilla.setHgap(10);
        miGrilla.setVgap(10);
        miGrilla.setPadding(new Insets(20)); // Añade padding para separar del marco

        // La grilla se adaptará al tamaño del formulario principal con un porcentaje
        miGrilla.prefWidthProperty().bind(miFormualario.widthProperty().multiply(Configuracion.MARCO_ANCHO_PORCENTAJE));
        miGrilla.maxWidthProperty().bind(miFormualario.widthProperty().multiply(Configuracion.MARCO_ANCHO_PORCENTAJE));

        // Columnas responsivas con porcentajes
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(35); // 35% para la primera columna (etiquetas)
        col1.setHgrow(Priority.ALWAYS);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(65); // 65% para la segunda columna (campos de entrada)
        col2.setHgrow(Priority.ALWAYS);

        miGrilla.getColumnConstraints().addAll(col1, col2);

        // Título
        Text titulo = new Text("Formulario Productos");
        titulo.setFill(Color.web("#E6E6E6"));
        titulo.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
        GridPane.setHalignment(titulo, javafx.geometry.HPos.CENTER); // Centrar el título en la grilla
        miGrilla.add(titulo, 0, 0, 2, 1);

        // Fila 1: Nombre Producto
        Label lblNombreProducto = new Label("Nombre Producto:");
        miGrilla.add(lblNombreProducto, 0, 1);
        TextField cajaNombreProducto = new TextField();
        cajaNombreProducto.setMaxWidth(Double.MAX_VALUE); // Permite que el TextField se expanda
        ControladorFormulario.cantidadCaracteres(cajaNombreProducto, 30);
        miGrilla.add(cajaNombreProducto, 1, 1);

        // Fila 2: Precio
        Label lblPrecioProducto = new Label("Precio:");
        miGrilla.add(lblPrecioProducto, 0, 2);
        TextField cajaPrecioProducto = new TextField();
        ControladorFormulario.cantidadCaracteres(cajaPrecioProducto, 12);
        ControladorFormulario.soloDecimales(cajaPrecioProducto);
        cajaPrecioProducto.setMaxWidth(Double.MAX_VALUE); // Permite que el TextField se expanda
        miGrilla.add(cajaPrecioProducto, 1, 2);

        // Fila 3: Cantidad
        Label lblCantidadProducto = new Label("Cantidad:");
        miGrilla.add(lblCantidadProducto, 0, 3);
        TextField cajaCantidadProducto = new TextField();
        ControladorFormulario.cantidadCaracteres(cajaCantidadProducto, 7);
        ControladorFormulario.soloNumeros(cajaCantidadProducto);
        cajaCantidadProducto.setMaxWidth(Double.MAX_VALUE); // Permite que el TextField se expanda
        miGrilla.add(cajaCantidadProducto, 1, 3);
        
        // Fila 4: Categoria
        Label lblCategoria = new Label("Categoría:");
        miGrilla.add(lblCategoria, 0, 4);
        armarCombo();
        comboCategoria.setMaxWidth(Double.MAX_VALUE); // Permite que el ComboBox se expanda
        miGrilla.add(comboCategoria, 1, 4);

        // Fila 5: La imagen
        Label lblImagen = new Label("Imagen:");
        miGrilla.add(lblImagen, 0, 5);

        cajaImagen = new TextField();
        cajaImagen.setDisable(true); // El usuario no debe editar esta caja directamente
        cajaImagen.setPromptText("Ruta de la imagen seleccionada"); // Texto de ayuda

        FileChooser objSeleccionar = crearSelectorDeLaImagen();

        Button btnAgregarImg = new Button("+");
        btnAgregarImg.setPrefWidth(40); // Ancho fijo para el botón "+"
        btnAgregarImg.setMaxHeight(Double.MAX_VALUE); // Permite que se ajuste a la altura del HBox
        btnAgregarImg.setOnAction((e) -> {
            rutaSeleccionada = ControladorImagen.seleccionarImagen(cajaImagen, objSeleccionar);
            if (rutaSeleccionada.isEmpty()) {
                Mensaje.modal(Alert.AlertType.WARNING, null,
                        "ADVERTENCIA", "No se seleccionó ninguna imagen.");
                cajaImagen.setText(""); // Limpiar la caja si no se selecciona nada
            }
        });
        
        HBox panelHorizontal = new HBox(5); // Espacio entre caja y botón
        HBox.setHgrow(cajaImagen, Priority.ALWAYS); // Caja de texto toma el espacio restante
        panelHorizontal.setAlignment(Pos.CENTER_LEFT); // Alinea los elementos a la izquierda en el HBox
        panelHorizontal.getChildren().addAll(cajaImagen, btnAgregarImg);
        miGrilla.add(panelHorizontal, 1, 5);

        // Fila 6: Botón Grabar
        Button btnGrabar = new Button("Crear Producto");
        btnGrabar.setMaxWidth(Double.MAX_VALUE); // Permite que el botón se expanda
        btnGrabar.setTextFill(Color.web("#6C3483"));
        btnGrabar.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        GridPane.setHalignment(btnGrabar, javafx.geometry.HPos.RIGHT); // Alinea el botón a la derecha
        miGrilla.add(btnGrabar, 1, 6); // Ajustado a la fila 6

        btnGrabar.setOnAction(e -> {
            if (formularioDiligenciado(cajaNombreProducto, cajaPrecioProducto, cajaCantidadProducto, comboCategoria)) {
                String nombre = cajaNombreProducto.getText();
                double precio = Double.parseDouble(cajaPrecioProducto.getText());
                int cantidad = Integer.parseInt(cajaCantidadProducto.getText());
                Categoria objCategoria = comboCategoria.getValue();

                if (ControladorProductoGrabar.grabar(nombre, precio, cantidad,
                        objCategoria, cajaImagen.getText(), rutaSeleccionada)) {
                    Mensaje.modal(Alert.AlertType.INFORMATION, null, "ÉXITO", "Producto grabado exitosamente");

                    // Limpiar campos
                    cajaNombreProducto.setText("");
                    cajaPrecioProducto.setText("");
                    cajaCantidadProducto.setText("");
                    comboCategoria.getSelectionModel().select(0);
                    cajaImagen.setText(""); // Limpiar también la caja de imagen
                    rutaSeleccionada = ""; // Restablecer la ruta seleccionada
                    cajaNombreProducto.requestFocus();
                } else {
                    Mensaje.modal(Alert.AlertType.ERROR, null, "ERROR", "No se pudo grabar el producto");
                }
            }
        });

        miFormualario.getChildren().add(miGrilla);
    }

    private boolean formularioDiligenciado(TextField cajaNombreProducto, TextField cajaPrecioProducto,
            TextField cajaCantidadProducto, ComboBox comboCategoria) {
        boolean correcto = false;

        String nombre = cajaNombreProducto.getText();
        String precioTexto = cajaPrecioProducto.getText();
        String cantidadTexto = cajaCantidadProducto.getText();
        int indiceSeleccionado = comboCategoria.getSelectionModel().getSelectedIndex();
        String rutaImg = cajaImagen.getText(); // Verificar si se seleccionó una imagen

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
        } else if (rutaImg.isBlank()) { // Nueva validación para la imagen
            Mensaje.modal(Alert.AlertType.WARNING, null, "Advertencia", "Debe seleccionar una imagen para el producto.");
        }
        else {
            correcto = true;
        }

        return correcto;
    }
}