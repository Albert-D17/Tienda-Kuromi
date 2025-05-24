package edu.poo.aplicacion;

import edu.poo.recurso.dominio.Ruta;
import edu.poo.recurso.dominio.IconoNombre;
import edu.poo.controlador.varios.ControladorSalida;
import edu.poo.vista.varios.VistaAdmin;

import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.application.Application;
import static javafx.application.Application.launch;

public class Principal extends Application {

    private VistaAdmin adminVista;

    public Principal() {
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        String iconoApp = Ruta.RUTA_IMAGENES + IconoNombre.ICONO_APP;
        Image miImagen = new Image(getClass().getResourceAsStream(iconoApp));
        adminVista = new VistaAdmin();
        stage = adminVista.getMiEscenario();
        stage.setTitle("Tienda Kuromi");
        stage.show();   
        stage.getIcons().add(miImagen);
        habilitarEquisCerrar(stage);

    }

    private void habilitarEquisCerrar(Stage miEscenario) {
        miEscenario.setOnCloseRequest((event) -> {
            event.consume();
            ControladorSalida.verificar(miEscenario);
        });

    }
}
