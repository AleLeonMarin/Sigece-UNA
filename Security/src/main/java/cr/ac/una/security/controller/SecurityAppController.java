package cr.ac.una.security.controller;

import java.net.URL;
import java.util.ResourceBundle;

import cr.ac.una.security.util.FlowController;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Kendall Fonseca
 */
public class SecurityAppController extends Controller implements Initializable {

    /**
     * Initializes the controller class.
     */

    @FXML
    private MFXButton btnAdminSystems;

    @FXML
    private MFXButton btnAdminUsers;

    @FXML
    private ImageView imgViewUserPhotProf;

    @FXML
    private MFXButton btnRolAdmin;

    @FXML
    private MFXButton btnLogOut;


    @FXML
    private HBox root;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

         @Override
    public void initialize() {

    }
    @FXML
    private void onActionBtnAdminUsers(ActionEvent event) {
        // Usa el FlowController para cargar el StackPane en el HBox de la vista principal
        FlowController.getInstance().goView("AdminUsersView");
    }

    @FXML
    void onActionBtnAdminSystems(ActionEvent event) {

        FlowController.getInstance().goView("AdminSystemView");

    }

    @FXML
    void onActionBtnRolAdmin(ActionEvent event) {
        FlowController.getInstance().goView("AdminRolView");
    }

    @FXML
    void onActionBtnLogOut(ActionEvent event) {
        ((Stage)root.getScene().getWindow()).close();
        FlowController.getInstance().goMain("LoginView");

    }


}
