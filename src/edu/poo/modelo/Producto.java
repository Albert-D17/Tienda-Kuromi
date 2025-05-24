package edu.poo.modelo;

public class Producto {

    private int codProducto;
    private String nomProducto;
    private double preProducto;
    private int canProducto;
    private Categoria catProducto;
    private String nomImgPubProducto;
    private String nomImgOcuProducto;

    public Producto() {
    }

    public Producto(int codProducto, String nomProducto, double preProducto, int canProducto, Categoria catProducto, String nomImgPubProducto, String nomImgOcuProducto) {
        this.codProducto = codProducto;
        this.nomProducto = nomProducto;
        this.preProducto = preProducto;
        this.canProducto = canProducto;
        this.catProducto = catProducto;
        this.nomImgPubProducto = nomImgPubProducto;
        this.nomImgOcuProducto = nomImgOcuProducto;
    }

    public int getCodProducto() {
        return codProducto;
    }

    public void setCodProducto(int codProducto) {
        this.codProducto = codProducto;
    }

    public String getNomProducto() {
        return nomProducto;
    }

    public void setNomProducto(String nomProducto) {
        this.nomProducto = nomProducto;
    }

    public double getPreProducto() {
        return preProducto;
    }

    public void setPreProducto(double preProducto) {
        this.preProducto = preProducto;
    }

    public int getCanProducto() {
        return canProducto;
    }

    public void setCanProducto(int canProducto) {
        this.canProducto = canProducto;
    }

    public Categoria getCatProducto() {
        return catProducto;
    }

    public void setCatProducto(Categoria catProducto) {
        this.catProducto = catProducto;
    }

    public String getNomImgPubProducto() {
        return nomImgPubProducto;
    }

    public void setNomImgPubProducto(String nomImgPubProducto) {
        this.nomImgPubProducto = nomImgPubProducto;
    }

    public String getNomImgOcuProducto() {
        return nomImgOcuProducto;
    }

    public void setNomImgOcuProducto(String nomImgOcuProducto) {
        this.nomImgOcuProducto = nomImgOcuProducto;
    }

    @Override
    public String toString() {
        return "Producto{" + "codProducto=" + codProducto + ", nomProducto=" + nomProducto + ", preProducto=" + preProducto + ", canProducto=" + canProducto + ", catProducto=" + catProducto + ", nomImgPubProducto=" + nomImgPubProducto + ", nomImgOcuProducto=" + nomImgOcuProducto + '}';
    }

}
