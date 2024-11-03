    /*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
     */
    package cr.ac.una.mails.controller;

    import java.net.URL;
    import java.util.Locale;
    import java.util.ResourceBundle;
    import java.util.logging.Level;
    import java.util.logging.Logger;

    import cr.ac.una.mails.model.UsersDto;
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
    import javafx.scene.control.Button;

    import cr.ac.una.mails.App;
    import javafx.stage.Stage;


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
        private Button btnCostaRica;

        @FXML
        private Button btnUsa;

        @FXML
        private MFXTextField textMail;

        @FXML
        private MFXTextField textPassword;

        private ResourceBundle bundle;

        @Override
        public void initialize(URL url, ResourceBundle rb) {
            this.bundle = rb;
        }

        @Override
        public void initialize() {
            textMail.clear();
            textPassword.clear();
        }

        @FXML
        void onActionBtnChangePass(ActionEvent event) {

            FlowController.getInstance().goViewInWindow("KeyAcceptView");
            ((Stage)btnChangePass.getScene().getWindow()).close();

        }

        @FXML
        void onActionBtnLogIn(ActionEvent event) {

                if (textMail.getText().isEmpty() || textPassword.getText().isBlank()) {
                    new Mensaje().showModal(AlertType.ERROR, bundle.getString("userValidation.title"), getStage(),
                            bundle.getString("userValidation.emptyUser"));
                } else if (textPassword.getText().isEmpty() || textPassword.getText().isBlank()) {
                    new Mensaje().showModal(AlertType.ERROR, bundle.getString("userValidation.title"), getStage(),
                            bundle.getString("userValidation.emptyPassword"));
                } else {
                    UsersService service = new UsersService();
                    Respuesta respuesta = service.logIn(textMail.getText(), textPassword.getText());
                    if (respuesta.getEstado()) {
                        UsersDto usuario = (UsersDto) respuesta.getResultado("Usuario");
                        AppContext.getInstance().set("user", usuario);

                        AppContext.getInstance().set("Token", usuario.getToken());
                        System.out.println("token: " + usuario.getToken());

                        if (usuario.getRoles().stream().anyMatch(r -> r.getName().equals("Administrador de correos masivos"))
                                && usuario.getState().equals("A") ||
                                usuario.getRoles().stream().anyMatch(r -> r.getName().equals("Normal de correos masivos")
                                && usuario.getState().equals("A"))) {
                            AppContext.getInstance().set("Usuario", usuario);
                            FlowController.getInstance().goMain("MailAppView");
                        } else {
                            new Mensaje().showModal(AlertType.ERROR, bundle.getString("userValidation.title"), getStage(),
                                    bundle.getString("userValidation.noPermission"));
                        }
                    } else {
                        new Mensaje().showModal(AlertType.ERROR, bundle.getString("userValidation.title"), getStage(),
                                bundle.getString("userValidation.invalidCredentials"));
                    }
                }

        }

        @FXML
        void onActionBtnRegister(ActionEvent event) {
            FlowController.getInstance().goViewInWindow("RegisterView");
            ((Stage)btnRegister.getScene().getWindow()).close();
        }

        @FXML
        void onActionBtnCostaRica(ActionEvent event) {
            ResourceBundle bundle = ResourceBundle.getBundle("cr.ac.una.mails.resources.MessagesBundle", new Locale("es"));
            FlowController.getInstance().reiniciarVistasConNuevoIdioma(bundle);
            App.restart(new Locale("es"));
        }

        @FXML
        void onActionBtnUsa(ActionEvent event) {
            ResourceBundle bundle = ResourceBundle.getBundle("cr.ac.una.mails.resources.MessagesBundle", new Locale("en"));
            FlowController.getInstance().reiniciarVistasConNuevoIdioma(bundle);
            App.restart(new Locale("en"));
        }



    }
