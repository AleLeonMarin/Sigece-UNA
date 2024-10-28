/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.admin.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import cr.ac.una.admin.model.UsersDto;
import cr.ac.una.admin.service.UsersService;
import cr.ac.una.admin.util.FlowController;
import cr.ac.una.admin.util.Mensaje;
import cr.ac.una.admin.util.Respuesta;
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
public class LoginController extends Controller implements Initializable {

    /**
     * Initializes the controller class.
     */

    @FXML
    private MFXButton btnChangePass;

    @FXML
    private MFXButton btnLogIn;

    @FXML
    private MFXButton btnRegister;

    @FXML
    private MFXTextField textMail;

    @FXML
    private MFXTextField textPassword;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void initialize() {
        textMail.clear();
        textPassword.clear();
    }

    @FXML
    void onActionBtnChangePass(ActionEvent event) {

        FlowController.getInstance().goViewInWindow("KeyAcceptView");
        getStage().close();

    }

    @FXML
    void onActionBtnLogIn(ActionEvent event) {
        try {
            if (textMail.getText().isEmpty() || textPassword.getText().isBlank()) {
                new Mensaje().showModal(AlertType.ERROR, "Validacion de Usuario", getStage(),
                        "Es necesario digitar un usuario para ingresar al sistema.");
            } else if (textPassword.getText().isEmpty() || textPassword.getText().isBlank()) {
                new Mensaje().showModal(AlertType.ERROR, "Validacion de Usuario", getStage(),
                        "Es necesario digitar una contraseña para ingresar al sistema.");
            } else {
                UsersService service = new UsersService();
                Respuesta respuesta = service.logIn(textMail.getText(), textPassword.getText());
                if (respuesta.getEstado()) {

                    UsersDto usuario = (UsersDto) respuesta.getResultado("Usuario");
                    if (usuario.getRoles().stream().anyMatch(r -> r.getName().equals("Administrador"))
                            || usuario.getRoles().stream().anyMatch(r -> r.getName().equals("Solictante"))
                            || usuario.getRoles().stream().anyMatch(r -> r.getName().equals("Gestor"))
                                    && usuario.getState().equals("A")) {
                        FlowController.getInstance().goMain("PrincipalView");
                        getStage().close();
                    } else {
                        new Mensaje().showModal(AlertType.ERROR, "Validacion de Usuario", getStage(),
                                "Usuario no tiene permisos para ingresar al sistema o no esta activo.");
                    }
                } else {
                    new Mensaje().showModal(AlertType.ERROR, "Validacion de Usuario", getStage(),
                            "Usuario o contraseña incorrecta.");
                }
            }
        } catch (Exception e) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, "Error al intentar ingresar al sistema",
                    e);
            new Mensaje().showModal(AlertType.ERROR, "LogIn", getStage(),
                    "Error al intentar ingresar al sistema.");
        }

    }

    @FXML
    void onActionBtnRegister(ActionEvent event) {

        FlowController.getInstance().goViewInWindow("RegisterView");
        getStage().close();

    }

}
