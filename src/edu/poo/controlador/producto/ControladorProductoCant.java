package edu.poo.controlador.producto;

import edu.poo.persistencia.DAOProducto;

public class ControladorProductoCant {

    public static int obtenerCantidadPro() {
        DAOProducto miDao = new DAOProducto();
        return miDao.numRows();
    }

}
