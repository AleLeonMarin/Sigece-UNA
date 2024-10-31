package cr.ac.una.security;

import cr.ac.una.security.util.FlowController;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FlowController.getInstance().InitializeFlow(stage, null);
        FlowController.getInstance().goViewInWindow("LoginView");
        stage.setTitle("Security Sigece UNA");
        stage.getIcons().add(new Image(getClass().getResource("/cr/ac/una/security/resources/security.jpg").toExternalForm()));
    }

    public static void main(String[] args) {
        launch();
    }

}