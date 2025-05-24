package edu.poo.vista.categoria;

import edu.poo.controlador.categoria.ControladorCategoListar;
import edu.poo.modelo.Categoria;
import edu.poo.recurso.dominio.Configuracion;
import edu.poo.recurso.utilidad.Fondo;
import edu.poo.recurso.utilidad.Marco;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.SubScene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class VistaCategoListar extends SubScene {

    private final VBox miColumna;
    private final StackPane miFormulario;

    private final String centrar = "-fx-alignment: CENTER";
    private final String izquierda = "-fx-alignment: CENTER-LEFT";

    public VistaCategoListar(double anchoFrm, double altoFrm) {
        super(new StackPane(), anchoFrm, altoFrm);
        miFormulario = (StackPane) getRoot();
        miFormulario.setBackground(Fondo.asignarAleatorio(Configuracion.FONDOS));
        miFormulario.setAlignment(Pos.CENTER);

        miColumna = new VBox();
        miColumna.setSpacing(15);
        miColumna.setAlignment(Pos.TOP_CENTER);
        miColumna.setPadding(new Insets(20));

        // Tamaño proporcional al formulario
        miColumna.prefWidthProperty().bind(miFormulario.widthProperty().multiply(0.8));
        miColumna.maxWidthProperty().bind(miFormulario.widthProperty().multiply(0.8));

        crearMarco();
        armarTabla();

        VBox.setVgrow(miColumna, Priority.ALWAYS);
        miFormulario.getChildren().add(miColumna);
    }

    public StackPane getMiFormulario() {
        return miFormulario;
    }

    private void crearMarco() {
        Rectangle marco = Marco.crear(0, 0,
            Configuracion.DEGRADE_ARREGLO,
            Configuracion.DEGRADE_BORDE);
    StackPane.setAlignment(marco, Pos.CENTER);
    marco.widthProperty().bind(miFormulario.widthProperty().multiply(Configuracion.MARCO_ANCHO_PORCENTAJE));
    marco.heightProperty().bind(miFormulario.heightProperty().multiply(Configuracion.MARCO_ALTO_PORCENTAJE));
    
    // Agregar transparencia
    marco.setOpacity(0.7); // Ajusta entre 0.0 (total transparente) y 1.0 (opaco)
    
    miFormulario.getChildren().add(marco);
    }

    private void armarTabla() {
        Text titulo = new Text("Listado De Categorías");
        titulo.setFill(Color.web("#E6E6E6"));
        titulo.setFont(Font.font("verdana", FontWeight.BOLD, 25));

        TableView<Categoria> miTabla = new TableView<>();
        miTabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        // Tamaño adaptable y centrado
        miTabla.prefWidthProperty().bind(miFormulario.widthProperty().multiply(0.75));
        miTabla.maxWidthProperty().bind(miFormulario.widthProperty().multiply(0.75));
        VBox.setVgrow(miTabla, Priority.ALWAYS);

        TableColumn<Categoria, Integer> columnaCod = new TableColumn<>("Cod");
        columnaCod.setCellValueFactory(new PropertyValueFactory<>("CodCategoria"));
        columnaCod.setStyle(centrar);

        TableColumn<Categoria, String> columnaNombre = new TableColumn<>("Nombre");
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCategoria"));

        TableColumn<Categoria, String> columnaEstado = new TableColumn<>("Estado");
        columnaEstado.setCellValueFactory(objCategoria -> {
            String estado = objCategoria.getValue().isEstadoCategoria() ? "Activo" : "Inactivo";
            return new SimpleStringProperty(estado);
        });
        columnaEstado.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String estadoTXT, boolean bandera) {
                super.updateItem(estadoTXT, bandera);
                if (bandera || estadoTXT == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(estadoTXT);
                    setStyle(estadoTXT.equals("Activo") ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
                }
            }
        });
        columnaEstado.setStyle(izquierda);

        TableColumn<Categoria, Integer> columnaCantidadProductos = new TableColumn<>("Cant. Productos");
        columnaCantidadProductos.setCellValueFactory(cellData -> {
            int cantidad = ControladorCategoListar.obtenerCantidadProductosParaCategoria(cellData.getValue());
            return new SimpleIntegerProperty(cantidad).asObject();
        });
        columnaCantidadProductos.setStyle(centrar);

        TableColumn<Categoria, String> columnaNomImgPub = new TableColumn<>("Nombre Imagen Pública");
        columnaNomImgPub.setCellValueFactory(new PropertyValueFactory<>("nomImgPubCategoria"));
        columnaNomImgPub.setStyle(izquierda);

        miTabla.getColumns().addAll(columnaCod, columnaNombre, columnaNomImgPub, columnaCantidadProductos, columnaEstado);

        ObservableList<Categoria> datosCompletos = ControladorCategoListar.cargarDatos();
        miTabla.setItems(datosCompletos);

        miColumna.getChildren().addAll(titulo, miTabla);
    }
}
