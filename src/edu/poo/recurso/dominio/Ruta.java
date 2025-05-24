package edu.poo.recurso.dominio;

public class Ruta {

    private final static String RECURSO = "/edu/poo/recurso/";

    //Rutas principal
    public final static String RUTA_PROYECTO = System.getProperty("user.dir");
    public final static String RUTA_USUARIO = System.getProperty("user.home");
    public final static String RUTA_IMAGENES = "/edu/poo/recurso/imagen/";

    //Rutas estilos 
    public final static String RUTA_ESTILO_BTN_ACERCA = RECURSO + "estilo/BtnAcerca.css";
    public final static String RUTA_ESTILO_TEXTOTAREA_DESCRIPCION = RECURSO + "estilo/ProductoCrear.css";

    //Rutas persistencia
    public final static String RUTA_PERSISTENCIA = RUTA_PROYECTO + Configuracion.SEPARADOR_CARPETA + "baseDatosKuromi2";
    public final static String RUTA_FOTOS = RUTA_PERSISTENCIA + Configuracion.SEPARADOR_CARPETA + "fotosExternas";

}
