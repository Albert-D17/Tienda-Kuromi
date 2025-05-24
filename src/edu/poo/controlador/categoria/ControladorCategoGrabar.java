package edu.poo.controlador.categoria;

import edu.poo.modelo.Categoria;
import edu.poo.persistencia.DAOCategorias;

public class ControladorCategoGrabar {

     public static boolean grabar(String nom, Boolean est, String nomImgPub, String rutaAbsolutaImg) {
        boolean correcto = false;
        DAOCategorias miDAO = new DAOCategorias();
        int codiguito = miDAO.getSerial();

        Categoria miObj = new Categoria(codiguito, nom, est);
        miObj.setNomImgPubCategoria(nomImgPub);

        if (miDAO.insertInto(miObj, rutaAbsolutaImg)) {
            correcto = true;
        }
        return correcto;
    }
}
