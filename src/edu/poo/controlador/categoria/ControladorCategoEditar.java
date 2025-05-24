package edu.poo.controlador.categoria;

import edu.poo.modelo.Categoria;
import edu.poo.persistencia.DAOCategorias;

public class ControladorCategoEditar {

    public static boolean actualizar(int indice, Categoria categoriaEditada, String rutaNuevaImagen) {
        DAOCategorias dao = new DAOCategorias();
        // El método updateSet en DAOCategorias se encargará de la lógica de la imagen
        // (borrar antigua, guardar nueva, actualizar nomImgOcuCategoria en el objeto si es necesario).
        return dao.updateSet(indice, categoriaEditada, rutaNuevaImagen);
    }
}