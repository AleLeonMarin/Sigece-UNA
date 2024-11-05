package cr.ac.una.security.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import cr.ac.una.security.model.SystemsDto;
import cr.ac.una.security.model.UsersDto;
import cr.ac.una.security.service.SystemsService;
import cr.ac.una.security.service.UsersService;
import cr.ac.una.security.util.Mensaje;
import cr.ac.una.security.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class SearchViewController extends Controller implements Initializable {

    @FXML
    private MFXButton btnExit;

    @FXML
    private MFXButton btnFilter;

    @FXML
    private MFXButton btnLoad;

    @FXML
    private TableColumn<UsersDto, String> tbcIdCard;

    @FXML
    private TableColumn<UsersDto, String> tbcLastNames;

    @FXML
    private TableColumn<UsersDto, String> tbcName;

    @FXML
    private TableColumn<SystemsDto, String> tbcSystem;

    @FXML
    private TableView<UsersDto> tbvUsers;

    @FXML
    private TableView<SystemsDto> tbvSystems;

    @FXML
    private MFXTextField txfIdCard;

    @FXML
    private MFXTextField txfLastNames;

    @FXML
    private MFXTextField txfName;

    @FXML
    private MFXTextField txfNameSystem;

    @FXML
    public Tab tptSystems;

    @FXML
    public Tab tptUsers;

    UsersService userService;

    SystemsService systemService;

    ObservableList<SystemsDto> systems;

    ObservableList<UsersDto> users;

    Object result;

    ResourceBundle bundle;

    public Object getResult() {
        return result;
    }

    @FXML
    void onActionBtnExit(ActionEvent event) {
        this.getStage().close();
    }

    @FXML
    void onActionBtnFilter(ActionEvent event) {
        if (tptUsers.isSelected()) {
            loadUsers(txfName.getText(), txfLastNames.getText(), txfIdCard.getText());
        }
        if (tptSystems.isSelected()) {

            loadSystems(txfName.getText());
        }

    }

    public void loadSystems(String name) {
        Respuesta res = systemService.obtenerSystems();
        if (res.getEstado()) {
            List<SystemsDto> systemsList = (List<SystemsDto>) res.getResultado("Sistemas");
            ObservableList<SystemsDto> systemsDto = FXCollections.observableArrayList(systemsList);
            systems.clear();

            if (name != null && !name.isBlank()) {
                String nameBuscado = name.toLowerCase();
                ObservableList<SystemsDto> filteredSystems = systemsDto.stream()
                        .filter(p -> p.getName().toLowerCase().contains(nameBuscado))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
                tbvSystems.setItems(filteredSystems);
            } else {
                tbvSystems.setItems(systemsDto);
            }

            systems.addAll(systemsDto);
            tbvSystems.refresh();
        } else {
            new Mensaje().showModal(Alert.AlertType.ERROR, bundle.getString("errorTitle"),
                    getStage(), bundle.getString("loadSystemsError") + ": " + res.getMensaje());
        }
    }

    public void loadUsers(String name, String lastNames, String idCard) {
        Respuesta res = userService.getUsers();
        if (res.getEstado()) {
            ObservableList<UsersDto> usersDto = FXCollections
                    .observableArrayList((List<UsersDto>) res.getResultado("Usuarios"));
            users.clear();

            if (idCard != null && !idCard.isBlank()) {
                String idBuscado = idCard.toLowerCase();
                usersDto = usersDto.stream()
                        .filter(p -> p.getIdCard().toLowerCase().contains(idBuscado))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
            }
            if (name != null && !name.isBlank()) {
                String codigoBuscado = name.toLowerCase();
                usersDto = usersDto.stream()
                        .filter(p -> p.getName().toLowerCase().contains(codigoBuscado))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
            }
            if (lastNames != null && !lastNames.isBlank()) {
                String descripcionBuscada = lastNames.toLowerCase();
                usersDto = usersDto.stream()
                        .filter(p -> p.getLastNames().toLowerCase().contains(descripcionBuscada))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
            }

            users.addAll(usersDto);
            tbvUsers.setItems(usersDto);
            tbvUsers.refresh();

        } else {
            new Mensaje().showModal(Alert.AlertType.ERROR, bundle.getString("errorTitle"),
                    getStage(), bundle.getString("loadUsersError") + ": " + res.getMensaje());
        }
    }

    @FXML
    void onActionBtnLoad(ActionEvent event) {
        if (tptUsers.isSelected()) {
            result = tbvUsers.getSelectionModel().getSelectedItem();
            this.getStage().close();
        }
        if (tptSystems.isSelected()) {
            result = tbvSystems.getSelectionModel().getSelectedItem();
            this.getStage().close();
        }

    }

    @FXML
    void onMousePressedTbvUsers(MouseEvent event) {

        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
            onActionBtnLoad(null);
        }

    }

    @FXML
    void onMousePressedTbvSystems(MouseEvent event) {

        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
            onActionBtnLoad(null);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tbcIdCard.setCellValueFactory(u -> u.getValue().idCard);
        tbcName.setCellValueFactory(u -> u.getValue().name);
        tbcLastNames.setCellValueFactory(u -> u.getValue().lastNames);

        tbcSystem.setCellValueFactory(c -> c.getValue().name);

        bundle = resources;

    }

    @Override
    public void initialize() {
        userService = new UsersService();
        systemService = new SystemsService();
        users = FXCollections.observableArrayList();
        systems = FXCollections.observableArrayList();

    }

}
