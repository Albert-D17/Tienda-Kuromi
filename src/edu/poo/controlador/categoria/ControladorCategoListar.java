package edu.poo.controlador.categoria;

import edu.poo.controlador.producto.ControladorProductoListar;
import java.util.List;

import edu.poo.modelo.Categoria;
import edu.poo.modelo.Producto;
import edu.poo.persistencia.DAOCategorias;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ControladorCategoListar {

    public static ObservableList<Categoria> cargarDatos() {
        DAOCategorias miDAO = new DAOCategorias();
        List<Categoria> arreglo = miDAO.SelectFrom();

        ObservableList<Categoria> datosTabla = FXCollections.observableArrayList(arreglo);
        return datosTabla;
    }    public static List<Categoria> arregloCategoria() {
        DAOCategorias miDAO = new DAOCategorias();
        List<Categoria> todasCategorias = miDAO.SelectFrom();
        // Filtrar solo las categorías activas
        List<Categoria> categoriasActivas = new ArrayList<>();
        for (Categoria cat : todasCategorias) {
            if (cat.isEstadoCategoria()) {
                categoriasActivas.add(cat);
            }
        }
        return categoriasActivas;
    }

    
    //*************************************************************************
    public static int obtenerCantidadProductosParaCategoria(Categoria categoria) {
        if (categoria == null) {
            return 0;
        }

        // Obtener todos los productos.
        // Asumimos que ControladorProductoListar.cargarDatos() devuelve la lista actualizada de productos.
        ObservableList<Producto> todosLosProductos = ControladorProductoListar.cargarDatos();

        if (todosLosProductos == null) {
            System.err.println("Advertencia: ControladorProductoListar.cargarDatos() devolvió null.");
            return 0;
        }

        int contador = 0;
        for (Producto producto : todosLosProductos) {
            // Comprueba si el producto pertenece a la categoría dada.
            // Asegúrate que producto.getCatProducto() y categoria.getCodCategoria() son los métodos correctos.
            if (producto.getCatProducto() != null
                    && producto.getCatProducto().getCodCategoria() == categoria.getCodCategoria()) {
                contador++;
            }
        }
        return contador;
    }
    
    
    
    
}
