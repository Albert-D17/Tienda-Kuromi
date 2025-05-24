package edu.poo.vista.varios;

import edu.poo.recurso.dominio.Configuracion;
import edu.poo.recurso.dominio.Contenedor;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;

public class VistaAdmin {

    private final Scene miEscena;
    private final Stage miEscenario;

    private final Pane miPanelCuerpo;
    private final HBox miPanelCabecera;
    private final BorderPane miPanelPrincial;

    public VistaAdmin() {
        miEscenario = new Stage();
        miPanelCuerpo = new Pane();
        miPanelPrincial = new BorderPane();

        VistaCabecera cabeceraVista = new VistaCabecera(miEscenario, miPanelPrincial, miPanelCuerpo,
                Configuracion.ANCHO_APP, Contenedor.ALTO_CABECERA.getValor());

        miPanelCabecera = cabeceraVista.getMiPanelCabecera();
        miPanelCuerpo.setPrefHeight(Contenedor.ALTO_CABECERA.getValor());

        miPanelPrincial.setTop(miPanelCabecera);
        miPanelPrincial.setCenter(miPanelCuerpo);

        miEscena = new Scene(miPanelPrincial,
                Configuracion.ANCHO_APP, Configuracion.ALTO_APP);
        miEscenario.setScene(miEscena);
    }

    public Stage getMiEscenario() {
        return miEscenario;
    }
}
