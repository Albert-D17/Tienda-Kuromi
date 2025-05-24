package edu.poo.vista.categoria;

import edu.poo.modelo.Categoria;
import edu.poo.recurso.utilidad.Fondo;
import edu.poo.recurso.utilidad.Icono;
import edu.poo.recurso.utilidad.Marco;
import edu.poo.recurso.utilidad.Mensaje;
import edu.poo.recurso.dominio.IconoNombre;
import edu.poo.recurso.dominio.Configuracion;
import edu.poo.recurso.dominio.Contenedor;
import edu.poo.recurso.dominio.Ruta;
import edu.poo.controlador.categoria.ControladorCategoListar;
import edu.poo.controlador.categoria.ControladorCategoVentana;
import edu.poo.controlador.categoria.ControladorCategoEliminar;
import edu.poo.modelo.Producto;
import edu.poo.persistencia.DAOProducto;
import java.io.File;
import java.util.Map;
import java.util.HashMap;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class VistaCategoAdmin extends SubScene {

    private final VBox miColumna;
    private final StackPane miFormulario;
    private HBox panelHorizontalAdmin;
    private TableView<Categoria> miTabla;

    private final Map<String, Image> imageCache = new HashMap<>();
    private final Map<Integer, Integer> cantidadCache = new HashMap<>();
    private final Map<String, ImageView> imageViewCache = new HashMap<>();

    private final String centrar = "-fx-alignment: CENTER";
    private final String izquierda = "-fx-alignment: CENTER-LEFT";

    // Elimina estas variables fijas
    // private final double anchoMarco, altoMarco;

    private Pane panelCuerpo;
    private BorderPane panelPrincipal;

    public VistaCategoAdmin(BorderPane princ, Pane pane, double anchoFrm, double altoFrm) {
        super(new StackPane(), anchoFrm, altoFrm);

        miFormulario = (StackPane) getRoot();
        miFormulario.setBackground(Fondo.asignarAleatorio(Configuracion.FONDOS));
        miFormulario.setAlignment(Pos.CENTER);

        // Ya no necesitamos anchoMarco y altoMarco fijos aquí
        // this.anchoMarco = anchoFrm - (anchoFrm * 0.15);
        // this.altoMarco = altoFrm - (altoFrm * 0.10);

        this.panelPrincipal = princ;
        this.panelCuerpo = pane;

        miColumna = new VBox();
        crearMarco();
        armarTabla();
    }

    public StackPane getMiFormulario() {
        return miFormulario;
    }

    private void crearMarco() {
        Rectangle marco = Marco.crear(100, 100,
            Configuracion.DEGRADE_ARREGLO, Configuracion.DEGRADE_BORDE);

    // Ajustamos tamaño proporcional al formulario
    marco.widthProperty().bind(miFormulario.widthProperty().multiply(0.85));
    marco.heightProperty().bind(miFormulario.heightProperty().multiply(0.90));

    // Le ponemos transparencia al marco sin afectar los colores
    marco.setOpacity(0.85); // 85% opaco, 15% transparente, puedes ajustar este valor (0.0 a 1.0)

    miFormulario.getChildren().add(marco);
    }

    private void armarTabla() {
        ObservableList<Categoria> datos = ControladorCategoListar.cargarDatos();
        int cantidadCategorias = datos.size(); // Obtenemos la cantidad de categorías

        Text titulo = new Text("Administrar Categorías (" + cantidadCategorias + ")");
        titulo.setFill(Color.web("#E6E6E6"));
        titulo.setFont(Font.font("verdana", FontWeight.BOLD, 25));

        miTabla = new TableView<>();
        miTabla.setFixedCellSize(50);
        miTabla.setCache(true);
        miTabla.setCacheHint(javafx.scene.CacheHint.SPEED);

        TableColumn<Categoria, Integer> colCodigo = new TableColumn<>("Código");
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codCategoria"));
        colCodigo.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.15));
        colCodigo.setStyle(centrar);

        TableColumn<Categoria, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCategoria"));
        colNombre.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.35));
        colNombre.setStyle(izquierda);

        TableColumn<Categoria, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(categoria ->
                new SimpleStringProperty(categoria.getValue().isEstadoCategoria() ? "Activo" : "Inactivo"));
        colEstado.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.20));
        colEstado.setStyle(centrar);

        TableColumn<Categoria, String> columnaImagen = new TableColumn<>("Imagen");
        columnaImagen.setCellValueFactory(new PropertyValueFactory<>("nomImgOcuCategoria"));

        columnaImagen.setCellFactory(column -> new TableCell<>() {
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

                Thread thread = new Thread(() -> {
                    try {
                        String filePath = Ruta.RUTA_FOTOS + Configuracion.SEPARADOR_CARPETA + nombreImagen;
                        File file = new File(filePath);

                        if (!file.exists() && nombreImagen.endsWith("@")) {
                            String cleanFileName = nombreImagen.substring(0, nombreImagen.length() - 1);
                            file = new File(Ruta.RUTA_FOTOS + Configuracion.SEPARADOR_CARPETA + cleanFileName);
                        }

                        if (file.exists()) {
                            Image cachedImage = imageCache.get(nombreImagen);
                            if (cachedImage == null) {
                                cachedImage = new Image(file.toURI().toString(), 50, 50, true, true);
                                imageCache.put(nombreImagen, cachedImage);
                            }

                            final Image finalImage = cachedImage;
                            javafx.application.Platform.runLater(() -> {
                                if (getItem() == nombreImagen) {
                                    imageView.setImage(finalImage);
                                    setGraphic(imageView);
                                }
                            });
                        } else {
                            javafx.application.Platform.runLater(() -> setGraphic(null));
                        }
                    } catch (Exception e) {
                        javafx.application.Platform.runLater(() -> setGraphic(null));
                    }
                });
                thread.setDaemon(true);
                thread.start();
            }
        });

        columnaImagen.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.20));
        columnaImagen.setStyle(centrar);

        TableColumn<Categoria, Integer> colCantidadProductos = new TableColumn<>("Cant. Productos");
        colCantidadProductos.setCellValueFactory(cellData -> {
            Categoria categoriaActual = cellData.getValue();
            int codCategoria = categoriaActual.getCodCategoria();
            if (!cantidadCache.containsKey(codCategoria)) {
                cantidadCache.put(codCategoria,
                        ControladorCategoListar.obtenerCantidadProductosParaCategoria(categoriaActual));
            }
            return new SimpleIntegerProperty(cantidadCache.get(codCategoria)).asObject();
        });
        colCantidadProductos.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.15));
        colCantidadProductos.setStyle(centrar);

        miTabla.getColumns().addAll(colCodigo, colNombre, colEstado, colCantidadProductos, columnaImagen);

        miTabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        // Bind table dimensions to the miFormulario (SubScene) dimensions
        // Adjust the multipliers to ensure the table doesn't cover the entire frame
        miTabla.maxWidthProperty().bind(miFormulario.widthProperty().multiply(0.80)); // 80% del ancho del formulario
        miTabla.maxHeightProperty().bind(miFormulario.heightProperty().multiply(0.65)); // 65% del alto del formulario

        miTabla.setItems(datos);
        miTabla.refresh();

        armarIconosAdministrar();

        miColumna.setSpacing(15);
        miColumna.setAlignment(Pos.CENTER);
        // Add padding to miColumna to create space for the frame
        miColumna.setPadding(new Insets(20, 20, 20, 20)); // Adjust padding as needed for the frame
        miColumna.getChildren().addAll(titulo, miTabla, panelHorizontalAdmin);

        miFormulario.getChildren().add(miColumna);
    }

    private void armarIconosAdministrar() {
        int anchoBoton = 40;

        Button btnEliminar = new Button();
        btnEliminar.setPrefWidth(anchoBoton);
        btnEliminar.setCursor(Cursor.HAND);
        btnEliminar.setGraphic(Icono.obtenerIcono(IconoNombre.ICONO_BORRAR, 16));

        btnEliminar.setOnAction(e -> {
            Categoria categoria = miTabla.getSelectionModel().getSelectedItem();
            if (categoria == null) {
                Mensaje.modal(Alert.AlertType.WARNING, null, "Advertencia", "Debes seleccionar una categoría");
            } else {
                DAOProducto daoProducto = new DAOProducto();
                if (daoProducto.existenProductosConCategoria(categoria.getCodCategoria())) {
                    Mensaje.modal(Alert.AlertType.WARNING, null, "Advertencia",
                            "No se puede eliminar la categoría '" + categoria.getNombreCategoria() + "' porque tiene productos asociados.");
                    return;
                }

                Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                alerta.setTitle("Confirmación");
                alerta.setHeaderText(null);
                alerta.setContentText("¿Deseas eliminar la categoría: " + categoria.getNombreCategoria() + "?");

                if (alerta.showAndWait().get() == ButtonType.OK) {
                    int pos = miTabla.getSelectionModel().getSelectedIndex();
                    if (ControladorCategoEliminar.eliminar(pos)) {
                        miTabla.setItems(ControladorCategoListar.cargarDatos());
                        miTabla.refresh();
                        Mensaje.modal(Alert.AlertType.INFORMATION, null, "Éxito", "Categoría eliminada exitosamente");
                    } else {
                        Mensaje.modal(Alert.AlertType.ERROR, null, "Error", "No se pudo eliminar la categoría");
                    }
                }
            }
        });

        Button btnEditar = new Button();
        btnEditar.setPrefWidth(anchoBoton);
        btnEditar.setCursor(Cursor.HAND);
        btnEditar.setGraphic(Icono.obtenerIcono(IconoNombre.ICONO_EDITAR, 16));

        btnEditar.setOnAction(e -> {
            Categoria categoria = miTabla.getSelectionModel().getSelectedItem();
            if (categoria == null) {
                Mensaje.modal(Alert.AlertType.WARNING, null, "Advertencia", "Debes seleccionar una categoría");
            } else {
                int pos = miTabla.getSelectionModel().getSelectedIndex();
                panelCuerpo = ControladorCategoVentana.editar(panelPrincipal, panelCuerpo,
                        Configuracion.ANCHO_APP, Contenedor.ALTO_CUERPO.getValor(), categoria, pos);

                panelPrincipal.setCenter(null);
                panelPrincipal.setCenter(panelCuerpo);
            }
        });

        Button btnCancelar = new Button();
        btnCancelar.setPrefWidth(anchoBoton);
        btnCancelar.setCursor(Cursor.HAND);
        btnCancelar.setGraphic(Icono.obtenerIcono(IconoNombre.ICONO_CANCELAR, 16));

        btnCancelar.setOnAction(e -> {
            miTabla.getSelectionModel().clearSelection();
        });

        panelHorizontalAdmin = new HBox(4);
        panelHorizontalAdmin.setAlignment(Pos.CENTER);
        panelHorizontalAdmin.getChildren().addAll(btnEliminar, btnEditar, btnCancelar);
    }
}