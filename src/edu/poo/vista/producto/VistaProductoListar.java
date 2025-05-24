package edu.poo.vista.producto;

import edu.poo.controlador.producto.ControladorProductoListar;
import edu.poo.modelo.Categoria;
import edu.poo.modelo.Producto;
import edu.poo.recurso.dominio.Configuracion;
import edu.poo.recurso.utilidad.Fondo;
import edu.poo.recurso.utilidad.Marco;

import java.text.DecimalFormat;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.SubScene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.layout.Priority; // Importar Priority

public class VistaProductoListar extends SubScene {

    private final VBox miColumna;
    private final StackPane miFormulario;

    private final String centrar = "-fx-alignment: CENTER;"; // Añadido ;
    private final String izquierda = "-fx-alignment: CENTER-LEFT;"; // Añadido ;
    private final String derecha = "-fx-alignment: CENTER-RIGHT;"; // Añadido ;

    // Eliminamos las variables fijas de anchoMarco y altoMarco
    // private final double anchoMarco, altoMarco;

    public VistaProductoListar(double anchoFrm, double altoFrm /*formulario*/) {
        super(new StackPane(), anchoFrm, altoFrm);
        
        Background fondo = Fondo.asignarAleatorio(Configuracion.FONDOS);
        
        miFormulario = (StackPane) getRoot();
        miFormulario.setBackground(fondo);
        miFormulario.setAlignment(Pos.CENTER); // Centrar el formulario completo

        miColumna = new VBox();
        miColumna.setSpacing(15);
        miColumna.setAlignment(Pos.TOP_CENTER); // Alineación a la parte superior central dentro del VBox
        miColumna.setPadding(new Insets(20)); // Padding para separar del borde del marco

        // Tamaño proporcional al formulario
        miColumna.prefWidthProperty().bind(miFormulario.widthProperty().multiply(Configuracion.MARCO_ANCHO_PORCENTAJE));
        miColumna.maxWidthProperty().bind(miFormulario.widthProperty().multiply(Configuracion.MARCO_ANCHO_PORCENTAJE));
        VBox.setVgrow(miColumna, Priority.ALWAYS); // Permite que el VBox crezca verticalmente

        crearMarco();
        armarTabla();

        miFormulario.getChildren().add(miColumna); // Añadir miColumna al formulario principal
    }

    public StackPane getMiFormulario() {
        return miFormulario;
    }

    private void crearMarco() {
        Rectangle marco = Marco.crear(0, 0, // Valores iniciales no importan, se bindearán
                Configuracion.DEGRADE_ARREGLO,
                Configuracion.DEGRADE_BORDE);
        StackPane.setAlignment(marco, Pos.CENTER);
        
        // El marco se adaptará al tamaño del formulario principal con un porcentaje
        marco.widthProperty().bind(miFormulario.widthProperty().multiply(Configuracion.MARCO_ANCHO_PORCENTAJE));
        marco.heightProperty().bind(miFormulario.heightProperty().multiply(Configuracion.MARCO_ALTO_PORCENTAJE));
        
        // Añadimos transparencia al marco
        marco.setOpacity(0.7);
        
        miFormulario.getChildren().add(marco);
    }

    private void armarTabla() {
        Text titulo = new Text("Listado De Productos");
        titulo.setFill(Color.web("#E6E6E6"));
        titulo.setFont(Font.font("verdana", FontWeight.BOLD, 25));

        TableView<Producto> miTabla = new TableView<>();
        miTabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS); // Evita el scroll horizontal
        
        // La tabla se adapta al 95% del ancho del VBox miColumna y crece verticalmente
        miTabla.prefWidthProperty().bind(miColumna.widthProperty().multiply(0.95)); // 95% del ancho de miColumna
        miTabla.maxWidthProperty().bind(miColumna.widthProperty().multiply(0.95));
        VBox.setVgrow(miTabla, Priority.ALWAYS); // Permite que la tabla crezca verticalmente

        TableColumn<Producto, Integer> columnaCodigoProducto = new TableColumn<>("Código");
        columnaCodigoProducto.setCellValueFactory(new PropertyValueFactory<>("codProducto"));
        columnaCodigoProducto.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.08)); // Porcentaje del ancho total de la tabla
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

        DecimalFormat precioTXT = new DecimalFormat("#,##0.00"); // Formato mejorado para precios
        TableColumn<Producto, String> columnaPrecio = new TableColumn<>("Precio");
        columnaPrecio.setCellValueFactory(
                (dato) -> new SimpleStringProperty(precioTXT.format(dato.getValue().getPreProducto()))
        );
        columnaPrecio.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.12));
        columnaPrecio.setStyle(derecha);

        TableColumn<Producto, Integer> columnaCantidad = new TableColumn<>("Cantidad");
        columnaCantidad.setCellValueFactory(new PropertyValueFactory<>("canProducto"));
        columnaCantidad.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.10));
        columnaCantidad.setStyle(centrar);

        TableColumn<Producto, String> columnaImagen = new TableColumn<>("Imagen");
        columnaImagen.setCellValueFactory(new PropertyValueFactory<>("nomImgPubProducto"));
        columnaImagen.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.20));
        columnaImagen.setStyle(izquierda);

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

        miColumna.getChildren().addAll(titulo, miTabla); // Añadir el título y la tabla al VBox
    }
}