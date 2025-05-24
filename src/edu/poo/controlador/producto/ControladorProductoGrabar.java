package edu.poo.controlador.producto;

import edu.poo.modelo.Categoria;
import edu.poo.modelo.Producto;
import edu.poo.persistencia.DAOProducto;

public class ControladorProductoGrabar {
     public static boolean grabar(String nomb, double prec, int cant, 
             Categoria cate, String imag, String ruta) {
        boolean correcto = false;
         DAOProducto miDAO = new DAOProducto();
        int codiguito = miDAO.getSerial();

         Producto miObj = new Producto(codiguito, nomb, prec, cant, cate, imag,"" );
        if (miDAO.insertInto(miObj, ruta)) {
            correcto = true;
        }
        return correcto;
    }
    
}
