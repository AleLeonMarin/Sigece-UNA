/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.mails.controller;

import java.net.URL;
import java.util.ResourceBundle;

import cr.ac.una.mails.service.UsersService;
import cr.ac.una.mails.util.AppContext;
import cr.ac.una.mails.util.FlowController;
import cr.ac.una.mails.util.Mensaje;
import cr.ac.una.mails.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;

/**
 * FXML Controller class
 *
 * @author Kendall Fonseca
 */
public class KeyAcceptController extends Controller implements Initializable {

    /**
     * Initializes the controller class.
     */

    @FXML
    private MFXButton btnAcceptKey;

    @FXML
    private MFXButton btnAcceptMail;

    @FXML
    private MFXButton btnGoBack;

    @FXML
    private MFXTextField txtKey;

    @FXML
    private MFXTextField txtMail;

    UsersService service;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @Override
    public void initialize() {
        service = new UsersService();
        btnAcceptKey.setDisable(true);
        txtKey.setDisable(true);

    }

    @FXML
    void onActionBtnAcceptKey(ActionEvent event) {

        try {
            if (txtKey.getText().isEmpty() || txtKey.getText().isBlank()) {
                new Mensaje().showModal(AlertType.ERROR, "Clave", getStage(), "Debe ingresar una clave");
            } else {
                Respuesta res = service.getByPass(txtKey.getText());
                if (res.getEstado()) {
                    new Mensaje().showModal(AlertType.INFORMATION, "Clave", getStage(), "Clave correcta");
                    AppContext.getInstance().set("user", res.getResultado("Usuario"));
                    FlowController.getInstance().goViewInWindow("ResetPasswordView");
                } else {
                    new Mensaje().showModal(AlertType.ERROR, "Clave", getStage(), res.getMensaje());
                }
            }
        } catch (Exception ex) {
            new Mensaje().showModal(AlertType.ERROR, "Clave", getStage(), "Error al obtener la clave");
        }
    }

    @FXML
    void onActionBtnAcceptMail(ActionEvent event) {

        try {
            if (txtMail.getText().isEmpty() || txtMail.getText().isBlank()) {
                new Mensaje().showModal(AlertType.ERROR, "Correo", getStage(), "Debe ingresar un correo electrónico");
            } else {
                Respuesta res = service.getByMail(txtMail.getText());
                if (res.getEstado()) {
                    new Mensaje().showModal(AlertType.INFORMATION, "Correo", getStage(), "Usuario encontrado");
                    btnAcceptKey.setDisable(false);
                    txtKey.setDisable(false);
                } else {
                    new Mensaje().showModal(AlertType.ERROR, "Correo", getStage(), res.getMensaje());
                }
            }
        } catch (Exception ex) {
            new Mensaje().showModal(AlertType.ERROR, "Correo", getStage(), "Error al obtener el correo");
        }
    }

    @FXML
    void onActionBtnGoBack(ActionEvent event) {
        getStage().close();

    }

}
