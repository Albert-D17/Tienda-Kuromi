package edu.poo.controlador.producto;

import edu.poo.persistencia.DAOProducto;

public class ControladorProductoEliminar {

    public static boolean eliminar(int indice) {
        boolean correcto;
        DAOProducto miDaoProducto = new DAOProducto();

        correcto = miDaoProducto.deleteFrom(indice);

        return correcto;

    }
}
