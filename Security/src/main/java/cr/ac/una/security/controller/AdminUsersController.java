package cr.ac.una.security.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import cr.ac.una.security.model.RolesDto;
import cr.ac.una.security.model.SystemsDto;
import cr.ac.una.security.model.UsersDto;
import cr.ac.una.security.service.SystemsService;
import cr.ac.una.security.service.UsersService;
import cr.ac.una.security.util.AppContext;
import cr.ac.una.security.util.FlowController;
import cr.ac.una.security.util.Formato;
import cr.ac.una.security.util.Mensaje;
import cr.ac.una.security.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;
import java.awt.image.BufferedImage;;

/**
 * FXML Controller class
 *
 * @author Kendall Fonseca
 */
public class AdminUsersController extends Controller implements Initializable {

    /**
     * Initializes the controller class.
     */

    @FXML
    private MFXButton btnDelete;

    @FXML
    private MFXButton btnNew;

    @FXML
    private MFXButton btnSave;

    @FXML
    private MFXComboBox<String> cmbLan;

    @FXML
    private MFXComboBox<RolesDto> cmbRoles;

    @FXML
    private ImageView imgViewUser;

    @FXML
    private StackPane root;

    @FXML
    private TableColumn<RolesDto, String> tbcIdRol;

    @FXML
    private TableColumn<RolesDto, String> tbcRolNombre;

    @FXML
    private TableColumn<RolesDto, String> tbcSistemaNombre;

    @FXML
    private TableView<RolesDto> tbvRoles;

    @FXML
    private TableView<SystemsDto> tbvUsers;

    @FXML
    private Tab tptMantenimiento;

    @FXML
    private Tab tptRoles;

    @FXML
    private TabPane tbpUsuarios;

    @FXML
    private MFXTextField txfCed;

    @FXML
    private MFXTextField txfCel;

    @FXML
    private MFXTextField txfID;

    @FXML
    private MFXTextField txfIdSistema;

    @FXML
    private MFXTextField txfLasts;

    @FXML
    private MFXTextField txfMail;

    @FXML
    private MFXTextField txfNombre;

    @FXML
    private MFXTextField txfNombreSistema;

    @FXML
    private MFXTextField txfPassword;

    @FXML
    private MFXTextField txfStatus;

    @FXML
    private MFXTextField txfTel;

    @FXML
    private MFXTextField txfUser;

    @FXML
    TableColumn<SystemsDto, RolesDto> tbcRol;

    UsersDto usuariosDto;

    SystemsDto systems;

    RolesDto rol;

    File file;

    List<Node> requeridos = new ArrayList<>();

    ObservableList<SystemsDto> sistemasList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        txfID.delegateSetTextFormatter(Formato.getInstance().integerFormat());
        txfCed.delegateSetTextFormatter(Formato.getInstance().cedulaFormat(15));
        txfTel.delegateSetTextFormatter(Formato.getInstance().integerFormatWithMaxLength(30));
        txfCel.delegateSetTextFormatter(Formato.getInstance().integerFormatWithMaxLength(30));
        txfMail.delegateSetTextFormatter(Formato.getInstance().maxLengthFormat(100));
        txfNombre.delegateSetTextFormatter(Formato.getInstance().letrasFormat(100));
        txfLasts.delegateSetTextFormatter(Formato.getInstance().letrasFormat(100));
        txfUser.delegateSetTextFormatter(Formato.getInstance().letrasFormat(100));
        txfPassword.delegateSetTextFormatter(Formato.getInstance().maxLengthFormat(50));
        txfStatus.delegateSetTextFormatter(Formato.getInstance().letrasFormat(100));

        // Initialize TableView columns rols
        tbcIdRol.setCellValueFactory(cd -> cd.getValue().id);
        tbcRolNombre.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getName()));
        TableColumn<RolesDto, Boolean> tbcEliminar = new TableColumn<>("Eliminar");
        tbcEliminar.setCellValueFactory(
                (TableColumn.CellDataFeatures<RolesDto, Boolean> p) -> new SimpleBooleanProperty(p.getValue() != null));
        tbcEliminar.setCellFactory((TableColumn<RolesDto, Boolean> p) -> new ButtonCell());
        tbvRoles.getColumns().add(tbcEliminar);

        this.usuariosDto = new UsersDto();
        this.systems = new SystemsDto();
        this.rol = new RolesDto();
        newUser(); // Resets to a new user
        indicateRequiredFields(); // Marks required fields

        // Add listener for TableView row selection
        tbvUsers.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends SystemsDto> observable, SystemsDto oldvalue, SystemsDto newValue) -> {
                    unbindSystems();
                    if (newValue != null) {
                        this.systems = newValue;
                        bindSystems(false);
                    }
                }

        );


        cmbLan.getItems().clear();
        cmbLan.getItems().addAll("Español", "Inglés", "Francés", "Alemán");  // Idiomas esperados


    }

    private Image byteToImage(byte[] bytes) {
        try {
            if (bytes != null && bytes.length > 0) {
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                return new Image(bis);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void bindUser(boolean newUser) {
        if (!newUser) {
       txfID.textProperty().bind(new SimpleStringProperty(String.valueOf(usuariosDto.getId())));
        }
        txfNombre.textProperty().bindBidirectional(usuariosDto.name);
        txfLasts.textProperty().bindBidirectional(usuariosDto.lastNames);
        txfCed.textProperty().bindBidirectional(usuariosDto.idCard);
        txfMail.textProperty().bindBidirectional(usuariosDto.email);
        txfTel.textProperty().bindBidirectional(usuariosDto.phone);
        txfCel.textProperty().bindBidirectional(usuariosDto.cellPhone);
        cmbLan.valueProperty().bindBidirectional(usuariosDto.language);
        txfUser.textProperty().bindBidirectional(usuariosDto.user);
        txfPassword.textProperty().bindBidirectional(usuariosDto.password);
        txfStatus.textProperty().bindBidirectional(usuariosDto.status);
        if (usuariosDto.getPhoto() != null) {
            imgViewUser.setImage(byteToImage(usuariosDto.getPhoto()));
        }
    }
public void unbindUser() {
    // Unbind text fields
    txfID.textProperty().unbind();
    txfNombre.textProperty().unbindBidirectional(usuariosDto.name);
    txfLasts.textProperty().unbindBidirectional(usuariosDto.lastNames);
    txfCed.textProperty().unbindBidirectional(usuariosDto.idCard);
    txfMail.textProperty().unbindBidirectional(usuariosDto.email);
    txfTel.textProperty().unbindBidirectional(usuariosDto.phone);
    txfCel.textProperty().unbindBidirectional(usuariosDto.cellPhone);
    cmbLan.valueProperty().unbindBidirectional(usuariosDto.language);
    txfUser.textProperty().unbindBidirectional(usuariosDto.user);
    txfPassword.textProperty().unbindBidirectional(usuariosDto.password);
    txfStatus.textProperty().unbindBidirectional(usuariosDto.status);
    // imgViewUser.setImage(new Image("../resources/add-photo.jpg"));
}

    private void bindSystems(boolean newSystem) {
        if (!newSystem) {
            txfIdSistema.textProperty().bind(this.systems.id);
        }
        txfNombreSistema.textProperty().bindBidirectional(this.systems.name);
        cmbRoles.setConverter(new StringConverter<RolesDto>() {
            @Override
            public String toString(RolesDto rolesDto) {
                return rolesDto != null ? rolesDto.getName() : "";
            }

            @Override
            public RolesDto fromString(String string) {
                return null;
            }
        });

        if (this.systems.getRolesDto() != null) {
            cmbRoles.setItems(FXCollections.observableArrayList(this.systems.getRolesDto()));
        }
    }

    private void unbindSystems() {
        txfIdSistema.textProperty().unbind();
        txfNombreSistema.textProperty().unbindBidirectional(this.systems.name);
    }

    public String validarRequeridos() {
        Boolean validos = true;
        String invalidos = "";
        for (Node node : requeridos) {
            if (node instanceof MFXTextField
                    && (((MFXTextField) node).getText() == null || ((MFXTextField) node).getText().isBlank())) {
                if (validos) {
                    invalidos += ((MFXTextField) node).getFloatingText();
                } else {
                    invalidos += "," + ((MFXTextField) node).getFloatingText();
                }
                validos = false;
            } else if (node instanceof MFXPasswordField
                    && (((MFXPasswordField) node).getText() == null || ((MFXPasswordField) node).getText().isBlank())) {
                if (validos) {
                    invalidos += ((MFXPasswordField) node).getFloatingText();
                } else {
                    invalidos += "," + ((MFXPasswordField) node).getFloatingText();
                }
                validos = false;
            } else if (node instanceof MFXDatePicker && ((MFXDatePicker) node).getValue() == null) {
                if (validos) {
                    invalidos += ((MFXDatePicker) node).getFloatingText();
                } else {
                    invalidos += "," + ((MFXDatePicker) node).getFloatingText();
                }
                validos = false;
            } else if (node instanceof MFXComboBox && ((MFXComboBox) node).getSelectionModel().getSelectedIndex() < 0) {
                if (validos) {
                    invalidos += ((MFXComboBox) node).getFloatingText();
                } else {
                    invalidos += "," + ((MFXComboBox) node).getFloatingText();
                }
                validos = false;
            }
        }
        if (validos) {
            return "";
        } else {
            return "Campos requeridos o con problemas de formato [" + invalidos + "].";
        }
    }

    private void newUser() {
        unbindUser(); // Unbind after initializing the DTO
        this.usuariosDto = new UsersDto();
        bindUser(true); // Now bind with a new user object
        // Reset fields
        txfID.clear();
        txfID.requestFocus();
        cmbLan.clear();
        cmbLan.getSelectionModel().clearSelection();
        cmbLan.getItems().addAll("Español", "Inglés");

    }

    private void newSystem() {
        unbindSystems();
        tbvUsers.getSelectionModel().select(null);
        this.systems = new SystemsDto();
        bindSystems(true);
        txfIdSistema.clear();
        cmbRoles.getItems().clear();
        txfIdSistema.requestFocus();
    }

    private void indicateRequiredFields() {
        requeridos.clear();
        requeridos.addAll(Arrays.asList(txfNombre, txfLasts, txfCed, txfMail, txfTel, txfCel, cmbLan, txfUser,
                txfPassword, txfStatus));
    }

    /* Method to charge generic image where the user photo will be shown */
    private void chargeGenericImage(ImageView photo, File file) {
        if (file != null) {
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                photo.setImage(image);
            } catch (IOException e) {
                new Mensaje().showModal(AlertType.ERROR, "Error", getStage(), "Error al cargar la imagen.");
            }
        }

    }

    public void chargeRoles() {
        tbvRoles.getItems().clear();
        tbvRoles.setItems(FXCollections.observableArrayList(this.usuariosDto.getRoles()));

        // Obtiene el sistema asociado al rol
        SystemsDto sistema = rol.getSystem(); // Asumiendo que es un solo sistema y no una lista

        if (sistema != null) {
            tbcSistemaNombre.setCellValueFactory(cd -> new SimpleStringProperty(sistema.getName()));
        } else {
            tbcSistemaNombre.setCellValueFactory(cd -> new SimpleStringProperty("Sin sistema"));
        }

        tbvRoles.refresh();
    }


    private void chargeUser(Long id) {
        try {
            UsersService service = new UsersService();
            Respuesta respuesta = service.obtenerUsuario(id);
            if (respuesta.getEstado()) {
                unbindUser(); // Desvincula los campos para evitar problemas anteriores
                this.usuariosDto = (UsersDto) respuesta.getResultado("Usuario");

                // Aquí puedes imprimir para asegurarte de que el objeto tiene datos
                System.out.println("Usuario ID: " + usuariosDto.getId());
                System.out.println("Nombre: " + usuariosDto.getName());

                bindUser(false); // Vincula los datos si existen
                validarRequeridos(); // Verifica los campos requeridos
                chargeRoles(); // Carga los roles asociados al usuario
            } else {
                new Mensaje().showModal(AlertType.INFORMATION, "Buscar Usuario", getStage(), respuesta.getMensaje());
            }
        } catch (Exception e) {
            Logger.getLogger(AdminUsersController.class.getName()).log(Level.SEVERE, "Error buscando el usuario ", e);
            new Mensaje().showModal(AlertType.ERROR, "Buscar Usuario", getStage(), "Error buscando el usuario.");
        }
    }


    private void chargeSistem(Long id) {
        try {
            SystemsService service = new SystemsService();
            Respuesta res = service.obtenerSystem(id);

            if (!res.getEstado()) {
                new Mensaje().showModal(AlertType.ERROR, "Cargar Sistema", getStage(), res.getMensaje());
            } else {
                unbindSystems();
                this.systems = (SystemsDto) res.getResultado("Sistema");
                bindSystems(false);
            }
        } catch (Exception e) {
            Logger.getLogger(AdminSystemController.class.getName()).log(Level.SEVERE, "Error cargando sistema", e);
            new Mensaje().showModal(AlertType.ERROR, "Cargar Sistema", getStage(), "Error cargando el Sistema.");
        }
    }

    private void columnsTable() {
        // Configuración de la columna para el ID
        TableColumn<SystemsDto, String> tbcIdRol = new TableColumn<>("ID");
        tbcIdRol.setCellValueFactory(cd -> cd.getValue().id);
        tbvUsers.getColumns().add(tbcIdRol);

        // Configuración de la columna para el nombre
        TableColumn<SystemsDto, String> tbcRolNombre = new TableColumn<>("Nombre");
        tbcRolNombre.setCellValueFactory(cd -> cd.getValue().name);
        tbvUsers.getColumns().add(tbcRolNombre);

        // Configuración de la columna para el rol
        tbcRol = new TableColumn<>("Rol");
        tbcRol.setCellValueFactory(cd -> {
            RolesDto selectedRole = cmbRoles.getSelectionModel().getSelectedItem();
            return new SimpleObjectProperty<>(selectedRole);
        });

        tbcRol.setCellFactory(column -> new TableCell<SystemsDto, RolesDto>() {
            @Override
            protected void updateItem(RolesDto rol, boolean empty) {
                super.updateItem(rol, empty);
                if (empty || rol == null) {
                    setText(null);
                } else {
                    // Muestra el nombre del rol
                    setText(rol.getName());
                }
            }
        });

        tbvUsers.getColumns().add(tbcRol);
    }

    @Override
    public void initialize() {
    }

    @FXML
    void onKeyPressedTxId(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && !txfID.getText().isBlank()) {
            chargeUser(Long.valueOf(txfID.getText()));
        }

    }

    @FXML
    void onKeyPressedTxfIdSistema(KeyEvent event) {

        if (event.getCode() == KeyCode.ENTER && !txfIdSistema.getText().isBlank()) {
            chargeSistem(Long.valueOf(txfIdSistema.getText()));
        }

    }

    @FXML
    void selectionChangeTabUsuarios(Event event) {

        if (tptRoles.isSelected()) {
            if (this.usuariosDto.getId() == null) {
                new Mensaje().showModal(AlertType.INFORMATION, "Roles de Usuario", getStage(),
                        "Debe seleccionar un usuario.");
                tbpUsuarios.getSelectionModel().select(tptMantenimiento);
            } else {
                new Mensaje().showModal(AlertType.INFORMATION, "Inclusion", getStage(), "Solo puede agregar un rol de un sistema a la vez.");
                tbvUsers.getItems().clear();
                tbvUsers.getColumns().clear();
                if (tbvUsers.getColumns().isEmpty()) {
                    columnsTable();
                }
                newSystem();
            }
        }
    }

    @FXML
    void onActionBtnAddUser(ActionEvent event) {

        if (this.systems.getId() == null || this.systems.getName().isEmpty() || cmbRoles.getSelectedItem() == null) {
            new Mensaje().showModal(AlertType.ERROR, "Agregar Rol", getStage(),
                    "Es necesario cargar un sistema y seleccionar un rol.");
        } else if (tbvUsers.getItems() == null || !tbvUsers.getItems().stream().anyMatch(s -> s.equals(this.systems))) {
            this.usuariosDto.setModified(true);
            tbvUsers.getItems().add(this.systems);
            tbvUsers.refresh();
        }

        newSystem();
    }

    @FXML
    void onActionBtnDelete(ActionEvent event) {

        try {
            if (this.usuariosDto.getId() == null) {
                new Mensaje().showModal(AlertType.INFORMATION, "Eliminar Usuario", getStage(),
                        "No se ha seleccionado un usuario.");
            } else {
                UsersService service = new UsersService();
                Respuesta respuesta = service.eliminarUsuario(this.usuariosDto.getId());
                if (!respuesta.getEstado()) {
                    new Mensaje().showModal(AlertType.INFORMATION, "Eliminar Usuario", getStage(),
                            respuesta.getMensaje());
                } else {
                    new Mensaje().showModal(AlertType.INFORMATION, "Eliminar Usuario", getStage(),
                            "Usuario eliminado correctamente.");
                    newUser();
                }
            }
        } catch (Exception e) {
            Logger.getLogger(AdminUsersController.class.getName()).log(Level.SEVERE, "Error eliminando el usuario ", e);
            new Mensaje().showModal(AlertType.ERROR, "Eliminar Usuario", getStage(), "Error eliminando el usuario.");
        }

    }

    @FXML
    void onActionBtnNew(ActionEvent event) {

        if (tptRoles.isSelected()) {
            newSystem();
        } else if (tptMantenimiento.isSelected()) {
            if (new Mensaje().showConfirmation("Limpiar Usuario", getStage(),
                    "¿Esta seguro que desea limpiar el registro?")) {
                newUser();
                tbvRoles.getItems().clear();
            }
        }

    }

    @FXML
    void onActionBtnSave(ActionEvent event) {
        try {
            String validation = validarRequeridos();
            if (!validation.isEmpty()) {
                new Mensaje().showModal(AlertType.WARNING, "Guardar Usuario", getStage(), validation);
            } else {

                if (this.usuariosDto.getId() == null) {
                    this.usuariosDto.setState("I");
                }

                List<RolesDto> rolesList = new ArrayList<>();
                for (SystemsDto sistema : tbvUsers.getItems()) {
                    RolesDto selectedRole = (RolesDto) tbcRol.getCellData(sistema); // Get the RolesDto from the cell
                    if (selectedRole != null) {
                        selectedRole.setModified(true);
                        rolesList.add(selectedRole);
                    }
                }

                // Assign the roles to the user
                this.usuariosDto.setRoles(rolesList);

                rolesList.forEach(rol -> System.out.println("Rol seleccionado: " + rol.getName()));

                // Guardar el usuario y sus roles seleccionados
                UsersService service = new UsersService();
                Respuesta respuesta = service.guardarUsuario(usuariosDto);

                if (!respuesta.getEstado()) {
                    new Mensaje().showModal(AlertType.INFORMATION, "Guardar Usuario", getStage(),
                            respuesta.getMensaje());
                } else {
                    unbindUser();
                    this.usuariosDto = (UsersDto) respuesta.getResultado("Usuario");
                    bindUser(false);
                    new Mensaje().showModal(AlertType.INFORMATION, "Guardar Usuario", getStage(),
                            "Usuario guardado correctamente.");

                    File file = new File("photo.png");
                    if (file.exists()) {
                        boolean deleted = file.delete();
                        if (deleted) {
                            System.out.println("Imagen borrada correctamente.");
                        } else {
                            System.out.println("No se pudo borrar la imagen.");
                        }
                    }
                }

                AppContext.getInstance().set("Taken", false);

            }
        } catch (Exception e) {
            Logger.getLogger(AdminUsersController.class.getName()).log(Level.SEVERE, "Error guardando el usuario", e);
            new Mensaje().showModal(AlertType.ERROR, "Guardar Usuario", getStage(), "Error guardando el usuario.");
        }
    }

    @FXML
    void onActionImgvPhoto(MouseEvent event) {

        FlowController.getInstance().goViewInWindowModal("CamView", getStage(), false);

    }

    @FXML
    void onActionRoot(MouseEvent event) {
        Boolean photoTaken = (Boolean) AppContext.getInstance().get("Taken");

        if (photoTaken != null && photoTaken) {
            File file = new File("photo.png");
            if (file.exists()) {
                try {
                    BufferedImage bufferedImage = ImageIO.read(file);
                    Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                    imgViewUser.setImage(image);
                    usuariosDto.setPhoto(toByteArray(bufferedImage));
                } catch (IOException e) {
                    e.printStackTrace();
                    new Mensaje().showModal(AlertType.ERROR, "Error", getStage(), "Error al cargar la imagen.");
                }
            } else {
                new Mensaje().showModal(AlertType.WARNING, "Error", getStage(), "El archivo de la foto no existe.");
            }
        }
    }

    private byte[] toByteArray(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        return baos.toByteArray();
    }

    private class ButtonCell extends TableCell<RolesDto, Boolean> {

        final Button cellButton = new Button();

        ButtonCell() {
            cellButton.setPrefWidth(500);
            cellButton.getStyleClass().add("jfx-tbvUser-btnDelete");

            cellButton.setOnAction((ActionEvent t) -> {
                RolesDto rol = (RolesDto) ButtonCell.this.getTableView().getItems().get(ButtonCell.this.getIndex());
                usuariosDto.getRolesEliminados().add(rol);
                usuariosDto.getRoles().remove(rol);
                rol.getUsuariosDto().remove(usuariosDto);
                tbvRoles.getItems().remove(rol);
                tbvRoles.refresh();
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                setGraphic(cellButton);
            }
        }
    }

}
