/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.mails.controller;

import java.net.URL;
import java.util.ResourceBundle;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Kendall Fonseca
 */
public class ResetPasswordController extends Controller implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

        @Override
    public void initialize() {
    }

    @FXML
    private MFXButton btnAccept;

    @FXML
    private MFXPasswordField fillPassWord;

    @FXML
    private MFXPasswordField repeatPassword;


    @FXML
    void onActionBtnAccept(ActionEvent event) {

    }






    
}
