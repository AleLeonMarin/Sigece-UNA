/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.security.controller;

import java.net.URL;
import java.util.ResourceBundle;

import cr.ac.una.security.service.UsersService;
import cr.ac.una.security.util.AppContext;
import cr.ac.una.security.util.FlowController;
import cr.ac.una.security.util.Mensaje;
import cr.ac.una.security.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

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
    private ResourceBundle rb;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
    }

    @Override
    public void initialize() {
        service = new UsersService();
        btnAcceptKey.setDisable(true);
        txtKey.setDisable(true);

        txtKey.clear();
        txtMail.clear();
    }

    @FXML
    void onActionBtnAcceptKey(ActionEvent event) {
        try {
            if (txtKey.getText().isEmpty() || txtKey.getText().isBlank()) {
                new Mensaje().showModal(AlertType.ERROR, rb.getString("keyTitle"), getStage(), rb.getString("keyErrorEmpty"));
            } else {
                Respuesta res = service.getByPass(txtKey.getText());
                if (res.getEstado()) {
                    new Mensaje().showModal(AlertType.INFORMATION, rb.getString("keyTitle"), getStage(), rb.getString("keySuccess"));
                    AppContext.getInstance().set("user", res.getResultado("Usuario"));
                    FlowController.getInstance().goViewInWindow("ResetPasswordView");
                    ((Stage)btnAcceptKey.getScene().getWindow()).close();
                } else {
                    new Mensaje().showModal(AlertType.ERROR, rb.getString("keyTitle"), getStage(), res.getMensaje());
                }
            }
        } catch (Exception ex) {
            new Mensaje().showModal(AlertType.ERROR, rb.getString("keyTitle"), getStage(), rb.getString("keyError"));
        }
    }

    @FXML
    void onActionBtnAcceptMail(ActionEvent event) {
        try {
            if (txtMail.getText().isEmpty() || txtMail.getText().isBlank()) {
                new Mensaje().showModal(AlertType.ERROR, rb.getString("emailTitle"), getStage(), rb.getString("emailErrorEmpty"));
            } else {
                Respuesta res = service.getByMail(txtMail.getText());
                if (res.getEstado()) {
                    new Mensaje().showModal(AlertType.INFORMATION, rb.getString("emailTitle"), getStage(), rb.getString("emailSuccess"));
                    btnAcceptKey.setDisable(false);
                    txtKey.setDisable(false);
                } else {
                    new Mensaje().showModal(AlertType.ERROR, rb.getString("emailTitle"), getStage(), res.getMensaje());
                }
            }
        } catch (Exception ex) {
            new Mensaje().showModal(AlertType.ERROR, rb.getString("emailTitle"), getStage(), rb.getString("emailError"));
        }
    }

    @FXML
    void onActionBtnGoBack(ActionEvent event) {
        ((Stage) btnGoBack.getScene().getWindow()).close();
        FlowController.getInstance().goMain("LoginView");


    }
}
