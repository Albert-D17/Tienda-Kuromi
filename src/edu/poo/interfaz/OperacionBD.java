package edu.poo.interfaz;

import java.util.List;

public interface OperacionBD<T> {

    public List<T> SelectFrom();

    public boolean insertInto(T obj, String ruta);

    public int getSerial();

    public int numRows();

    public boolean deleteFrom(int indice);

    public boolean updateSet(int indice, T obj, String ruta);

    public T getOne(int indice);

}
