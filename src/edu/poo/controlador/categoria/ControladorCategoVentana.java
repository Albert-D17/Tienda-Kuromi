package edu.poo.controlador.categoria;

import edu.poo.vista.categoria.VistaCategoAdmin;
import edu.poo.vista.categoria.VistaCategoCrear;
import edu.poo.vista.categoria.VistaCategoEditar;
import edu.poo.vista.categoria.VistaCategoListar;
import edu.poo.controlador.varios.ControladorEfecto;
import edu.poo.modelo.Categoria;
import edu.poo.vista.categoria.VistaCategoCarrusel;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class ControladorCategoVentana {

    public static StackPane crear(double anchoFrm, double altoFrm) {
        VistaCategoCrear categoriaCrearSubScena = new VistaCategoCrear(anchoFrm, altoFrm);
        StackPane contenedor = categoriaCrearSubScena.getMiFormualario();

        ControladorEfecto.aplicarEfecto(contenedor, anchoFrm, altoFrm);

        return contenedor;
    }

    public static StackPane listar(double anchoFrm, double altoFrm) {
        VistaCategoListar ventana = new VistaCategoListar(anchoFrm, altoFrm);
        StackPane contenedor = ventana.getMiFormulario();

        ControladorEfecto.aplicarEfecto(contenedor, anchoFrm, altoFrm);

        return contenedor;
    }

    public static StackPane administrar(BorderPane princ, Pane pane, double anchoFrm, double altoFrm) {
        VistaCategoAdmin ventana = new VistaCategoAdmin(princ, pane, anchoFrm, altoFrm);
        StackPane contenedor = ventana.getMiFormulario();

        ControladorEfecto.aplicarEfecto(contenedor, anchoFrm, altoFrm);

        return contenedor;
    }

    public static StackPane editar(BorderPane princ, Pane pane, double anchoFrm, double altoFrm, Categoria objCategoria,
            int posicion) {
        VistaCategoEditar ventana = new VistaCategoEditar(
                princ, pane, anchoFrm, altoFrm, objCategoria, posicion);
        StackPane contenedor = ventana.getMiFormulario();

        ControladorEfecto.aplicarEfecto(contenedor, anchoFrm, altoFrm);

        return contenedor;
    }
    
    public static StackPane carrusel(double anchoFrm, double altoFrm) {
        VistaCategoCarrusel ventana = new VistaCategoCarrusel(anchoFrm, altoFrm);
        StackPane contenedor = ventana.getContenedor();
        ControladorEfecto.aplicarEfecto(contenedor, anchoFrm, altoFrm);
        return contenedor;
    }
}

