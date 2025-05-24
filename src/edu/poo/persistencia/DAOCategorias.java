package edu.poo.persistencia;

import edu.poo.controlador.varios.ControladorImagen; // Necesario para grabar y potencialmente borrar imágenes
import edu.poo.modelo.Categoria;
import edu.poo.interfaz.OperacionBD;
import edu.poo.recurso.dominio.Configuracion;
import edu.poo.recurso.dominio.Ruta; // Asegúrate que esta clase tiene RUTA_FOTOS_CATEGORIAS_O_SIMILAR
import unimag.poo.persistencia.NioFile;

import java.io.IOException;
import java.nio.file.Files; // Para borrar archivos
import java.nio.file.Path;   // Para borrar archivos
import java.nio.file.Paths;  // Para borrar archivos
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DAOCategorias implements OperacionBD<Categoria> {

    private NioFile miArchivo;
    private String nombrePersistencia;
    // Define la ruta donde se guardan las imágenes de las categorías.
    // Puede ser la misma que la de productos o una específica. ej: Ruta.RUTA_FOTOS + "/categorias"
    // O una nueva constante en tu clase Ruta: Ruta.RUTA_FOTOS_CATEGORIAS
    private final String RUTA_BASE_IMAGENES_CATEGORIA = Ruta.RUTA_FOTOS; // ¡AJUSTA ESTO SI ES NECESARIO!

    public DAOCategorias() {
        nombrePersistencia = Ruta.RUTA_PERSISTENCIA + Configuracion.SEPARADOR_CARPETA + "DatosCategoria.txt";
        try {
            miArchivo = new NioFile(nombrePersistencia);
        } catch (IOException ex) {
            Logger.getLogger(DAOCategorias.class.getName()).log(Level.SEVERE, "Error inicializando NioFile para " + nombrePersistencia, ex);
        }
    }

    @Override
    public List<Categoria> SelectFrom() {
        List<String> arregloDatos;
        List<Categoria> arregloCategoria = new ArrayList<>();

        if (miArchivo == null) {
            Logger.getLogger(DAOCategorias.class.getName()).log(Level.SEVERE, "miArchivo es null en SelectFrom. No se pueden leer datos.");
            return arregloCategoria; // Devuelve lista vacía
        }
        arregloDatos = miArchivo.obtenerDatos();

        for (String cadena : arregloDatos) {
            if (cadena == null || cadena.trim().isEmpty()) continue; // Omitir líneas vacías

            String[] partes = cadena.split(Configuracion.SEPARADOR_COLUMNAS, -1); // -1 para incluir campos vacíos al final

            if (partes.length < 3) { // Mínimo código, nombre, estado
                Logger.getLogger(DAOCategorias.class.getName()).log(Level.WARNING, "Línea con formato incorrecto (menos de 3 campos): " + cadena);
                continue;
            }
            try {
                int codigo = Integer.parseInt(partes[0].trim());
                String nombre = partes[1].trim();
                boolean estado = Boolean.parseBoolean(partes[2].trim());
                
                String nomImgPub = (partes.length > 3) ? partes[3].trim() : "";
                String nomImgOcu = (partes.length > 4) ? partes[4].trim() : "";

                Categoria objTemporal = new Categoria(codigo, nombre, estado, nomImgPub, nomImgOcu);
                arregloCategoria.add(objTemporal);
            } catch (NumberFormatException e) {
                Logger.getLogger(DAOCategorias.class.getName()).log(Level.WARNING, "Error de formato numérico en línea: " + cadena, e);
            }
        }
        return arregloCategoria;
    }

    // SelectFrom2 modificado de manera similar a SelectFrom
    public List<Categoria> SelectFrom2() { // Carga solo las activas
        List<Categoria> todasLasCategorias = SelectFrom();
        List<Categoria> arregloCategoriaActivas = new ArrayList<>();
        for (Categoria cat : todasLasCategorias) {
            if (cat.isEstadoCategoria()) {
                arregloCategoriaActivas.add(cat);
            }
        }
        return arregloCategoriaActivas;
    }

    @Override
    public boolean insertInto(Categoria obj, String rutaAbsolutaImg) {
        if (miArchivo == null) {
             Logger.getLogger(DAOCategorias.class.getName()).log(Level.SEVERE, "miArchivo es null en insertInto. No se puede guardar.");
            return false;
        }
        boolean correcto = false;
        String nombreOcultoImagenFinal = "";

        if (rutaAbsolutaImg != null && !rutaAbsolutaImg.isBlank()) {
            // Grabar la imagen y obtener el nombre oculto
            String nombreOcultoGenerado = ControladorImagen.grabarLaImagen(rutaAbsolutaImg); // Asegúrate que esta función usa RUTA_BASE_IMAGENES_CATEGORIA implicitamente o pásala como parámetro.
            
            if (nombreOcultoGenerado != null && !nombreOcultoGenerado.isBlank()) {
                nombreOcultoImagenFinal = nombreOcultoGenerado;
                // El nombre público ya debe estar en obj.getNomImgPubCategoria() desde el controlador
            } else {
                // Falló la grabación de la imagen, no guardar nombres de imagen
                obj.setNomImgPubCategoria(""); // Limpiar por si acaso
                Logger.getLogger(DAOCategorias.class.getName()).log(Level.WARNING, "Fallo al grabar imagen desde: " + rutaAbsolutaImg + ". Se guardará categoría sin imagen.");
            }
        } else {
             // No se proporcionó ruta de imagen, asegurar que no haya nombre público si no hay ruta
            if (obj.getNomImgPubCategoria() != null && !obj.getNomImgPubCategoria().isBlank()){
                 //Esto no debería pasar si la UI está bien: nombre público pero no ruta. Se asume no imagen.
                Logger.getLogger(DAOCategorias.class.getName()).log(Level.INFO, "Nombre público de imagen presente pero sin ruta de archivo. Se guardará categoría sin imagen.");
                obj.setNomImgPubCategoria("");
            }
        }
        obj.setNomImgOcuCategoria(nombreOcultoImagenFinal); // Asignar el nombre oculto (vacío si no hubo imagen)


        String cadena = obj.getCodCategoria() + Configuracion.SEPARADOR_COLUMNAS
                + obj.getNombreCategoria() + Configuracion.SEPARADOR_COLUMNAS
                + obj.isEstadoCategoria() + Configuracion.SEPARADOR_COLUMNAS
                + (obj.getNomImgPubCategoria() != null ? obj.getNomImgPubCategoria() : "") + Configuracion.SEPARADOR_COLUMNAS
                + (obj.getNomImgOcuCategoria() != null ? obj.getNomImgOcuCategoria() : ""); // Último campo sin separador al final

        if (miArchivo.agregarRegistro(cadena)) {
            correcto = true;
        }
        return correcto;
    }

    @Override
    public int getSerial() {
        if (miArchivo == null) return 1; // o manejo de error
        int id = 0;
        try {
            id = miArchivo.ultimoCodigo() + 1;
        } catch (IOException ex) {
            Logger.getLogger(DAOCategorias.class.getName()).log(Level.SEVERE, null, ex);
            try { // Intento alternativo si ultimoCodigo falla y el archivo está vacío
                if(miArchivo.cantidadFilas() == 0) return 1;
            } catch (IOException ex2) {
                 Logger.getLogger(DAOCategorias.class.getName()).log(Level.SEVERE, null, ex2);
            }
        }
        return (id == 0) ? 1 : id; // Asegurar que el primer ID sea al menos 1
    }

    @Override
    public int numRows() {
        if (miArchivo == null) return 0;
        try {
            return miArchivo.cantidadFilas();
        } catch (IOException ex) {
            Logger.getLogger(DAOCategorias.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    private void borrarArchivoImagen(String nombreOcultoImagen) {
        if (nombreOcultoImagen != null && !nombreOcultoImagen.isBlank()) {
            try {
                Path rutaImagenABorrar = Paths.get(RUTA_BASE_IMAGENES_CATEGORIA + Configuracion.SEPARADOR_CARPETA + nombreOcultoImagen);
                Files.deleteIfExists(rutaImagenABorrar);
                Logger.getLogger(DAOCategorias.class.getName()).log(Level.INFO, "Imagen borrada: " + rutaImagenABorrar.toString());
            } catch (IOException ex) {
                Logger.getLogger(DAOCategorias.class.getName()).log(Level.SEVERE, "Error al borrar archivo de imagen: " + nombreOcultoImagen, ex);
            }
        }
    }

    @Override
    public boolean deleteFrom(int indice) {
        if (miArchivo == null) return false;
        // Paso 1: Obtener la información de la categoría ANTES de borrarla del archivo de texto
        Categoria categoriaAEliminar = getOne(indice); // Usar getOne para leer la fila
        boolean correcto = false;

        if (categoriaAEliminar != null) {
            try {
                // Paso 2: Intentar borrar la fila del archivo de texto
                // Asumo que tu NioFile.borrarFilaPosicion(indice) borra la fila y devuelve un booleano o una lista.
                // Si no devuelve la data, es por eso que usamos getOne antes.
                // Vamos a asumir que `borrarFilaPosicion` simplemente borra y devuelve `true` o `false`.
                // Si `miArchivo.borrarFilaPosicion` es el método que realmente borra la línea:
                 List<String> datosFilaBorrada = miArchivo.borrarFilaPosicion(indice); //Si este es el metodo que borra del archivo
                 if (datosFilaBorrada != null && !datosFilaBorrada.isEmpty()) { // O si solo devuelve boolean: if(miArchivo.borrarLaLineaEn(indice))
                    correcto = true;
                 }

            } catch (IOException ex) {
                 Logger.getLogger(DAOCategorias.class.getName()).log(Level.SEVERE, "Error al borrar fila del archivo en deleteFrom", ex);
                 return false; // No continuar si la fila no se pudo borrar
            }

            // Paso 3: Si la fila se borró del archivo de texto, borrar el archivo de imagen asociado
            if (correcto) {
                borrarArchivoImagen(categoriaAEliminar.getNomImgOcuCategoria());
            }
        } else {
             Logger.getLogger(DAOCategorias.class.getName()).log(Level.WARNING, "No se encontró categoría en índice: " + indice + " para borrar.");
        }
        return correcto;
    }

    @Override
    public boolean updateSet(int indice, Categoria objNuevo, String rutaAbsolutaImgNueva) {
        if (miArchivo == null) return false;
        boolean correcto = false;

        // 1. Obtener datos antiguos de la categoría, especialmente el nombre de la imagen oculta
        Categoria objAntiguo = getOne(indice);
        if (objAntiguo == null) {
            Logger.getLogger(DAOCategorias.class.getName()).log(Level.WARNING, "No se encontró categoría en índice: " + indice + " para actualizar.");
            return false;
        }

        String nomImgOcuFinal = objAntiguo.getNomImgOcuCategoria(); // Por defecto, mantener la antigua imagen oculta
        String nomImgPubFinal = objNuevo.getNomImgPubCategoria(); // Nombre público viene del objeto nuevo (UI)

        // 2. Manejar la nueva imagen si se proporcionó una
        if (rutaAbsolutaImgNueva != null && !rutaAbsolutaImgNueva.isBlank()) {
            // Se proporcionó una nueva imagen
            String nuevoNomImgOculto = ControladorImagen.grabarLaImagen(rutaAbsolutaImgNueva);
            if (nuevoNomImgOculto != null && !nuevoNomImgOculto.isBlank()) {
                // Nueva imagen guardada exitosamente, borrar la antigua si existía
                if (!objAntiguo.getNomImgOcuCategoria().equals(nuevoNomImgOculto)) { // Solo borrar si es diferente
                    borrarArchivoImagen(objAntiguo.getNomImgOcuCategoria());
                }
                nomImgOcuFinal = nuevoNomImgOculto;
                // El nombre público ya está en objNuevo.getNomImgPubCategoria()
            } else {
                // Falló la grabación de la nueva imagen. ¿Qué hacer?
                // Opción A: Mantener la imagen antigua (no cambiar nomImgOcuFinal ni nomImgPubFinal del antiguo)
                // Opción B: Proceder sin imagen (limpiar nomImgPubFinal y nomImgOcuFinal)
                // Opción C: Fallar la actualización (return false)
                Logger.getLogger(DAOCategorias.class.getName()).log(Level.WARNING, "Fallo al grabar nueva imagen para actualización. Se mantendrá la imagen anterior si existe o ninguna.");
                // Para mantener la antigua, nomImgPubFinal también debería ser el antiguo si la UI no lo borró
                nomImgPubFinal = objAntiguo.getNomImgPubCategoria(); // Revertir público al antiguo si nueva imagen falló
                // nomImgOcuFinal ya tiene el antiguo
            }
        } else {
            // No se proporcionó una nueva ruta de imagen.
            // Verificar si el nombre público fue borrado en la UI (implica eliminar imagen existente)
            if (nomImgPubFinal == null || nomImgPubFinal.isBlank()) {
                if (objAntiguo.getNomImgPubCategoria() != null && !objAntiguo.getNomImgPubCategoria().isBlank()){
                    // El nombre público fue borrado, así que borrar la imagen física antigua y el nombre oculto
                    borrarArchivoImagen(objAntiguo.getNomImgOcuCategoria());
                    nomImgOcuFinal = "";
                }
            }
            // Si nomImgPubFinal tiene un valor, se asume que es el nombre de la imagen existente que se quiere conservar
            // y nomImgOcuFinal ya tiene el valor correcto de objAntiguo.
        }
        
        // Actualizar el objeto 'objNuevo' con los nombres de imagen finales
        objNuevo.setNomImgPubCategoria(nomImgPubFinal);
        objNuevo.setNomImgOcuCategoria(nomImgOcuFinal);


        // 3. Construir la cadena para el archivo
        String cadenaActualizada = objNuevo.getCodCategoria() + Configuracion.SEPARADOR_COLUMNAS
                + objNuevo.getNombreCategoria() + Configuracion.SEPARADOR_COLUMNAS
                + objNuevo.isEstadoCategoria() + Configuracion.SEPARADOR_COLUMNAS
                + (objNuevo.getNomImgPubCategoria() != null ? objNuevo.getNomImgPubCategoria() : "") + Configuracion.SEPARADOR_COLUMNAS
                + (objNuevo.getNomImgOcuCategoria() != null ? objNuevo.getNomImgOcuCategoria() : "");

        // 4. Actualizar la fila en el archivo
        try {
            if (miArchivo.actualizaFilaPosicion(indice, cadenaActualizada)) {
                correcto = true;
            }
        } catch (IOException ex) {
            Logger.getLogger(DAOCategorias.class.getName()).log(Level.SEVERE, "Error al actualizar fila en el archivo.", ex);
        }
        return correcto;
    }

    @Override
    public Categoria getOne(int indice) {
        if (miArchivo == null) return null;
        List<String> partesFila;
        try {
            partesFila = miArchivo.obtenerFila(indice); // Asumo que esto devuelve las partes de la fila como List<String>
            if (partesFila == null || partesFila.size() < 3) { // Mínimo código, nombre, estado
                Logger.getLogger(DAOCategorias.class.getName()).log(Level.WARNING, "No se pudo obtener la fila o formato incorrecto en índice: " + indice);
                return null;
            }

            int codigo = Integer.parseInt(partesFila.get(0).trim());
            String nombre = partesFila.get(1).trim();
            boolean estado = Boolean.parseBoolean(partesFila.get(2).trim());
            
            String nomImgPub = (partesFila.size() > 3) ? partesFila.get(3).trim() : "";
            String nomImgOcu = (partesFila.size() > 4) ? partesFila.get(4).trim() : "";

            return new Categoria(codigo, nombre, estado, nomImgPub, nomImgOcu);

        } catch (IOException | NumberFormatException | IndexOutOfBoundsException ex) {
            Logger.getLogger(DAOCategorias.class.getName()).log(Level.SEVERE, "Error al obtener/parsear categoría en índice: " + indice, ex);
            return null;
        }
    }
}