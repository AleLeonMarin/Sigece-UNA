/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.chats.controller;

import java.net.URL;
import java.util.ResourceBundle;

import cr.ac.una.chats.model.UsersDto;
import cr.ac.una.chats.service.UsersService;
import cr.ac.una.chats.util.AppContext;
import cr.ac.una.chats.util.Mensaje;
import cr.ac.una.chats.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import cr.ac.una.chats.util.FlowController;

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

    ResourceBundle rb;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.rb = rb;

    }

    @Override
    public void initialize() {
    }

    @FXML
    void onActionBtnAccept(ActionEvent event) {
        try {


            if (fillPassWord.getText().isBlank() || repeatPassword.getText().isBlank()) {
                new Mensaje().showModal(AlertType.ERROR, rb.getString("errorTitle"), getStage(), rb.getString("errorFillAllFields"));
            } else if (fillPassWord.getText().isBlank() || fillPassWord.getText().isEmpty()) {
                new Mensaje().showModal(AlertType.ERROR, rb.getString("errorTitle"), getStage(), rb.getString("errorFillPasswordField"));
            } else if (repeatPassword.getText().isBlank() || repeatPassword.getText().isEmpty()) {
                new Mensaje().showModal(AlertType.ERROR, rb.getString("errorTitle"), getStage(), rb.getString("errorFillRepeatPasswordField"));
            } else if (!fillPassWord.getText().equals(repeatPassword.getText())) {
                new Mensaje().showModal(AlertType.ERROR, rb.getString("errorTitle"), getStage(), rb.getString("errorPasswordsDoNotMatch"));
            } else {
                UsersDto user = (UsersDto) AppContext.getInstance().get("user");
                UsersService service = new UsersService();
                Respuesta respuesta = service.updatePassword(user.getEmail(), repeatPassword.getText());
                if (respuesta.getEstado()) {
                    new Mensaje().showModal(AlertType.INFORMATION, rb.getString("successTitle"), getStage(),
                            rb.getString("successPasswordUpdated"));
                    FlowController.getInstance().goMain("LoginView");
                    this.getStage().close();
                } else {
                    new Mensaje().showModal(AlertType.ERROR, rb.getString("errorTitle"), getStage(), respuesta.getMensaje());
                }
            }
        } catch (Exception e) {
            new Mensaje().showModal(AlertType.ERROR, rb.getString("errorTitle"), getStage(), rb.getString("errorUpdatingPassword"));
        }
    }

}
