package edu.poo.recurso.utilidad;

import edu.poo.recurso.dominio.Ruta;
import edu.poo.recurso.dominio.Configuracion;
import edu.poo.controlador.varios.ControladorSalida;

import java.io.IOException;
import java.io.FileInputStream;

import java.io.InputStream;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Icono {

    public static ImageView obtenerIcono(String nombreIcono, int alto) {
        String rutaIcono = Ruta.RUTA_IMAGENES + nombreIcono;
        InputStream iconoSalirStream = ControladorSalida.class.getResourceAsStream(rutaIcono);

        Image iconoBasico = new Image(iconoSalirStream);
        ImageView iconoMostrar = new ImageView(iconoBasico);
        if (alto != 0) {
            iconoMostrar.setFitHeight(alto);
        }

        iconoMostrar.setPreserveRatio(true);
        iconoMostrar.setSmooth(true);
        return iconoMostrar;
    }

    public static ImageView obtenerFotosExternas(String nombreIcono, int alto) {
        Image imgBasica;
        String iconoSalirRuta;
        ImageView imgMostrar = null;

        iconoSalirRuta = Ruta.RUTA_FOTOS + Configuracion.SEPARADOR_CARPETA + nombreIcono;
        try (FileInputStream archivo = new FileInputStream(iconoSalirRuta)) {
            imgBasica = new Image(archivo);
            imgMostrar = new ImageView(imgBasica);
            if (alto != 0) {
                imgMostrar.setFitHeight(alto);
            }

            imgMostrar.setPreserveRatio(true);
            imgMostrar.setSmooth(true);
        } catch (IOException miError) {
            System.out.println("Error al cargar la foto externa: " + miError.getMessage());
        }

        return imgMostrar;
    }
}
