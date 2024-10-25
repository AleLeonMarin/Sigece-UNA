package cr.ac.una.chats.controller;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;


import cr.ac.una.chats.model.ChatsDto;
import cr.ac.una.chats.model.MessagesDto;
import cr.ac.una.chats.model.UsersDto;
import cr.ac.una.chats.service.ChatsService;
import cr.ac.una.chats.service.MensajesService;
import cr.ac.una.chats.service.UsuariosServiceRest;
import cr.ac.una.chats.util.AppContext;
import cr.ac.una.chats.util.FlowController;
import cr.ac.una.chats.util.Mensaje;
import cr.ac.una.chats.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.util.Callback;
import javafx.util.Duration;

public class ChatsAppController extends Controller implements Initializable {

    @FXML
    private TableView<UsersDto> tbvContactos;
    @FXML
    private TableColumn<UsersDto, String> tbcContactos;
    @FXML
    private VBox vboxChats;

    private ChatsService chatsService = new ChatsService();

    private UsuariosServiceRest usuariosService = new UsuariosServiceRest();

    private Long idEmisor;

    @FXML
    private MFXTextField txtMensaje;

    @FXML
    private MFXButton BtnDeleteChat;

    private ChatsDto currentChat;

    private Timeline timeline;

    @FXML
    private MFXButton btnGuardarEstado;

    @FXML
    private MFXTextField txtEstado;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idEmisor = obtenerIdEmisorActual();
        cargarUsuarios();

        tbvContactos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                cargarChatConUsuario(newValue.getId());
            }
        });

        // Configurar la columna
        tbcContactos.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName() + " " + cellData.getValue().getLastNames()));

        // Usar cellFactory para mostrar la imagen y el estado
        tbcContactos.setCellFactory(column -> new TableCell<UsersDto, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null) {
                    setGraphic(null);
                } else {
                    UsersDto usuario = getTableRow().getItem();
                    HBox hbox = new HBox(10);
                    ImageView imageView = new ImageView();

                    // Cargar imagen
                    if (usuario.getUsuFotoBase64() != null && !usuario.getUsuFotoBase64().isEmpty()) {
                        byte[] imageBytes = usuario.getPhoto();
                        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
                        imageView.setImage(new Image(bis));
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
    }

    @FXML
    private MFXButton btnSend;

    @FXML
    public void initialize() {

    }

    private Long obtenerIdEmisorActual() {
        return ((UsersDto) AppContext.getInstance().get("UsuarioActual")).getId();
    }

    private void cargarUsuarios() {
        Respuesta respuesta = chatsService.getUsuarios();
        if (respuesta.getEstado()) {
            List<UsersDto> usuarios = (List<UsersDto>) respuesta.getResultado("Usuarios");
            List<UsersDto> usuariosConChats = new ArrayList<>();
            System.out.println("Número de usuarios obtenidos: " + usuarios.size());
            for (UsersDto usuario : usuarios) {
                System.out.println("Usuario: " + usuario.getName() + " " + usuario.getLastNames());
            }
            for (UsersDto usuario : usuarios) {
                if ("A".equals(usuario.getState()) && !idEmisor.equals(usuario.getId())) {
                    Respuesta respuestaChat = chatsService.getChatsEntreUsuarios(idEmisor, usuario.getId());
                    if (respuestaChat.getEstado()) {
                        List<ChatsDto> chats = (List<ChatsDto>) respuestaChat.getResultado("Chats");
                        if (chats != null && !chats.isEmpty()) {
                            usuariosConChats.add(usuario);
                        }
                    }
                }
            }

            ObservableList<UsersDto> usuariosList = FXCollections.observableArrayList(usuariosConChats);
            tbvContactos.setItems(usuariosList);
            tbcContactos.setCellFactory(column -> new TableCell<UsersDto, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        UsersDto usuario = getTableRow().getItem();
                        System.out.println("Cargando usuario en celda: " + usuario.getName());

                        // Aquí continúa la lógica de configuración de la celda
                HBox hbox = new HBox(10);
                        ImageView imageView = new ImageView();
                        if (usuario.getUsuFotoBase64() != null && !usuario.getUsuFotoBase64().isEmpty()) {
                            byte[] imageBytes = usuario.getPhoto();
                            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
                            imageView.setImage(new Image(bis));
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

        }
    }


    private void cargarChatConUsuario(Long idReceptor) {
        vboxChats.getChildren().clear();
        currentChat = null;

        Respuesta respuesta = chatsService.getChatsEntreUsuarios(idEmisor, idReceptor);

        if (respuesta.getEstado()) {
            List<ChatsDto> chats = (List<ChatsDto>) respuesta.getResultado("Chats");
            if (chats != null && !chats.isEmpty()) {
                currentChat = chats.get(0);
                mostrarMensajesDelChat(currentChat.getMessages());
            } else {
                Label noChatLabel = new Label("No hay un chat con este usuario, se creará cuando envíes un mensaje.");
                noChatLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 14px;");
                vboxChats.getChildren().add(noChatLabel);
            }
        } else {
            System.out.println("Error obteniendo los chats: " + respuesta.getMensaje());
        }
    }

    private void mostrarMensajesDelChat(List<MessagesDto> mensajes) {
        vboxChats.getChildren().clear();
        if (mensajes != null && !mensajes.isEmpty()) {
            mensajes.stream()
                    .sorted(Comparator.comparing(MessagesDto::getDate))
                    .forEach(mensaje -> {
                        HBox hbox = new HBox();
                        Label mensajeLabel = new Label(mensaje.getText());
                        hbox.setPrefWidth(vboxChats.getPrefWidth() - 20);
                        hbox.setMaxWidth(vboxChats.getPrefWidth() - 20);
                        mensajeLabel.setWrapText(true);
                        mensajeLabel.setMaxWidth(hbox.getPrefWidth() * 0.75);

                        Button btnEliminar = new Button("Eliminar");
                        btnEliminar.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                        btnEliminar.setOnAction(event -> onActionEliminarMensaje(mensaje));

                        Long emisorIdMensaje = mensaje.getUser().getId();
                        if (emisorIdMensaje != null && emisorIdMensaje.equals(idEmisor)) {
                            hbox.setAlignment(Pos.CENTER_RIGHT);
                            mensajeLabel.setStyle("-fx-background-color: #2390b8; -fx-padding: 10px; -fx-background-radius: 10px;");
                            hbox.getChildren().addAll(mensajeLabel, btnEliminar);
                        } else {
                            hbox.setAlignment(Pos.CENTER_LEFT);
                            mensajeLabel.setStyle("-fx-background-color: lightgray; -fx-padding: 10px; -fx-background-radius: 10px;");
                            hbox.getChildren().addAll(mensajeLabel, btnEliminar);
                        }

                        vboxChats.getChildren().add(hbox);
                    });
        } else {
            Label noMessagesLabel = new Label("No hay mensajes en este chat.");
            noMessagesLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 14px;");
            vboxChats.getChildren().add(noMessagesLabel);
        }
    }



    private void iniciarActualizacionPeriodica() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> actualizarMensajes()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void actualizarMensajes() {
        Optional.ofNullable(currentChat)
                .map(chat -> chatsService.getChatsEntreUsuarios(idEmisor, chat.getReceptor().getId()))
                .filter(Respuesta::getEstado)
                .map(respuesta -> (List<ChatsDto>) respuesta.getResultado("Chats"))
                .filter(chats -> !chats.isEmpty())
                .map(chats -> chats.get(0))
                .ifPresent(nuevoChat -> {
                    List<MessagesDto> nuevosMensajes = nuevoChat.getMessages();
                    if (!nuevosMensajes.equals(currentChat.getMessages())) {
                        currentChat = nuevoChat;
                        mostrarMensajesDelChat(nuevosMensajes);
                    }
                });
    }


    @FXML
    void onActonBtnSend(ActionEvent event) {
        String textoMensaje = txtMensaje.getText();

        if (textoMensaje.isEmpty()) {
            new Mensaje().show(Alert.AlertType.WARNING, "Advertencia", "El campo de mensaje no puede estar vacío.");
            return;
        }

        if (tbvContactos.getSelectionModel().getSelectedItem() == null) {
            new Mensaje().show(Alert.AlertType.WARNING, "Advertencia", "No se ha seleccionado ningún chat.");
            return;
        }

        Long idReceptor = tbvContactos.getSelectionModel().getSelectedItem().getId();
        if (idReceptor == null) {
            System.out.println("Error: No se ha seleccionado ningún contacto.");
            return;
        }

        if (currentChat == null) {
            ChatsDto nuevoChat = new ChatsDto();
            UsersDto emisor = new UsersDto();
            UsersDto receptor = new UsersDto();

            emisor.setUser(obtenerIdEmisorActual().toString());
            receptor.setUser(idReceptor.toString());

            nuevoChat.setEmisor(emisor);
            nuevoChat.setReceptor(receptor);

            Respuesta respuestaChat = chatsService.guardarChat(nuevoChat);
            if (respuestaChat.getEstado()) {
                currentChat = (ChatsDto) respuestaChat.getResultado("Chat");
                System.out.println("Nuevo chat creado con éxito.");
            } else {
                System.out.println("Error creando el chat: " + respuestaChat.getMensaje());
                return;
            }
        }

        MessagesDto mensajeDto = new MessagesDto();
        mensajeDto.setText(textoMensaje);

        UsersDto emisor = new UsersDto();
        UsersDto receptor = new UsersDto();
        receptor.setUser(idReceptor.toString());
        emisor.setUser(obtenerIdEmisorActual().toString());
        mensajeDto.setUser(emisor);
        mensajeDto.setChat(currentChat);

        MensajesService mensajesService = new MensajesService();
        Respuesta respuesta = mensajesService.guardarMensaje(mensajeDto);

        if (respuesta.getEstado()) {
            System.out.println("Mensaje enviado correctamente.");

            HBox hbox = new HBox();
            hbox.setPrefWidth(vboxChats.getPrefWidth() - 20);
            hbox.setMaxWidth(vboxChats.getPrefWidth() - 20);
            hbox.setAlignment(Pos.CENTER_RIGHT);

            Label mensajeLabel = new Label(textoMensaje);
            mensajeLabel.setWrapText(true);
            mensajeLabel.setMaxWidth(hbox.getPrefWidth() * 0.75);
            mensajeLabel.setStyle("-fx-background-color: #2390b8; -fx-padding: 10px; -fx-background-radius: 10px;");

            Button btnEliminar = new Button("Eliminar");
            btnEliminar.setStyle("-fx-background-color: red; -fx-text-fill: white;");
            btnEliminar.setOnAction(e -> onActionEliminarMensaje(mensajeDto));

            hbox.getChildren().addAll(mensajeLabel, btnEliminar);

            vboxChats.getChildren().add(hbox);

            txtMensaje.clear();
        } else {
            System.out.println("Error enviando el mensaje: " + respuesta.getMensaje());
        }
    }



    @FXML
    void onActionBtnDeleteChat(ActionEvent event) {
        if (currentChat == null) {
            tbvContactos.getItems().remove(tbvContactos.getSelectionModel().getSelectedItem());
            tbvContactos.refresh();
            Mensaje mensaje = new Mensaje();
            mensaje.show(Alert.AlertType.WARNING, "Advertencia", "No hay ningún chat seleccionado para eliminar.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar chat");
        alert.setHeaderText("¿Está seguro de que desea eliminar este chat?");
        alert.setContentText("Esta acción no se puede deshacer.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            Respuesta respuesta = chatsService.eliminarChat(currentChat.getId().toString());

            if (respuesta.getEstado()) {
                Mensaje mensaje = new Mensaje();
                mensaje.show(Alert.AlertType.INFORMATION, "Éxito", "El chat ha sido eliminado correctamente.");

                tbvContactos.getItems().remove(tbvContactos.getSelectionModel().getSelectedItem());
                tbvContactos.refresh();

                currentChat = null;
                vboxChats.getChildren().clear();
                Label noChatLabel = new Label("El chat ha sido eliminado.");
                noChatLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 14px;");
                vboxChats.getChildren().add(noChatLabel);
            } else {
                Mensaje mensaje = new Mensaje();
                mensaje.show(Alert.AlertType.ERROR, "Error", "Ocurrió un error al eliminar el chat: " + respuesta.getMensaje());
            }
        } else {
            System.out.println("Eliminación de chat cancelada.");
        }
    }


    private void onActionEliminarMensaje(MessagesDto mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar mensaje");
        alert.setHeaderText("¿Está seguro de que desea eliminar este mensaje?");
        alert.setContentText("Esta acción no se puede deshacer.");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            MensajesService mensajesService = new MensajesService();
            Respuesta respuesta = mensajesService.eliminarMensaje(mensaje.getId());

            if (respuesta.getEstado()) {
                Mensaje mensajeAlerta = new Mensaje();
                mensajeAlerta.show(Alert.AlertType.INFORMATION, "Éxito", "El mensaje ha sido eliminado correctamente.");
                currentChat.setMessages(currentChat.getMessages().stream()
                        .filter(m -> !m.equals(mensaje))
                        .collect(Collectors.toList()));
                mostrarMensajesDelChat(currentChat.getMessages());

            } else {
                Mensaje mensajeAlerta = new Mensaje();
                mensajeAlerta.show(Alert.AlertType.ERROR, "Error", "Ocurrió un error al eliminar el mensaje: " + respuesta.getMensaje());
            }
        }
        //quitar seleccion de la tbv
        tbvContactos.getSelectionModel().clearSelection();
    }

    @FXML
    void onActionBtnNewChat(ActionEvent event) {
        vboxChats.getChildren().clear();
        tbvContactos.getSelectionModel().clearSelection();
        currentChat = null;

        FlowController.getInstance().goViewInWindowModal("ListaContactosView", this.getStage(), Boolean.TRUE);

        UsersDto usuarioSeleccionado = (UsersDto) AppContext.getInstance().get("usuarioSeleccionado");

        if (usuarioSeleccionado != null) {
            ObservableList<UsersDto> usuariosList = tbvContactos.getItems();

            boolean usuarioYaEnLista = usuariosList.stream()
                    .anyMatch(usuario -> usuario.getId().equals(usuarioSeleccionado.getId()));

            if (!usuarioYaEnLista) {
                usuariosList.add(usuarioSeleccionado);
                tbvContactos.setItems(usuariosList);
                tbvContactos.refresh();

            } else {
                new Mensaje().show(Alert.AlertType.INFORMATION, "Información", "YA TIENES UN CHAT CON ESTE USUARIO.");
            }
        }
    }


    @FXML
    void onBtnGuardarEstado(ActionEvent event) {
        String nuevoEstado = txtEstado.getText();
        if (nuevoEstado == null || nuevoEstado.isEmpty()) {
            new Mensaje().show(Alert.AlertType.WARNING, "Advertencia", "El estado no puede estar vacío.");
            return;
        }

        UsersDto usuarioActual = (UsersDto) AppContext.getInstance().get("UsuarioActual");

        usuarioActual.setStatus(nuevoEstado);

        Respuesta respuesta = usuariosService.actualizarEstadoUsuario(usuarioActual);
        if (respuesta.getEstado()) {
            new Mensaje().show(Alert.AlertType.INFORMATION, "Éxito", "El estado ha sido actualizado correctamente.");
        } else {
            new Mensaje().show(Alert.AlertType.ERROR, "Error", "Ocurrió un error al actualizar el estado: " + respuesta.getMensaje());
        }
    }
}

