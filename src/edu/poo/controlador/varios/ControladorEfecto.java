package edu.poo.controlador.varios;

import edu.poo.recurso.utilidad.Aleatorio;
import edu.poo.recurso.utilidad.Efecto;
import javafx.scene.layout.Pane;

public class ControladorEfecto {

    public static void aplicarEfecto(Pane contenedor,
            double anchoFrm, double altoFrm) {

        int opcion = Aleatorio.entero(1, 11);
        switch (opcion) {
            case 1 -> {
                contenedor.setTranslateX(anchoFrm- (anchoFrm * 0.2));
                Efecto.transicionX(contenedor, 1.0);
            }
            case 2 -> {
                contenedor.setTranslateY(altoFrm - (altoFrm * 0.2));
                Efecto.transicionY(contenedor, 1.0);
            }
            case 3 -> {
                Efecto.crecer(contenedor, 1.0);
            }
            case 4 -> {
                Efecto.rotar(contenedor, 1.0);
            }
            case 5 -> {
                Efecto.desvanecer(contenedor, 1.0);
            }
            case 6 -> {
                Efecto.rebote(contenedor, 1.0);
            }
            case 7 -> {
                Efecto.latido(contenedor, 1.0);
            }
            case 8 -> {
                Efecto.desenfoque(contenedor, 1.0);
            }
            case 9 -> {
                Efecto.deslizarDiagonal(contenedor, 1.0, anchoFrm, altoFrm);
            }
            case 10 -> {
                Efecto.parpadear(contenedor, 1.0);
            }
            case 11 -> {
                Efecto.sacudir(contenedor, 1.0);
            }

        }
    }
}
