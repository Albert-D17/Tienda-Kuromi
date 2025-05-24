package edu.poo.vista.categoria;

import edu.poo.modelo.Categoria;
import edu.poo.persistencia.DAOCategorias;
import edu.poo.recurso.dominio.Configuracion;
import edu.poo.recurso.utilidad.Fondo;
import edu.poo.recurso.utilidad.Marco;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.SubScene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import edu.poo.modelo.Categoria;
import edu.poo.persistencia.DAOCategorias;
import edu.poo.recurso.dominio.Configuracion;
import edu.poo.recurso.dominio.Ruta;
import edu.poo.recurso.utilidad.Fondo;
import edu.poo.recurso.utilidad.Marco;
import edu.poo.recurso.utilidad.Mensaje;

public class VistaCategoCarrusel extends SubScene{
    
     private final StackPane contenedor;
    private final double anchoMarco;
    private final double altoMarco;
    private final List<Categoria> listaCategorias;
    private int indiceActual = 0;

    private final ImageView vistaImagen;
    private final Text textoNombre;
    private final Text textoEstado;

    public StackPane getContenedor() {
        return contenedor;
    }
    

    public VistaCategoCarrusel(double ancho, double alto) {
        super(new StackPane(), ancho, alto);
        contenedor = (StackPane) getRoot();
        contenedor.setAlignment(Pos.CENTER);
        contenedor.setBackground(Fondo.asignarAleatorio(Configuracion.FONDOS));

        anchoMarco = ancho - ancho * 0.3;
        altoMarco = alto - alto * 0.2;

        Rectangle marco = Marco.crear(anchoMarco, altoMarco, Configuracion.DEGRADE_ARREGLO, Configuracion.DEGRADE_BORDE);
        contenedor.getChildren().add(marco);

        DAOCategorias dao = new DAOCategorias();
        listaCategorias = dao.SelectFrom();

        vistaImagen = new ImageView();
        vistaImagen.setFitWidth(180);
        vistaImagen.setFitHeight(180);
        vistaImagen.setPreserveRatio(true);

        textoNombre = new Text();
        textoNombre.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        textoNombre.setFill(Color.WHITE);

        textoEstado = new Text();
        textoEstado.setFont(Font.font("Verdana", FontWeight.NORMAL, 16));
        textoEstado.setFill(Color.WHITE);

        

        ImageView iconoAnterior = new ImageView(new Image("file:src/poo/edu/recursos/imagenes/categoria/btnAtras.png"));
        iconoAnterior.setFitHeight(40);
        iconoAnterior.setFitWidth(40);

        ImageView iconoSiguiente = new ImageView(new Image("file:src/poo/edu/recursos/imagenes/categoria/btnSigiente.png"));
        iconoSiguiente.setFitHeight(40);
        iconoSiguiente.setFitWidth(40);
        
        Button btnAnterior = new Button();
        btnAnterior.setGraphic(iconoAnterior);
        btnAnterior.setStyle("-fx-background-color: transparent;");
        btnAnterior.setOnAction(e -> mostrarAnterior());

        Button btnSiguiente = new Button();
        btnSiguiente.setGraphic(iconoSiguiente);
        btnSiguiente.setStyle("-fx-background-color: transparent;");
        btnSiguiente.setOnAction(e -> mostrarSiguiente());

        Button btnEliminar = new Button("Eliminar categoría");
        btnEliminar.setOnAction(e -> eliminarActual());

        StackPane.setAlignment(iconoAnterior, Pos.CENTER_LEFT);
        StackPane.setAlignment(iconoSiguiente, Pos.CENTER_RIGHT);
        StackPane.setAlignment(btnEliminar, Pos.BOTTOM_CENTER);

        StackPane.setAlignment(vistaImagen, Pos.TOP_CENTER);
        StackPane.setAlignment(textoNombre, Pos.CENTER);
        StackPane.setAlignment(textoEstado, Pos.CENTER);
        

        StackPane.setMargin(textoNombre, new javafx.geometry.Insets(150, 0, 0, 0));
        StackPane.setMargin(textoEstado, new javafx.geometry.Insets(190, 0, 0, 0));
        
        contenedor.getChildren().addAll(vistaImagen, textoNombre, textoEstado, btnAnterior, btnSiguiente, btnEliminar);

        if (!listaCategorias.isEmpty()) {
            mostrarCategoria();
        } else {
            textoNombre.setText("No hay categorías disponibles");
        }
    }

    private void mostrarCategoria() {
        if (listaCategorias.isEmpty()) return;

        Categoria actual = listaCategorias.get(indiceActual);

        textoNombre.setText("Nombre: " + actual.getNombreCategoria());
        textoEstado.setText("Estado: " + (actual.isEstadoCategoria() ? "Activo" : "Inactivo"));
        

        try {
            String rutaImagen = Ruta.RUTA_IMAGENES + actual.getNomImgOcuCategoria();
            vistaImagen.setImage(new Image(rutaImagen));
        } catch (Exception e) {
            vistaImagen.setImage(new Image(Configuracion.IMAGEN_POR_DEFECTO));
        }
    }

    private void mostrarAnterior() {
        if (listaCategorias.isEmpty()) return;
        indiceActual = (indiceActual - 1 + listaCategorias.size()) % listaCategorias.size();
        mostrarCategoria();
    }

    private void mostrarSiguiente() {
        if (listaCategorias.isEmpty()) return;
        indiceActual = (indiceActual + 1) % listaCategorias.size();
        mostrarCategoria();
    }

    private void eliminarActual() {
        if (listaCategorias.isEmpty()) return;
        Categoria actual = listaCategorias.get(indiceActual);

        if (actual.getCodCategoria()> 0) {
            Mensaje.modal(Alert.AlertType.WARNING, null, "No se puede eliminar", "La categoría tiene productos asociados.");
            return;
        }

        DAOCategorias dao = new DAOCategorias();
        if (dao.deleteFrom(indiceActual)) {
            listaCategorias.remove(indiceActual);
            if (listaCategorias.isEmpty()) {
                textoNombre.setText("No hay categorías disponibles");
                vistaImagen.setImage(new Image("file:src/edu/poo/recursos/img/no_disponible.png"));
                textoEstado.setText("");
               
            } else {
                indiceActual = indiceActual % listaCategorias.size();
                mostrarCategoria();
            }
        } else {
            Mensaje.modal(Alert.AlertType.ERROR, null, "Error", "No se pudo eliminar la categoría.");
        }
    }
}