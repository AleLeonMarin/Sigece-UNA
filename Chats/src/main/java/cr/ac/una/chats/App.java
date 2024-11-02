package cr.ac.una.chats;

import cr.ac.una.chats.util.FlowController;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Stage primaryStage;
    private static Locale currentLocale = new Locale("en");

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        restartApp();
    }

    public static void restart(Locale newLocale) {
        currentLocale = newLocale;
        restartApp();
    }

    public static void restartAf(Locale newLocale) {
        currentLocale = newLocale;
        restartAppAf();
    }

    private static void restartApp() {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("cr.ac.una.chats.resources.MessagesBundle", currentLocale);
            FlowController.getInstance().InitializeFlow(primaryStage, bundle);
            FlowController.getInstance().goMain("LoginView");
            primaryStage.setTitle("SigeceUna");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void restartAppAf() {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("cr.ac.una.mails.resources.MessagesBundle", currentLocale);
            FlowController.getInstance().InitializeFlow(primaryStage, bundle);
            FlowController.getInstance().goMain("MailAppView");
            primaryStage.setTitle("SigeceUna");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}