package edu.poo.vista.producto;

import edu.poo.modelo.Categoria;
import edu.poo.modelo.Producto;
import edu.poo.recurso.utilidad.Fondo;
import edu.poo.recurso.utilidad.Icono;
import edu.poo.recurso.utilidad.Marco;
import edu.poo.recurso.utilidad.Mensaje;
import edu.poo.recurso.dominio.IconoNombre;
import edu.poo.recurso.dominio.Configuracion;
import edu.poo.recurso.dominio.Contenedor;
import edu.poo.controlador.producto.ControladorProductoListar;
import edu.poo.controlador.producto.ControladorProductoVentana;
import edu.poo.controlador.producto.ControladorProductoEliminar;
import edu.poo.recurso.dominio.Ruta;

import javafx.application.Platform;

import java.text.DecimalFormat;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.SubScene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority; // Importar Priority
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class VistaProductoAdmin extends SubScene {

    private final VBox miColumna;
    private final StackPane miFormulario;
    private HBox panelHorizontalAdmin;
    private TableView<Producto> miTabla;

    // Caches para optimizar rendimiento
    private final Map<String, Image> imageCache = new HashMap<>();
    private final Map<String, ImageView> imageViewCache = new HashMap<>();

    private final String centrar = "-fx-alignment: CENTER;"; 
    private final String izquierda = "-fx-alignment: CENTER-LEFT;"; 
    private final String derecha = "-fx-alignment: CENTER-RIGHT;"; 

    // Eliminamos las variables fijas de anchoMarco y altoMarco
    // private final double anchoMarco, altoMarco;

    private Pane panelCuerpo;
    private BorderPane panelPrincipal;

    public VistaProductoAdmin(BorderPane princ, Pane pane, double anchoFrm, double altoFrm /* formulario */) {
        super(new StackPane(), anchoFrm, altoFrm);
        
        Background fondo = Fondo.asignarAleatorio(Configuracion.FONDOS);
        
        miFormulario = (StackPane) getRoot();
        miFormulario.setBackground(fondo);
        miFormulario.setAlignment(Pos.CENTER);

        // Eliminamos la inicialización de anchoMarco y altoMarco aquí
        // anchoMarco = anchoFrm - (anchoFrm * 0.15);
        // altoMarco = altoFrm - (altoFrm * 0.10);

        panelPrincipal = princ;
        panelCuerpo = pane;

        miColumna = new VBox();
        miColumna.setSpacing(15);
        miColumna.setAlignment(Pos.CENTER); // Alineación a la parte superior central
        miColumna.setPadding(new Insets(20)); // Padding para separar del borde del marco

        // Tamaño proporcional al formulario para miColumna
        miColumna.prefWidthProperty().bind(miFormulario.widthProperty().multiply(Configuracion.MARCO_ANCHO_PORCENTAJE));
        miColumna.maxWidthProperty().bind(miFormulario.widthProperty().multiply(Configuracion.MARCO_ANCHO_PORCENTAJE));
        VBox.setVgrow(miColumna, Priority.ALWAYS); // Permite que el VBox crezca verticalmente

        crearMarco();
        armarTabla();

        // El panelHorizontalAdmin se crea dentro de armarIconosAdministrar()
        // y se añade al final de miColumna en armarTabla().
        miFormulario.getChildren().add(miColumna);
    }

    public StackPane getMiFormulario() {
        return miFormulario;
    }

    private void crearMarco() {
        Rectangle marco = Marco.crear(100, 100, // Valores iniciales no importan, se bindearán
                Configuracion.DEGRADE_ARREGLO,
                Configuracion.DEGRADE_BORDE);
        StackPane.setAlignment(marco, Pos.CENTER);
        
        // El marco se adaptará al tamaño del formulario principal con un porcentaje
        marco.widthProperty().bind(miFormulario.widthProperty().multiply(0.85));
        marco.heightProperty().bind(miFormulario.heightProperty().multiply(0.90));
        
        // Añadimos transparencia al marco
        marco.setOpacity(0.7);
        
        miFormulario.getChildren().add(marco);
    }

    private void armarTabla() {
        Text titulo = new Text("Listado De Productos");
        titulo.setFill(Color.web("#E6E6E6"));
        titulo.setFont(Font.font("verdana", FontWeight.BOLD, 25));
        
        miTabla = new TableView<>();
        miTabla.setFixedCellSize(50);
        miTabla.setCache(true);
        miTabla.setCacheHint(javafx.scene.CacheHint.SPEED);

        // Celda Código del producto
        TableColumn<Producto, Integer> columnaCodigoProducto = new TableColumn<>("Código");
        columnaCodigoProducto.setCellValueFactory(new PropertyValueFactory<>("codProducto"));
        columnaCodigoProducto.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.08));
        columnaCodigoProducto.setStyle(centrar);

        TableColumn<Producto, String> columnaNombre = new TableColumn<>("Nombre Producto");
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nomProducto"));
        columnaNombre.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.25));
        columnaNombre.setStyle(izquierda);

        // Celda categoría
        TableColumn<Producto, String> codCategoriaColum = new TableColumn<>("Categoría");
        codCategoriaColum.setCellValueFactory(
                miCatego -> {
                    Categoria categoria = miCatego.getValue().getCatProducto();
                    int codCatego = categoria.getCodCategoria();
                    String cadena = categoria.getNombreCategoria() + " (" + codCatego + ")";
                    return new SimpleStringProperty(cadena);
                });
        codCategoriaColum.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.25));
        codCategoriaColum.setStyle(izquierda);

        // Celda Precio
        DecimalFormat precioTXT = new DecimalFormat("#,##0.00"); // Formato mejorado para precios
        TableColumn<Producto, String> columnaPrecio = new TableColumn<>("Precio");
        columnaPrecio.setCellValueFactory(
                (dato) -> new SimpleStringProperty(precioTXT.format(dato.getValue().getPreProducto())));
        columnaPrecio.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.12));
        columnaPrecio.setStyle(derecha);

        // Celda cantidad
        TableColumn<Producto, Integer> columnaCantidad = new TableColumn<>("Cantidad");
        columnaCantidad.setCellValueFactory(new PropertyValueFactory<>("canProducto"));
        columnaCantidad.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.10));
        columnaCantidad.setStyle(centrar);

        // Celda Imagen
        TableColumn<Producto, String> columnaImagen = new TableColumn<>("Imagen");
        columnaImagen.setCellValueFactory(new PropertyValueFactory<>("nomImgOcuProducto"));
        columnaImagen.setCellFactory(column -> {
            TableCell<Producto, String> cell = new TableCell<Producto, String>() {
                @Override
                protected void updateItem(String nombreImagen, boolean empty) {
                    super.updateItem(nombreImagen, empty);
                    
                    if (empty || nombreImagen == null) {
                        setGraphic(null);
                        return;
                    }
                    
                    ImageView imageView = imageViewCache.computeIfAbsent(nombreImagen, k -> {
                        ImageView iv = new ImageView();
                        iv.setFitWidth(50);
                        iv.setFitHeight(50);
                        iv.setPreserveRatio(true);
                        iv.setSmooth(true);
                        return iv;
                    });
                    
                    Image cachedImage = imageCache.get(nombreImagen);
                    if (cachedImage != null) {
                        imageView.setImage(cachedImage);
                        setGraphic(imageView);
                        return;
                    }
                    
                    Thread thread = new Thread(() -> {
                        try {
                            String filePath = Ruta.RUTA_FOTOS + Configuracion.SEPARADOR_CARPETA + nombreImagen;
                            File file = new File(filePath);
                            
                            if (!file.exists() && nombreImagen.endsWith("@")) {
                                String cleanFileName = nombreImagen.substring(0, nombreImagen.length() - 1);
                                file = new File(Ruta.RUTA_FOTOS + Configuracion.SEPARADOR_CARPETA + cleanFileName);
                            }
                            
                            if (file.exists()) {
                                Image image = new Image(file.toURI().toString(), 50, 50, true, true);
                                imageCache.put(nombreImagen, image);
                                
                                Platform.runLater(() -> {
                                    if (getItem() == nombreImagen) { // Evita actualizar si la celda ya fue reciclada para otro ítem
                                        imageView.setImage(image);
                                        setGraphic(imageView);
                                    }
                                });
                            } else {
                                System.err.println("No se pudo encontrar la imagen: " + filePath);
                                Platform.runLater(() -> setGraphic(null));
                            }
                        } catch (Exception e) {
                            System.err.println("Error al cargar la imagen: " + e.getMessage());
                            Platform.runLater(() -> setGraphic(null));
                        }
                    });
                    thread.setDaemon(true);
                    thread.start();
                }
            };
            return cell;
        });
        columnaImagen.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.20));
        columnaImagen.setStyle(centrar);

        miTabla.getColumns().addAll(
            columnaCodigoProducto,
            columnaNombre,
            codCategoriaColum,
            columnaPrecio,
            columnaCantidad,
            columnaImagen
        );

        ObservableList<Producto> datosCompletos = ControladorProductoListar.cargarDatos();
        miTabla.setItems(datosCompletos);
        miTabla.refresh();

        armarIconosAdministrar(); // Se arma el HBox de botones aquí

        miColumna.getChildren().addAll(titulo, miTabla, panelHorizontalAdmin); // Se añade la tabla y los botones al VBox
    }

    private void armarIconosAdministrar() {
        // En un diseño responsive, el tamaño fijo de los botones (anchoBoton) 
        // podría no ser ideal si se quiere que se escalen también.
        // Sin embargo, para mantener la coherencia con el ejemplo dado y si los iconos son pequeños,
        // un tamaño fijo puede ser aceptable si el HBox que los contiene es responsivo.
        int anchoBoton = 40; 

        Button btnEliminar = new Button();
        btnEliminar.setPrefWidth(anchoBoton);
        btnEliminar.setCursor(Cursor.HAND);
        btnEliminar.setGraphic(Icono.obtenerIcono(IconoNombre.ICONO_BORRAR, 16));

        btnEliminar.setOnAction((e) -> {
            if (miTabla.getSelectionModel().getSelectedItem() == null) {
                Mensaje.modal(Alert.AlertType.WARNING, null, "Advertencia", "Debes seleccionar un producto!");
            } else {
                String texto1, texto2, texto3, texto4;
                Producto objProducto = miTabla.getSelectionModel().getSelectedItem();

                texto1 = "¿Seguro que quieres borrar el producto?\n";
                texto2 = "\nCódigo: " + objProducto.getCodProducto();
                texto3 = "\nNombre: " + objProducto.getNomProducto();
                texto4 = "\nEsto es irreversible!!!";

                Alert msg = new Alert(Alert.AlertType.CONFIRMATION);
                msg.setTitle("Advertencia Borrar");
                msg.setHeaderText(null);
                msg.setContentText(texto1 + texto2 + texto3 + texto4);
                msg.initOwner(null);
                if (msg.showAndWait().get() == ButtonType.OK) {
                    int posicion = miTabla.getSelectionModel().getSelectedIndex();
                    if (ControladorProductoEliminar.eliminar(posicion)) {
                        miTabla.setItems(ControladorProductoListar.cargarDatos());
                        miTabla.refresh();
                        Mensaje.modal(Alert.AlertType.INFORMATION, null, "Éxito", "Producto borrado");

                    } else {
                        Mensaje.modal(Alert.AlertType.ERROR, null, "Error", "No se pudo borrar");
                    }
                } else {
                    miTabla.getSelectionModel().clearSelection();
                }
            }
        });

        // El de actualizar
        Button btnEditar = new Button();
        btnEditar.setPrefWidth(anchoBoton);
        btnEditar.setCursor(Cursor.HAND);
        btnEditar.setGraphic(Icono.obtenerIcono(IconoNombre.ICONO_EDITAR, 16));

        btnEditar.setOnAction((e) -> {
            if (miTabla.getSelectionModel().getSelectedItem() == null) {
                Mensaje.modal(Alert.AlertType.WARNING, null, "Te lo advierto", "selecciona un producto");
            } else {
                Producto objPro = miTabla.getSelectionModel().getSelectedItem();
                int posi = miTabla.getSelectionModel().getSelectedIndex();

                panelCuerpo = ControladorProductoVentana.editar(panelPrincipal, panelCuerpo,
                        Configuracion.ANCHO_APP, Contenedor.ALTO_CUERPO.getValor(), objPro, posi);

                panelPrincipal.setCenter(null);
                panelPrincipal.setCenter(panelCuerpo);

            }
        });

        // El de cancelar
        Button btnCancelar = new Button();
        btnCancelar.setPrefWidth(anchoBoton);
        btnCancelar.setCursor(Cursor.HAND);
        btnCancelar.setGraphic(Icono.obtenerIcono(IconoNombre.ICONO_CANCELAR, 16));

        btnCancelar.setOnAction((e) -> {
            miTabla.getSelectionModel().clearSelection();
        });

        panelHorizontalAdmin = new HBox(10); // Espaciado de 10 entre botones
        panelHorizontalAdmin.setAlignment(Pos.CENTER); // Centrar los botones en el HBox
        // Se puede bidear el ancho del HBox a un porcentaje del VBox contenedor
        panelHorizontalAdmin.prefWidthProperty().bind(miColumna.widthProperty().multiply(0.95)); 
        panelHorizontalAdmin.getChildren().addAll(btnEliminar, btnEditar, btnCancelar);
    }
}