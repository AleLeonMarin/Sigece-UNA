package cr.ac.una.chats.controller;

import cr.ac.una.chats.model.UsersDto;
import cr.ac.una.chats.service.ChatsService;
import cr.ac.una.chats.util.AppContext;
import cr.ac.una.chats.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ListaContactosController extends Controller implements Initializable {

    @FXML
    private TableView<UsersDto> tbvContactos;
    @FXML
    private TableColumn<UsersDto, String> tbcContactos;

    private ObservableList<UsersDto> listaUsuarios;
    private ChatsService chatsService = new ChatsService();

    @FXML
    private MFXButton btnSearch;

    @FXML
    private MFXTextField txtSearch;

    ResourceBundle bundle;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bundle = rb;
        tbcContactos.setCellFactory(column -> new TableCell<UsersDto, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    UsersDto usuario = getTableRow().getItem();
                    HBox hbox = new HBox(10);
                    ImageView imageView = new ImageView();
                    if (usuario.getUsuFotoBase64() != null && !usuario.getUsuFotoBase64().isEmpty()) {
                        try {
                            byte[] imageBytes = usuario.getPhoto();
                            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
                            imageView.setImage(new Image(bis));
                        } catch (IllegalArgumentException e) {
                            imageView.setImage(new Image("src/main/resources/cr/ac/una/chatsapp/resources/add-user.png"));
                        }
                    } else {
                        imageView.setImage(new Image("src/main/resources/cr/ac/una/chatsapp/resources/add-user.png"));
                    }
                    imageView.setFitHeight(40);
                    imageView.setFitWidth(40);
                    Label nombreLabel = new Label(usuario.getName() + " " + usuario.getLastNames());
                    Label estadoLabel = new Label(usuario.getStatus());
                    estadoLabel.setStyle("-fx-text-fill: #00bfff;");
                    hbox.getChildren().addAll(imageView, nombreLabel, estadoLabel);
                    setGraphic(hbox);
                }
            }
        });

        tbvContactos.setRowFactory(tv -> {
            TableRow<UsersDto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    UsersDto usuarioSeleccionado = row.getItem();
                    AppContext.getInstance().set("usuarioSeleccionado", usuarioSeleccionado);
                    getStage().close();
                }
            });
            return row;
        });

        cargarUsuarios();
    }

    @Override
    public void initialize() {
    }

    private void cargarUsuarios() {
        Respuesta respuesta = chatsService.getUsuarios();
        if (respuesta.getEstado()) {
            List<UsersDto> usuarios = (List<UsersDto>) respuesta.getResultado("Usuarios");

            UsersDto usuarioActual = (UsersDto) AppContext.getInstance().get("UsuarioActual");

            List<UsersDto> usuariosFiltrados = usuarios.stream()
                    .filter(usuario -> "A".equals(usuario.getState()))
                    .filter(usuario -> !usuario.getId().equals(usuarioActual.getId()))
                    .collect(Collectors.toList());

            listaUsuarios = FXCollections.observableArrayList(usuariosFiltrados);
            tbvContactos.setItems(listaUsuarios);
        } else {
            System.out.println("Error obteniendo los usuarios: " + respuesta.getMensaje());
        }
    }


    @FXML
    void onActionBtnSearch() {
        String searchText = txtSearch.getText().toLowerCase();

        if (searchText.isEmpty()) {
            tbvContactos.setItems(listaUsuarios);
        } else {
            ObservableList<UsersDto> listaFiltrada = listaUsuarios.stream()
                    .filter(usuario -> usuario.getName().toLowerCase().contains(searchText) ||
                            usuario.getLastNames().toLowerCase().contains(searchText))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));

            tbvContactos.setItems(listaFiltrada);
            tbvContactos.refresh();
        }
    }

}
