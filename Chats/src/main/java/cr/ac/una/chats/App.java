package cr.ac.una.chats;

import cr.ac.una.chats.util.FlowController;
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
        stage.getIcons().add(new Image("cr/ac/una/chats/resources/chats.jpg"));
        stage.setTitle("Chats Sigece UNA");
    }

    public static void main(String[] args) {
        launch();
    }

}