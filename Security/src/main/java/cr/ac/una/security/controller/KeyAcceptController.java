/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.security.controller;

import java.net.URL;
import java.util.ResourceBundle;

import cr.ac.una.security.util.FlowController;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @Override
    public void initialize() {
        btnAcceptKey.setDisable(true);
        txtKey.setDisable(true);

    }

    @FXML
    void onActionBtnAcceptKey(ActionEvent event) {
        FlowController.getInstance().goViewInWindow("ResetPasswordView");
    }

    @FXML
    void onActionBtnAcceptMail(ActionEvent event) {
        btnAcceptKey.setDisable(false);
        txtKey.setDisable(false);
    }

    @FXML
    void onActionBtnGoBack(ActionEvent event) {
        getStage().close();

    }

}
