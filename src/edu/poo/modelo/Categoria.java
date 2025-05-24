package edu.poo.modelo;

public class Categoria {

    private int codCategoria;
    private String nombreCategoria;
    private boolean estadoCategoria;
    private String nomImgPubCategoria; // Nuevo: Nombre público de la imagen
    private String nomImgOcuCategoria; // Nuevo: Nombre oculto/privado de la imagen

    public Categoria() {
        // Inicializar nombres de imagen a cadena vacía o null si se prefiere
        this.nomImgPubCategoria = "";
        this.nomImgOcuCategoria = "";
    }

    // Constructor existente (lo adaptamos o puedes tener múltiples constructores)
    public Categoria(int codCategoria, String nombreCategoria, boolean estadoCategoria) {
        this.codCategoria = codCategoria;
        this.nombreCategoria = nombreCategoria;
        this.estadoCategoria = estadoCategoria;
        this.nomImgPubCategoria = ""; // Por defecto vacío
        this.nomImgOcuCategoria = ""; // Por defecto vacío
    }

    // Constructor completo (opcional, pero útil)
    public Categoria(int codCategoria, String nombreCategoria, boolean estadoCategoria, String nomImgPubCategoria, String nomImgOcuCategoria) {
        this.codCategoria = codCategoria;
        this.nombreCategoria = nombreCategoria;
        this.estadoCategoria = estadoCategoria;
        this.nomImgPubCategoria = nomImgPubCategoria;
        this.nomImgOcuCategoria = nomImgOcuCategoria;
    }

    public int getCodCategoria() {
        return codCategoria;
    }

    public void setCodCategoria(int codCategoria) {
        this.codCategoria = codCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public boolean isEstadoCategoria() {
        return estadoCategoria;
    }

    public void setEstadoCategoria(boolean estadoCategoria) {
        this.estadoCategoria = estadoCategoria;
    }

    // Getters y Setters para los nuevos atributos de imagen
    public String getNomImgPubCategoria() {
        return nomImgPubCategoria;
    }

    public void setNomImgPubCategoria(String nomImgPubCategoria) {
        this.nomImgPubCategoria = nomImgPubCategoria;
    }

    public String getNomImgOcuCategoria() {
        return nomImgOcuCategoria;
    }

    public void setNomImgOcuCategoria(String nomImgOcuCategoria) {
        this.nomImgOcuCategoria = nomImgOcuCategoria;
    }

    @Override
    public String toString() {
        return "Categoria{" +
               "codCategoria=" + codCategoria +
               ", nombreCategoria='" + nombreCategoria + '\'' +
               ", estadoCategoria=" + estadoCategoria +
               ", nomImgPubCategoria='" + nomImgPubCategoria + '\'' +
               ", nomImgOcuCategoria='" + nomImgOcuCategoria + '\'' +
               '}';
    }
}