package edu.poo.controlador.categoria;

import edu.poo.modelo.Categoria;
import edu.poo.persistencia.DAOCategorias;
import edu.poo.persistencia.DAOProducto;

public class ControladorCategoEliminar {
    public static boolean eliminar(int posicion) {
        DAOCategorias daoCategoria = new DAOCategorias();
        Categoria categoria = daoCategoria.getOne(posicion);
        
        if (categoria == null) {
            return false;
        }

        DAOProducto daoProducto = new DAOProducto();
        
        // Verifica si hay productos asociados a la categoría
        if (daoProducto.existenProductosConCategoria(categoria.getCodCategoria())) {
            return false; // No se permite eliminar si hay productos asociados
        }

        // Si no hay productos asociados, procede con la eliminación
        return daoCategoria.deleteFrom(posicion);
    }
}
