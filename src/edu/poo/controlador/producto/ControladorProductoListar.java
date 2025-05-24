package edu.poo.controlador.producto;

import edu.poo.modelo.Producto;
import edu.poo.persistencia.DAOProducto;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ControladorProductoListar {

    public static ObservableList<Producto> cargarDatos() {
        DAOProducto miDAO = new DAOProducto();
        List<Producto> arreglo = miDAO.SelectFrom();

        ObservableList<Producto> datosTabla
                = FXCollections.observableArrayList(arreglo);
        return datosTabla;
    }

}
