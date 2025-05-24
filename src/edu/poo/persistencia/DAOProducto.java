package edu.poo.persistencia;

import edu.poo.modelo.Categoria;
import edu.poo.modelo.Producto;
import edu.poo.recurso.dominio.Ruta;
import edu.poo.interfaz.OperacionBD;
import unimag.poo.persistencia.NioFile;
import edu.poo.recurso.dominio.Configuracion;
import edu.poo.controlador.varios.ControladorImagen;
import edu.poo.controlador.categoria.ControladorCategoListar;

import java.io.IOException;
import java.math.BigDecimal;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DAOProducto implements OperacionBD<Producto> {

    private NioFile miArchivo;
    private String nombrePersistencia;

    public DAOProducto() {
        try {
            nombrePersistencia = Ruta.RUTA_PERSISTENCIA + "\\" + "DatosProducto.txt";
            miArchivo = new NioFile(nombrePersistencia);
        } catch (IOException ex) {
            Logger.getLogger(DAOProducto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Producto> SelectFrom() {
        String nombre, cadena, imagenPublica, imagenPrivada;
        int i, desde, limite, cuente, codigo;
        double precio;
        int cantidad, codCategoria;

        List<String> arregloDatos;
        List<Producto> arregloProducto = new ArrayList<>();

        // Obtener todas las categorías, incluso las inactivas, para mostrar los productos
        DAOCategorias daoCategorias = new DAOCategorias();
        List<Categoria> arrCategorias = daoCategorias.SelectFrom();

        arregloDatos = miArchivo.obtenerDatos();
        limite = arregloDatos.size();

        for (i = 0; i < limite; i++) {
            cadena = arregloDatos.get(i).trim();
            try {
                desde = 0;
                cuente = cadena.indexOf(Configuracion.SEPARADOR_COLUMNAS, desde);
                codigo = Integer.parseInt(cadena.substring(desde, cuente).trim());

                desde = cuente + 1;
                cuente = cadena.indexOf(Configuracion.SEPARADOR_COLUMNAS, desde);
                nombre = cadena.substring(desde, cuente).trim();

                desde = cuente + 1;
                cuente = cadena.indexOf(Configuracion.SEPARADOR_COLUMNAS, desde);
                precio = Double.parseDouble(cadena.substring(desde, cuente).trim());

                desde = cuente + 1;
                cuente = cadena.indexOf(Configuracion.SEPARADOR_COLUMNAS, desde);
                cantidad = Integer.parseInt(cadena.substring(desde, cuente).trim());

                desde = cuente + 1;
                cuente = cadena.indexOf(Configuracion.SEPARADOR_COLUMNAS, desde);
                codCategoria = Integer.parseInt(cadena.substring(desde, cuente).trim());

                desde = cuente + 1;
                cuente = cadena.indexOf(Configuracion.SEPARADOR_COLUMNAS, desde);
                imagenPublica = cadena.substring(desde, cuente).trim();

                desde = cuente + 1;
                cuente = cadena.length(); // Último dato va hasta el final
                imagenPrivada = cadena.substring(desde, cuente).trim();

                // Buscar la categoría correspondiente, incluso si está inactiva
                Categoria objCategoria = new Categoria(codCategoria, "Categoría no encontrada", false);
                for (Categoria cate : arrCategorias) {
                    if (cate.getCodCategoria() == codCategoria) {
                        objCategoria = cate;
                        break;
                    }
                }

                Producto objTemporal = new Producto(codigo, nombre, precio, cantidad, objCategoria,
                        imagenPublica, imagenPrivada);
                arregloProducto.add(objTemporal);

            } catch (NumberFormatException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        return arregloProducto;
    }

    @Override
    public boolean insertInto(Producto obj, String ruta) {
        boolean correcto = false;
        BigDecimal precioBonito = new BigDecimal(obj.getPreProducto());
        obj.setNomImgOcuProducto(ControladorImagen.grabarLaImagen(ruta));
        String cadena = obj.getCodProducto() + Configuracion.SEPARADOR_COLUMNAS
                + obj.getNomProducto() + Configuracion.SEPARADOR_COLUMNAS
                + precioBonito + Configuracion.SEPARADOR_COLUMNAS
                + obj.getCanProducto() + Configuracion.SEPARADOR_COLUMNAS
                + obj.getCatProducto().getCodCategoria() + Configuracion.SEPARADOR_COLUMNAS
                + obj.getNomImgPubProducto() + Configuracion.SEPARADOR_COLUMNAS
                + obj.getNomImgOcuProducto();
        if (miArchivo.agregarRegistro(cadena)) {
            correcto = true;
        }
        return correcto;
    }

    @Override
    public int getSerial() {
        int id = 0;
        try {
            id = miArchivo.ultimoCodigo() + 1;
        } catch (IOException ex) {
            Logger.getLogger(DAOProducto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    @Override
    public int numRows() {
        int cantidad = 0;
        try {
            cantidad = miArchivo.cantidadFilas();

        } catch (IOException ex) {
            Logger.getLogger(DAOProducto.class.getName()).log(Level.SEVERE, null, ex);
        }

        return cantidad;

    }

    @Override
    public boolean deleteFrom(int indice) {
        boolean correcto = false;
        List<String> arregloDatos;
        try {
            arregloDatos = miArchivo.borrarFilaPosicion(indice);
            if (!arregloDatos.isEmpty()) {
                String nocu = arregloDatos.get(arregloDatos.size() - 1);
                String nombreBorrar = Ruta.RUTA_FOTOS
                        + Configuracion.SEPARADOR_CARPETA + nocu;
                Path rutaBorrar = Paths.get(nombreBorrar);
                Files.deleteIfExists(rutaBorrar);
                correcto = true;
            }
        } catch (IOException ex) {
            Logger.getLogger(DAOProducto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return correcto;
    }

    @Override
    public boolean updateSet(int indice, Producto obj, String ruta) {
        boolean correcto = false;

        String cadena, nocu;
        List<String> arrDatos;

        cadena = obj.getCodProducto() + Configuracion.SEPARADOR_COLUMNAS
                + obj.getNomProducto() + Configuracion.SEPARADOR_COLUMNAS
                + obj.getPreProducto() + Configuracion.SEPARADOR_COLUMNAS
                + obj.getCanProducto() + Configuracion.SEPARADOR_COLUMNAS
                + obj.getCatProducto().getCodCategoria() + Configuracion.SEPARADOR_COLUMNAS
                + obj.getNomImgPubProducto() + Configuracion.SEPARADOR_COLUMNAS;

        if (ruta.isBlank()) {
            cadena = cadena + obj.getNomImgOcuProducto();
        } else {

            try {
                nocu = ControladorImagen.grabarLaImagen(ruta);
                cadena = cadena + nocu;
                arrDatos = miArchivo.borrarFilaPosicion(indice);
                if (!arrDatos.isEmpty()) {
                    String nomOculto = arrDatos.get(arrDatos.size() - 1);
                    String nombreBorrar = Ruta.RUTA_FOTOS
                            + Configuracion.SEPARADOR_CARPETA + nomOculto;
                    Path rutaBorrar = Paths.get(nombreBorrar);
                    Files.deleteIfExists(rutaBorrar);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        try {
            if (miArchivo.actualizaFilaPosicion(indice, cadena)) {
                correcto = true;
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
        return correcto;
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }    @Override
    public Producto getOne(int indice) {
        DAOCategorias miDaoCatego = new DAOCategorias();
        // Usar SelectFrom() para obtener todas las categorías, incluso las inactivas
        List<Categoria> arregloCategorias = miDaoCatego.SelectFrom();
        Producto objProducto = new Producto();
        try {
            double pre;
            int codP, codC, cantip;
            String nomp, npub, nocu;
            List<String> arrDatos = miArchivo.obtenerFila(indice);

            codP = Integer.parseInt(arrDatos.get(0));
            nomp = arrDatos.get(1);
            pre = Double.parseDouble(arrDatos.get(2));
            cantip = Integer.parseInt(arrDatos.get(3));
            codC = Integer.parseInt(arrDatos.get(4));            npub = arrDatos.get(5);
            nocu = arrDatos.get(6);

            // Buscar la categoría correspondiente
            Categoria objTemporal = new Categoria(codC, "Categoría no encontrada", false);
            for (Categoria cate : arregloCategorias) {
                if (cate.getCodCategoria() == codC) {
                    objTemporal = cate;
                    break;
                }
            }

            objProducto = new Producto(codP, nomp, pre, cantip, objTemporal, npub, nocu);

        } catch (IOException ex) {
            Logger.getLogger(DAOProducto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return objProducto;
    }

    public boolean existenProductosConCategoria(int codCategoria) {
        List<Producto> productos = this.SelectFrom(); // Cargar todos los productos

        for (Producto producto : productos) {
            if (producto.getCatProducto() != null
                    && producto.getCatProducto().getCodCategoria() == codCategoria) {
                return true; // Se encontró al menos un producto con esa categoría
            } 
        }

        return false; // No hay productos con esa categoría
    }

}
