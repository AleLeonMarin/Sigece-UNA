package cr.ac.una.mails;

import cr.ac.una.mails.util.FlowController;
import javafx.application.Application;
//import cr.ac.una.tarea.controller.LoginController;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    //LoginController loginController;

    @Override
    public void start(Stage stage) throws IOException {
        FlowController.getInstance().InitializeFlow(stage, null);
        FlowController.getInstance().goMain("LoginView");
        stage.getIcons().add(new Image("cr/ac/una/mails/resources/logo2.png"));
        stage.setTitle("SigeceUna");
    }

    public static void main(String[] args) {
        launch();
    }

}