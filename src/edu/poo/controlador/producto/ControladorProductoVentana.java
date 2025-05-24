package edu.poo.controlador.producto;

import edu.poo.vista.producto.VistaProductoAdmin;
import edu.poo.vista.producto.VistaProductoCrear;
import edu.poo.vista.producto.VistaProductoEditar;
import edu.poo.vista.producto.VistaProductoListar;
import edu.poo.controlador.varios.ControladorEfecto;
import edu.poo.modelo.Producto;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class ControladorProductoVentana {

    public static StackPane crear(double anchoFrm, double altoFrm) {
        VistaProductoCrear carroCrearSubScena = new VistaProductoCrear(anchoFrm, altoFrm);
        StackPane contenedor = carroCrearSubScena.getMiFormualario();

        ControladorEfecto.aplicarEfecto(contenedor, anchoFrm, altoFrm);

        return contenedor;
    }

    public static StackPane listar(double anchoFrm, double altoFrm) {
        VistaProductoListar ventana = new VistaProductoListar(anchoFrm, altoFrm);
        StackPane contenedor = ventana.getMiFormulario();

        ControladorEfecto.aplicarEfecto(contenedor, anchoFrm, altoFrm);

        return contenedor;
    }

    public static StackPane administrar(BorderPane princ, Pane pane, double anchoFrm, double altoFrm) {
        VistaProductoAdmin ventana = new VistaProductoAdmin(princ, pane, anchoFrm, altoFrm);
        StackPane contenedor = ventana.getMiFormulario();

        ControladorEfecto.aplicarEfecto(contenedor, anchoFrm, altoFrm);

        return contenedor;
    }

    public static StackPane editar(BorderPane princ, Pane pane, double anchoFrm, double altoFrm, Producto objProducto,
            int posicion) {
        VistaProductoEditar ventana = new VistaProductoEditar(
                princ, pane, anchoFrm, altoFrm, objProducto, posicion);
        StackPane contenedor = ventana.getMiFormualario();

        ControladorEfecto.aplicarEfecto(contenedor, anchoFrm, altoFrm);

        return contenedor;
    }
}
