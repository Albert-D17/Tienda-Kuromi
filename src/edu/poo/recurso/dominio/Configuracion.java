package edu.poo.recurso.dominio;

import java.io.File;
import javafx.stage.Screen;
import javafx.scene.paint.Stop;
import javafx.scene.paint.Color;

public class Configuracion {

    //Ventan Principal APP
    public final static int ANCHO_APP = (int) (Screen.getPrimary().getBounds().getWidth() * 0.7);
    public final static int ALTO_APP = (int) (Screen.getPrimary().getBounds().getHeight() * 0.8);
    public final static double MARCO_ANCHO_PORCENTAJE = 0.85; 
    public final static double MARCO_ALTO_PORCENTAJE = 0.93;  

    //Persistencia
    public final static String SEPARADOR_COLUMNAS = ";";
    public final static String SEPARADOR_CARPETA = File.separator;
    public final static String SEPARADOR_NOMBRE = "datosBaseTiendaKuromi2.txt";

    //Panel Cabecera
    public final static double CABECERA_ALTO_PORCENTAJE = 0.1;
    public final static String CABECERA_COLOR_FONDO = "-fx-background-color: #C3A7E8;"
            + "-fx-background-color:linear-gradient(#2E2E2E,#C3A7E8, #F7D6E0); ";

    //Degradee Kuromi
    public final static String DEGRADE_BORDE = "#C0C0C0";
    public final static Stop[] DEGRADE_ARREGLO = new Stop[]{
        new Stop(0, Color.web("#2E2E2E")),
        new Stop(0.5, Color.web("#C3A7E8")),
        new Stop(1, Color.web("#F7D6E0"))};

    // Fondos
    public final static String FONDOS[] = {
        "fondo01.png",
        "fondo02.png",
        "fondo03.png",
        "fondo04.png",
        "fondo05.png",
        "fondo06.png"
    };

    //Imagen para el efecto de pantalla rota
    public final static String PANTALLA_ROTA = "pantallaRota.png";

}
