package cr.ac.una.security.util;

import cr.ac.una.security.App;
import cr.ac.una.security.controller.Controller;
import java.io.IOException;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;

public class FlowController {

    private static FlowController INSTANCE = null;
    private static Stage mainStage;
    private static ResourceBundle idioma;
    private static HashMap<String, FXMLLoader> loaders = new HashMap<>();

    private FlowController() {
    }

    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (FlowController.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FlowController();
                }
            }
        }
    }

    public static FlowController getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void InitializeFlow(Stage stage, ResourceBundle idioma) {
        getInstance();
        this.mainStage = stage;
        this.idioma = idioma;
    }

    private FXMLLoader getLoader(String name) {
        FXMLLoader loader = loaders.get(name);
        if (loader == null) {
            synchronized (FlowController.class) {
                if (loader == null) {
                    try {
                        loader = new FXMLLoader(App.class.getResource("view/" + name + ".fxml"), this.idioma);
                        loader.load();
                        loaders.put(name, loader);
                    } catch (Exception ex) {
                        loader = null;
                        java.util.logging.Logger.getLogger(FlowController.class.getName()).log(Level.SEVERE,
                                "Creando loader [" + name + "].", ex);
                    }
                }
            }
        }
        return loader;
    }

    public void goMain(String viewName) {
        try {
            this.mainStage.setScene(
                    new Scene(FXMLLoader.load(App.class.getResource("view/" + viewName + ".fxml"), this.idioma)));
            MFXThemeManager.addOn(this.mainStage.getScene(), Themes.DEFAULT, Themes.LEGACY);
            this.mainStage.show();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(FlowController.class.getName()).log(Level.SEVERE,
                    "Error inicializando la vista base.", ex);
        }
    }

    public void goView(String viewName) {
        goView(viewName, "Center", null);
    }

    public void goView(String viewName, String accion) {
        goView(viewName, "Center", accion);
    }

    public void goView(String viewName, String location, String accion) {
        FXMLLoader loader = getLoader(viewName);
        Controller controller = loader.getController();
        controller.setAccion(accion);
        controller.initialize();
        Stage stage = this.mainStage;

        if (stage.getScene() == null) {
            System.out.println("La escena principal no está inicializada.");
            return;
        }

        Parent root = loader.getRoot();

        // Verifica si el root de la escena es un StackPane
        if (stage.getScene().getRoot() instanceof StackPane) {
            StackPane mainStackPane = (StackPane) stage.getScene().getRoot();

            // Verifica si el primer hijo es un StackPane
            if (mainStackPane.getChildren().get(0) instanceof StackPane) {
                StackPane innerStackPane = (StackPane) mainStackPane.getChildren().get(0);

                // Verifica si el primer hijo de ese StackPane es un HBox
                if (innerStackPane.getChildren().get(0) instanceof HBox) {
                    HBox hbox = (HBox) innerStackPane.getChildren().get(0);

                    // Verifica si el tercer hijo del HBox es un StackPane (después del VBox y
                    // Separator)
                    if (hbox.getChildren().get(2) instanceof StackPane) {
                        StackPane targetStackPane = (StackPane) hbox.getChildren().get(2);

                        switch (location) {
                            case "Center":
                                targetStackPane.getChildren().clear();
                                targetStackPane.getChildren().add(root);
                                break;
                            default:
                                System.out.println("Invalid location specified: " + location);
                                break;
                        }
                    } else {
                        System.out.println("Third child of HBox is not a StackPane, it's a "
                                + hbox.getChildren().get(2).getClass().getName());
                    }
                } else {
                    System.out.println("First child of inner StackPane is not an HBox, it's a "
                            + innerStackPane.getChildren().get(0).getClass().getName());
                }
            } else {
                System.out.println("First child of main StackPane is not a StackPane, it's a "
                        + mainStackPane.getChildren().get(0).getClass().getName());
            }
        } else {
            System.out.println(
                    "Root of the scene is not a StackPane, it's a " + stage.getScene().getRoot().getClass().getName());
        }
    }

    public void goViewInStage(String viewName, Stage stage) {
        FXMLLoader loader = getLoader(viewName);
        Controller controller = loader.getController();
        controller.setStage(stage);
        stage.getScene().setRoot(loader.getRoot());
        MFXThemeManager.addOn(stage.getScene(), Themes.DEFAULT, Themes.LEGACY);

    }

    public void goViewInWindow(String viewName) {
        FXMLLoader loader = getLoader(viewName);
        Controller controller = loader.getController();
        controller.initialize();
        Stage stage = new Stage();
        stage.getIcons().add(new Image("cr/ac/una/security/resources/security.jpg"));
        stage.setOnHidden((WindowEvent event) -> {
            controller.getStage().getScene().setRoot(new Pane());
            controller.setStage(null);
        });
        controller.setStage(stage);
        Parent root = loader.getRoot();
        Scene scene = new Scene(root);
        MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void goViewInWindowModal(String viewName, Stage parentStage, Boolean resizable) {
        FXMLLoader loader = getLoader(viewName);
        Controller controller = loader.getController();
        controller.initialize();
        Stage stage = new Stage();
        stage.getIcons().add(new Image("cr/ac/una/security/resources/security.jpg"));
        stage.setResizable(resizable);
        stage.setOnHidden((WindowEvent event) -> {
            controller.getStage().getScene().setRoot(new Pane());
            controller.setStage(null);
        });
        controller.setStage(stage);
        Parent root = loader.getRoot();
        Scene scene = new Scene(root);
        MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(parentStage);
        stage.centerOnScreen();
        stage.showAndWait();

    }

    public Controller getController(String viewName) {
        return getLoader(viewName).getController();
    }

    public void limpiarLoader(String view) {
        this.loaders.remove(view);
    }

    public static void setIdioma(ResourceBundle idioma) {
        FlowController.idioma = idioma;
    }

    public void initialize() {
        this.loaders.clear();
    }

    public void salir() {
        this.mainStage.close();
    }

}