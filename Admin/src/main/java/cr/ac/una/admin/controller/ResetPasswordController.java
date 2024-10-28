/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.admin.controller;

import java.net.URL;
import java.util.ResourceBundle;

import cr.ac.una.admin.model.UsersDto;
import cr.ac.una.admin.service.UsersService;
import cr.ac.una.admin.util.AppContext;
import cr.ac.una.admin.util.Mensaje;
import cr.ac.una.admin.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import cr.ac.una.admin.util.FlowController;

/**
 * FXML Controller class
 *
 * @author Kendall Fonseca
 */
public class ResetPasswordController extends Controller implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private MFXButton btnAccept;

    @FXML
    private MFXPasswordField fillPassWord;

    @FXML
    private MFXPasswordField repeatPassword;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void initialize() {
    }

    @FXML
    void onActionBtnAccept(ActionEvent event) {
        try {
            if (fillPassWord.getText().isBlank() || repeatPassword.getText().isBlank()) {
                new Mensaje().showModal(AlertType.ERROR, "Clave", getStage(), "Debe llenar todos los campos");
            } else if (fillPassWord.getText().isBlank() || fillPassWord.getText().isEmpty()) {
                new Mensaje().showModal(AlertType.ERROR, "Clave", getStage(), "Debe llenar el campo de clave");
            } else if (repeatPassword.getText().isBlank() || repeatPassword.getText().isEmpty()) {
                new Mensaje().showModal(AlertType.ERROR, "Clave", getStage(), "Debe llenar el campo de repetir clave");
            } else if (!fillPassWord.getText().equals(repeatPassword.getText())) {
                new Mensaje().showModal(AlertType.ERROR, "Clave", getStage(), "Las claves no coinciden");
            } else {

                UsersDto user = (UsersDto) AppContext.getInstance().get("user");
                UsersService service = new UsersService();
                Respuesta respuesta = service.updatePassword(user.getEmail(), repeatPassword.getText());
                if (respuesta.getEstado()) {
                    new Mensaje().showModal(AlertType.INFORMATION, "Clave", getStage(),
                            "Clave actualizada correctamente");
                    FlowController.getInstance().goViewInWindow("LoginView");
                    this.getStage().close();
                } else {
                    new Mensaje().showModal(AlertType.ERROR, "Clave", getStage(), respuesta.getMensaje());
                }
            }
        } catch (Exception e) {
            new Mensaje().showModal(AlertType.ERROR, "Clave", getStage(), "Error al actualizar la clave");
        }

    }

}
