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

import cr.ac.una.security.model.AreasDto;
import cr.ac.una.security.model.RolesDto;
import cr.ac.una.security.model.SystemsDto;
import cr.ac.una.security.model.UsersDto;
import cr.ac.una.security.service.AreasService;
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

import java.awt.geom.Area;
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
    private MFXButton btnSearch;

    @FXML
    private MFXComboBox<String> cmbLan;

    @FXML
    private MFXComboBox<RolesDto> cmbRoles;

    @FXML
    private MFXComboBox<AreasDto> cmbAreas;

    @FXML
    private ImageView imgViewUser;

    @FXML
    private StackPane root;

    @FXML
    private TableColumn<RolesDto, String> tbcIdRol;

    @FXML
    private TableColumn<RolesDto, String> tbcRolNombre;

    @FXML
    private TableView<RolesDto> tbvRoles;

    @FXML
    private TableView<SystemsDto> tbvUsers;

    @FXML
    public Tab tptMantenimiento;

    @FXML
    public Tab tptRoles;

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

    AreasDto area;

    File file;

    List<Node> requeridos = new ArrayList<>();

    ObservableList<SystemsDto> sistemasList = FXCollections.observableArrayList();

    ResourceBundle bundle;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.bundle = rb;

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
        this.area = new AreasDto();
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

        cmbAreas.setConverter(new StringConverter<AreasDto>() {
            @Override
            public String toString(AreasDto area) {
                return area != null ? area.getName() : "";
            }

            @Override
            public AreasDto fromString(String string) {
                return null;
            }
        });

        cmbLan.getItems().clear();
        cmbLan.getItems().addAll("Español", "Inglés", "Francés", "Alemán"); // Idiomas esperados

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

        if (this.systems.getRoles() != null) {
            cmbRoles.setItems(FXCollections.observableArrayList(this.systems.getRoles()));
        }
    }

    private void unbindSystems() {
        txfIdSistema.textProperty().unbind();
        txfNombreSistema.textProperty().unbindBidirectional(this.systems.name);
        cmbRoles.setItems(FXCollections.emptyObservableList());
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
            return validos ? "" : bundle.getString("requiredFields") + "[" + invalidos + "].";
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
        cmbAreas.getItems().clear();
        cmbAreas.getSelectionModel().clearSelection();
        chargeAreas();
        tbvRoles.getItems().clear();

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
                new Mensaje().showModal(AlertType.ERROR, bundle.getString("errorCargaImagen"), getStage(),
                        bundle.getString("errorCargaImagen"));
            }
        }
    }

    public void chargeRoles() {
        tbvRoles.getItems().clear();
        tbvRoles.setItems(FXCollections.observableArrayList(this.usuariosDto.getRoles()));
        tbvRoles.refresh();
    }

    public void chargeAreasToCombo() {
        AreasDto areaSeleccionada = usuariosDto.getAreas();
        if (areaSeleccionada != null) {
            for (AreasDto area : cmbAreas.getItems()) {
                if (area.getId().equals(areaSeleccionada.getId())) {
                    cmbAreas.getSelectionModel().selectItem(area);
                    break;
                }
            }
        } else {
            System.out.println("El área es nula en usuariosDto");
        }

    }

    private void chargeUser(Long id) {
        try {
            UsersService service = new UsersService();
            Respuesta respuesta = service.obtenerUsuario(id);
            if (respuesta.getEstado()) {
                unbindUser();
                this.usuariosDto = (UsersDto) respuesta.getResultado("Usuario");
                bindUser(false);
                validarRequeridos();
                chargeRoles();
                AreasDto areaSeleccionada = usuariosDto.getAreas();
                if (areaSeleccionada != null) {
                    for (AreasDto area : cmbAreas.getItems()) {
                        if (area.getId().equals(areaSeleccionada.getId())) {
                            cmbAreas.getSelectionModel().selectItem(area);
                            break;
                        }
                    }
                }
                cmbLan.getSelectionModel().selectItem(usuariosDto.getLanguage());
            } else {
                new Mensaje().showModal(AlertType.INFORMATION, bundle.getString("buscarUsuario"), getStage(),
                        respuesta.getMensaje());
            }
        } catch (Exception e) {
            Logger.getLogger(AdminUsersController.class.getName()).log(Level.SEVERE,
                    bundle.getString("errorBuscarUsuario"), e);
            new Mensaje().showModal(AlertType.ERROR, bundle.getString("buscarUsuario"), getStage(),
                    bundle.getString("errorBuscarUsuario"));
        }
    }

    private void chargeSistem(Long id) {
        try {
            SystemsService service = new SystemsService();
            Respuesta res = service.obtenerSystem(id);
            if (!res.getEstado()) {
                new Mensaje().showModal(AlertType.ERROR, bundle.getString("cargarSistema"), getStage(),
                        res.getMensaje());
            } else {
                unbindSystems();
                this.systems = (SystemsDto) res.getResultado("Sistema");
                bindSystems(false);
            }
        } catch (Exception e) {
            Logger.getLogger(AdminSystemController.class.getName()).log(Level.SEVERE,
                    bundle.getString("errorCargarSistema"), e);
            new Mensaje().showModal(AlertType.ERROR, bundle.getString("cargarSistema"), getStage(),
                    bundle.getString("errorCargarSistema"));
        }
    }

    private void chargeAreas() {
        try {
            AreasService service = new AreasService();
            Respuesta respuesta = service.getAreas();
            if (respuesta.getEstado()) {
                cmbAreas.setItems(FXCollections.observableArrayList((List<AreasDto>) respuesta.getResultado("Areas")));
            } else {
                new Mensaje().showModal(AlertType.INFORMATION, bundle.getString("cargarAreas"), getStage(),
                        respuesta.getMensaje());
            }
        } catch (Exception e) {
            Logger.getLogger(AdminUsersController.class.getName()).log(Level.SEVERE,
                    bundle.getString("errorCargarAreas"), e);
            new Mensaje().showModal(AlertType.ERROR, bundle.getString("cargarAreas"), getStage(),
                    bundle.getString("errorCargarAreas"));
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
                new Mensaje().showModal(AlertType.INFORMATION, bundle.getString("rolesUsuario"), getStage(),
                        bundle.getString("debeSeleccionarUsuario"));
                tbpUsuarios.getSelectionModel().select(tptMantenimiento);
            } else {
                new Mensaje().showModal(AlertType.INFORMATION, bundle.getString("inclusion"), getStage(),
                        bundle.getString("soloUnRol"));
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
            new Mensaje().showModal(AlertType.ERROR, bundle.getString("agregarRol"), getStage(),
                    bundle.getString("necesarioSistemaRol"));
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
                new Mensaje().showModal(AlertType.INFORMATION, bundle.getString("eliminarUsuario"), getStage(),
                        bundle.getString("noSeleccionoUsuario"));
            } else {
                UsersService service = new UsersService();
                Respuesta respuesta = service.eliminarUsuario(this.usuariosDto.getId());
                if (!respuesta.getEstado()) {
                    new Mensaje().showModal(AlertType.INFORMATION, bundle.getString("eliminarUsuario"), getStage(),
                            respuesta.getMensaje());
                } else {
                    new Mensaje().showModal(AlertType.INFORMATION, bundle.getString("eliminarUsuario"), getStage(),
                            bundle.getString("usuarioEliminado"));
                    newUser();
                }
            }
        } catch (Exception e) {
            Logger.getLogger(AdminUsersController.class.getName()).log(Level.SEVERE,
                    bundle.getString("errorEliminarUsuario"), e);
            new Mensaje().showModal(AlertType.ERROR, bundle.getString("eliminarUsuario"), getStage(),
                    bundle.getString("errorEliminarUsuario"));
        }
    }

    @FXML
    void onActionBtnNew(ActionEvent event) {
        if (tptRoles.isSelected()) {
            newSystem();
        } else if (tptMantenimiento.isSelected()) {
            if (new Mensaje().showConfirmation(bundle.getString("limpiarUsuario"), getStage(),
                    bundle.getString("confirmarLimpiarUsuario"))) {
                newUser();
                tbvRoles.getItems().clear();
            }
        }
    }

    @FXML
    void onActionBtnSearch(ActionEvent event) {
        // Obtén el controlador de la vista SearchView
        SearchViewController controller = (SearchViewController) FlowController.getInstance()
                .getController("SearchView");

        // Configura los tabs según la selección actual
        if (tptMantenimiento.isSelected()) {
            controller.tptSystems.setDisable(true);
            controller.tptUsers.setDisable(false);
        } else if (tptRoles.isSelected()) {
            controller.tptUsers.setDisable(true);
            controller.tptSystems.setDisable(false);
        }

        // Abre la vista SearchView en modo modal
        FlowController.getInstance().goViewInWindowModal("SearchView", getStage(), false);

        // Procesa el resultado después de cerrar la vista modal
        Object result = controller.getResult();
        if (tptMantenimiento.isSelected() && result instanceof UsersDto) {
            UsersDto user = (UsersDto) result;
            if (user != null) {
                chargeUser(user.getId());
            }
        } else if (tptRoles.isSelected() && result instanceof SystemsDto) {
            SystemsDto system = (SystemsDto) result;
            if (system != null) {
                chargeSistem(system.getId());
            }
        }
    }

    @FXML
    void onActionBtnSave(ActionEvent event) {
        try {
            String validation = validarRequeridos();
            if (!validation.isEmpty() && imgViewUser.getImage() == null) {
                new Mensaje().showModal(AlertType.WARNING, bundle.getString("guardarUsuario"), getStage(), validation);
            } else {
                if (this.usuariosDto.getId() == null) {
                    this.usuariosDto.setState("I");
                }
                List<RolesDto> rolesList = new ArrayList<>();
                for (SystemsDto sistema : tbvUsers.getItems()) {
                    RolesDto selectedRole = (RolesDto) tbcRol.getCellData(sistema);
                    if (selectedRole != null) {
                        rolesList.add(selectedRole);
                    }
                }
                this.usuariosDto.setRoles(rolesList);
                AreasDto area = cmbAreas.getSelectionModel().getSelectedItem();
                if (area != null) {
                    this.usuariosDto.setAreas(area);
                }
                UsersService service = new UsersService();
                Respuesta respuesta = service.guardarUsuario(usuariosDto);
                if (!respuesta.getEstado()) {
                    new Mensaje().showModal(AlertType.INFORMATION, bundle.getString("guardarUsuario"), getStage(),
                            respuesta.getMensaje());
                } else {
                    unbindUser();
                    this.usuariosDto = (UsersDto) respuesta.getResultado("Usuario");
                    bindUser(false);
                    new Mensaje().showModal(AlertType.INFORMATION, bundle.getString("guardarUsuario"), getStage(),
                            bundle.getString("usuarioGuardado"));
                }
                AppContext.getInstance().set("Taken", false);
            }
        } catch (Exception e) {
            Logger.getLogger(AdminUsersController.class.getName()).log(Level.SEVERE,
                    bundle.getString("errorGuardarUsuario"), e);
            new Mensaje().showModal(AlertType.ERROR, bundle.getString("guardarUsuario"), getStage(),
                    bundle.getString("errorGuardarUsuario"));
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
                    new Mensaje().showModal(AlertType.ERROR, bundle.getString("errorCargaImagen"), getStage(),
                            bundle.getString("errorCargaImagen"));
                }
            } else {
                new Mensaje().showModal(AlertType.WARNING, bundle.getString("errorCargaImagen"), getStage(),
                        bundle.getString("archivoFotoNoExiste"));
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
                rol.getUsers().remove(usuariosDto);
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
