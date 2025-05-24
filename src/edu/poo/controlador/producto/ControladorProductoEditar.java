package edu.poo.controlador.producto;

import edu.poo.modelo.Producto;
import edu.poo.persistencia.DAOProducto;

public class ControladorProductoEditar {

    public static boolean actualizar(int indiceExterno, Producto objExterno, String rutaImagen) {

        boolean correcto;

        DAOProducto miDao = new DAOProducto();

        correcto = miDao.updateSet(indiceExterno, objExterno, rutaImagen);

        return correcto;

    }

}
