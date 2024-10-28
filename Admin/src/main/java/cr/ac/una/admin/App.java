package cr.ac.una.admin;

import cr.ac.una.admin.util.FlowController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FlowController.getInstance().InitializeFlow(stage, null);
        FlowController.getInstance().goMain("LoginView");
        //stage.getIcons().add(new Image("cr/ac/una/mailapp/resources/logo2.png"));
        stage.setTitle("SigeceUna");
    }

    public static void main(String[] args) {
        launch();
    }

}
