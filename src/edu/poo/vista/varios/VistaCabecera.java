package edu.poo.vista.varios;

import javafx.stage.Stage;
import edu.poo.recurso.dominio.Contenedor;
import edu.poo.recurso.dominio.Ruta;
import edu.poo.recurso.utilidad.Icono;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import edu.poo.recurso.dominio.Configuracion;
import edu.poo.controlador.varios.ControladorSalida;
import edu.poo.controlador.categoria.ControladorCategoVentana;
import edu.poo.controlador.producto.ControladorProductoVentana;

public class VistaCabecera extends SubScene {

    private final int menuAncho = 160;
    private final int menuAlto = 35;
    private final int espacioX = 15;

    private Pane miPanelCuerpo;
    private final Stage miEscenario;
    private final HBox miPanelCabecera;
    private final BorderPane miPanelPrincipal;

    public VistaCabecera(
            Stage esce,
            BorderPane prin,
            Pane pane,
            double anchoPanel,
            double altoCabecera) {
        // Inicio constructor
        super(new HBox(), anchoPanel, altoCabecera);
        miPanelCabecera = (HBox) this.getRoot();
        miPanelCabecera.setAlignment(Pos.CENTER_LEFT);

        miPanelPrincipal = prin;
        miPanelCuerpo = pane;
        miEscenario = esce;

        miPanelCabecera.setSpacing(espacioX);
        miPanelCabecera.setPadding(new Insets(0, 30, 0, 30));
        miPanelCabecera.setPrefHeight(Contenedor.ALTO_CABECERA.getValor());
        miPanelCabecera.setStyle(Configuracion.CABECERA_COLOR_FONDO);

        crearOpciones();

    }

    public HBox getMiPanelCabecera() {
        return miPanelCabecera;
    }

    private void crearOpciones() {
        menuCategorias();
        menuProductos();
        menuVentas();
        btnSalir();

        btnAcerca(400, 350);
    }

    private void agregarMenu(MenuButton menu) {
        menu.setCursor(Cursor.HAND);
        menu.setPrefWidth(menuAncho);

        miPanelCabecera.getChildren().add(menu);
    }

    private void menuCategorias() {
        MenuItem opcion1 = new MenuItem("Crear categoria");
        MenuItem opcion2 = new MenuItem("Lista categorias");
        MenuItem opcion3 = new MenuItem("Administar categorias");
        MenuItem opcion4 = new MenuItem("Carrusel");

        opcion1.setOnAction((ActionEvent event) -> {
            miPanelCuerpo = ControladorCategoVentana.crear(Configuracion.ANCHO_APP, Contenedor.ALTO_CUERPO.getValor());
            miPanelPrincipal.setCenter(null);
            miPanelPrincipal.setCenter(miPanelCuerpo);
        });

        opcion2.setOnAction((ActionEvent event) -> {
            miPanelCuerpo = ControladorCategoVentana.listar(Configuracion.ANCHO_APP,
                    Contenedor.ALTO_CUERPO.getValor());
            miPanelPrincipal.setCenter(null);
            miPanelPrincipal.setCenter(miPanelCuerpo);
        });
        
        opcion3.setOnAction((ActionEvent event) -> {
           miPanelCuerpo = ControladorCategoVentana.administrar(
                    miPanelPrincipal, miPanelCuerpo,
                    Configuracion.ANCHO_APP,
                    Contenedor.ALTO_CUERPO.getValor());
            miPanelPrincipal.setCenter(null);
            miPanelPrincipal.setCenter(miPanelCuerpo);
        });
        
        opcion4.setOnAction((ActionEvent event) -> {
           miPanelCuerpo = ControladorCategoVentana.carrusel(
                    Configuracion.ANCHO_APP,
                    Contenedor.ALTO_CUERPO.getValor());
            miPanelPrincipal.setCenter(null);
            miPanelPrincipal.setCenter(miPanelCuerpo);
        });
        
        

        MenuButton menuButton = new MenuButton("Categorias");
        menuButton.setPrefWidth(menuAncho);
        menuButton.setPrefWidth(menuAlto);
        menuButton.setGraphic(Icono.obtenerIcono("iconoCategorias.png", 25));
        menuButton.getItems().addAll(opcion1, opcion2, opcion3, opcion4);

        agregarMenu(menuButton);
    }

    private void menuProductos() {
        MenuItem opcion1 = new MenuItem("Crear productos");
        MenuItem opcion2 = new MenuItem("Listar productos");
        MenuItem opcion3 = new MenuItem("Administrar productos");

        opcion1.setOnAction((ActionEvent event) -> {
            miPanelCuerpo = ControladorProductoVentana.crear(Configuracion.ANCHO_APP,
                    Contenedor.ALTO_CUERPO.getValor());
            miPanelPrincipal.setCenter(null);
            miPanelPrincipal.setCenter(miPanelCuerpo);
        });

        opcion2.setOnAction((ActionEvent event) -> {
            miPanelCuerpo = ControladorProductoVentana.listar(Configuracion.ANCHO_APP,
                    Contenedor.ALTO_CUERPO.getValor());
            miPanelPrincipal.setCenter(null);
            miPanelPrincipal.setCenter(miPanelCuerpo);
        });

        opcion3.setOnAction((ActionEvent event) -> {
            miPanelCuerpo = ControladorProductoVentana.administrar(
                    miPanelPrincipal, miPanelCuerpo,
                    Configuracion.ANCHO_APP,
                    Contenedor.ALTO_CUERPO.getValor());
            miPanelPrincipal.setCenter(null);
            miPanelPrincipal.setCenter(miPanelCuerpo);
        });

        MenuButton menuButton = new MenuButton("Productos");
        menuButton.setPrefWidth(menuAncho);
        menuButton.setPrefWidth(menuAlto);
        menuButton.setGraphic(Icono.obtenerIcono("iconoProductos.png", 25));
        menuButton.getItems().addAll(opcion1, opcion2, opcion3);

        agregarMenu(menuButton);
    }

    private void menuVentas() {
        MenuItem opcion1 = new MenuItem("Venta de productos");
        MenuItem opcion2 = new MenuItem("Listar ventas");
        MenuItem opcion3 = new MenuItem("Detalle ventas");

        MenuButton menuButton = new MenuButton("Ventas");
        menuButton.setPrefWidth(menuAncho);
        menuButton.setPrefWidth(menuAlto);
        menuButton.setGraphic(Icono.obtenerIcono("iconoVentas.png", 25));
        menuButton.getItems().addAll(opcion1, opcion2, opcion3);

        agregarMenu(menuButton);
    }

    private void btnSalir() {
        Button btnSalir = new Button("Salir");
        btnSalir.setCursor(Cursor.HAND);
        btnSalir.setPrefWidth(menuAncho);
        btnSalir.setPrefHeight(menuAlto);

        btnSalir.setOnAction((ActionEvent event) -> {
            event.consume();
            ControladorSalida.verificar(miEscenario);
        });

        miPanelCabecera.getChildren().add(btnSalir);
    }

    private void btnAcerca(double anchoFlotante, double altoFlotante) {
        Button botonAyuda = new Button("?");
        botonAyuda.setOnAction((ActionEvent event) -> {
            VistaAcerca.mostrar(anchoFlotante, altoFlotante);
        });

        botonAyuda.setPrefWidth(30);
        botonAyuda.setId("btn-ayuda");
        botonAyuda.setCursor(Cursor.HAND);
        botonAyuda.getStylesheets().add(getClass().getResource(Ruta.RUTA_ESTILO_BTN_ACERCA).toExternalForm());

        Region espacio = new Region();
        HBox.setHgrow(espacio, Priority.ALWAYS);

        miPanelCabecera.getChildren().add(espacio);
        miPanelCabecera.getChildren().add(botonAyuda);
    }

}
