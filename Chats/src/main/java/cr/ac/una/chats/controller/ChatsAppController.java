package cr.ac.una.chats.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
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
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.Duration;

import javax.sound.sampled.*;

public class ChatsAppController extends Controller implements Initializable {

    @FXML
    private TableView<UsersDto> tbvContactos;
    @FXML
    private TableColumn<UsersDto, String> tbcContactos;
    @FXML
    private VBox vboxChats;

    private byte[] archivoAdjunto;
    private String nombreArchivoAdjunto;

    @FXML
    private MFXButton btnAttach;

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

    @FXML
    private MFXButton btnVoiceRecorder;

    ResourceBundle bundle;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.bundle = rb;
        idEmisor = obtenerIdEmisorActual();
        cargarUsuarios();

        txtEstado.setText(((UsersDto) AppContext.getInstance().get("UsuarioActual")).getStatus());

        tbvContactos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                cargarChatConUsuario(newValue.getId());
                iniciarActualizacionPeriodica();
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

                // Realizar una consulta específica para obtener los mensajes del chat
                MensajesService mensajesService = new MensajesService();
                Respuesta respuestaMensajes = mensajesService.getMensajesByChat(currentChat.getId());

                if (respuestaMensajes.getEstado()) {
                    List<MessagesDto> mensajes = (List<MessagesDto>) respuestaMensajes.getResultado("Mensajes");
                    currentChat.setMessages(mensajes);
                    mostrarMensajesDelChat(mensajes);
                } else {
                    System.out.println("Error obteniendo los mensajes: " + respuestaMensajes.getMensaje());
                }
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
                        HBox hbox = new HBox(10); // Contenedor principal para cada mensaje
                        hbox.setPrefWidth(vboxChats.getPrefWidth() - 20);
                        hbox.setMaxWidth(vboxChats.getPrefWidth() - 20);

                        Label mensajeLabel = new Label(mensaje.getText());
                        mensajeLabel.setWrapText(true);
                        mensajeLabel.setMaxWidth(hbox.getPrefWidth() * 0.75);

                        VBox vboxMessageContent = new VBox(5); // Contenedor para imagen y texto del mensaje
                        vboxMessageContent.getChildren().add(mensajeLabel);

                        // Si el mensaje tiene archivo adjunto, agregamos una imagen según el tipo
                        Button btnDescargar = null;
                        if (mensaje.getArchive() != null && mensaje.getArchive().length > 0) {
                            ImageView imageView = new ImageView();
                            MensajesService mensajesService = new MensajesService();
                            Respuesta respuesta = mensajesService.descargarArchivo(mensaje.getId());

                            if (respuesta.getEstado() && respuesta.getResultado("ArchivoAdjunto") != null) {
                                MessagesDto mensajeDto = (MessagesDto) respuesta.getResultado("ArchivoAdjunto");
                                String extension = mensajeDto.getExtension();
                                if (extension.equals(".png")) {
                                    imageView.setImage(new Image(new ByteArrayInputStream(mensajeDto.getArchive())));
                                } else if (extension.equals(".pdf")) {
                                    imageView.setImage(new Image("/cr/ac/una/chats/resources/pdf.png"));
                                } else if (extension.equals(".docx")) {
                                    imageView.setImage(new Image("/cr/ac/una/chats/resources/word.png"));
                                } else if (extension.equals(".xlsx")) {
                                    imageView.setImage(new Image("/cr/ac/una/chats/resources/sheets.png"));
                                } else if (extension.equals(".pptx")) {
                                    imageView.setImage(new Image("/cr/ac/una/chats/resources/ppt.png"));
                                }
                                imageView.setFitWidth(100);
                                imageView.setPreserveRatio(true);
                                vboxMessageContent.getChildren().add(0, imageView); // Imagen encima del texto
                            }

                            // Crear botón de descarga
                            btnDescargar = new Button("");
                            btnDescargar.getStyleClass().add("download-button");
                            btnDescargar.setOnAction(event -> descargarArchivoAdjunto(mensaje.getId()));
                        }

                        // Crear botón de eliminar
                        Button btnEliminar = new Button("");
                        btnEliminar.getStyleClass().add("deletechat-button");
                        btnEliminar.setOnAction(event -> onActionEliminarMensaje(mensaje));

                        Long emisorIdMensaje = mensaje.getUser().getId();

                        // Configurar orden y alineación según emisor o receptor
                        if (emisorIdMensaje != null && emisorIdMensaje.equals(idEmisor)) {
                            hbox.setAlignment(Pos.CENTER_RIGHT);
                            mensajeLabel.getStyleClass().add("chat-bubble-emisor");
                            if (btnDescargar != null) {
                                hbox.getChildren().addAll(btnDescargar, btnEliminar, vboxMessageContent); // Emisor: descargar - eliminar - mensaje
                            } else {
                                hbox.getChildren().addAll(btnEliminar, vboxMessageContent); // Si no hay descarga, solo eliminar - mensaje
                            }
                        } else {
                            hbox.setAlignment(Pos.CENTER_LEFT);
                            mensajeLabel.getStyleClass().add("chat-bubble-receptor");
                            if (btnDescargar != null) {
                                hbox.getChildren().addAll(vboxMessageContent, btnEliminar, btnDescargar); // Receptor: mensaje - eliminar - descargar
                            } else {
                                hbox.getChildren().addAll(vboxMessageContent, btnEliminar); // Si no hay descarga, solo mensaje - eliminar
                            }
                        }

                        vboxChats.getChildren().add(hbox); // Agregar el HBox al VBox principal
                    });
        } else {
            Label noMessagesLabel = new Label("No hay mensajes en este chat.");
            noMessagesLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 14px;");
            vboxChats.getChildren().add(noMessagesLabel);
        }
    }







    @FXML
    void onActonBtnSend(ActionEvent event) {
        String textoMensaje = txtMensaje.getText();

        if (textoMensaje.isEmpty()) {
            new Mensaje().show(Alert.AlertType.WARNING, bundle.getString("warningTitle"), bundle.getString("warningEmptyMessageField"));
            return;
        }

        if (tbvContactos.getSelectionModel().getSelectedItem() == null) {
            new Mensaje().show(Alert.AlertType.WARNING, bundle.getString("warningTitle"), bundle.getString("warningNoChatSelected"));
            return;
        }

        Long idReceptor = tbvContactos.getSelectionModel().getSelectedItem().getId();
        if (idReceptor == null) {
            System.out.println("Error: No se ha seleccionado ningún contacto.");
            return;
        }

        // Verificar si ya existe un chat entre el emisor y el receptor
        Respuesta respuestaChatExistente = chatsService.getChatsEntreUsuarios(idEmisor, idReceptor);
        if (respuestaChatExistente.getEstado()) {
            List<ChatsDto> chatsExistentes = (List<ChatsDto>) respuestaChatExistente.getResultado("Chats");

            if (chatsExistentes != null && !chatsExistentes.isEmpty()) {
                currentChat = chatsExistentes.get(0);
                System.out.println("Chat existente encontrado. Usando el chat con ID: " + currentChat.getId());
            } else {
                // Si no existe un chat, crear uno nuevo
                ChatsDto nuevoChat = new ChatsDto();
                UsersDto emisor = new UsersDto();
                UsersDto receptor = new UsersDto();

                emisor.setId(obtenerIdEmisorActual());
                receptor.setId(idReceptor);

                nuevoChat.setEmisor(emisor);
                nuevoChat.setReceptor(receptor);

                Respuesta respuestaNuevoChat = chatsService.guardarChat(nuevoChat);
                if (respuestaNuevoChat.getEstado()) {
                    currentChat = (ChatsDto) respuestaNuevoChat.getResultado("Chat");
                    System.out.println("Nuevo chat creado con éxito con ID: " + currentChat.getId());
                } else {
                    System.out.println("Error creando el chat: " + respuestaNuevoChat.getMensaje());
                    return;
                }
            }
        } else {
            System.out.println("Error verificando existencia del chat: " + respuestaChatExistente.getMensaje());
            return;
        }

        // Crear y enviar el mensaje
        MessagesDto mensajeDto = new MessagesDto();
        mensajeDto.setText(textoMensaje);

        if (archivoAdjunto != null) {
            mensajeDto.setArchive(archivoAdjunto);
        }

        UsersDto emisor = new UsersDto();
        UsersDto receptor = new UsersDto();
        receptor.setId(idReceptor);
        emisor.setId(obtenerIdEmisorActual());
        mensajeDto.setUser(emisor);
        mensajeDto.setChat(currentChat);

        MensajesService mensajesService = new MensajesService();
        Respuesta respuesta = mensajesService.guardarMensaje(mensajeDto);

        if (respuesta.getEstado()) {
            System.out.println("Mensaje enviado correctamente.");

            archivoAdjunto = null; // Limpiar el archivo adjunto después de enviar
            nombreArchivoAdjunto = null;
            txtMensaje.clear();

            HBox hbox = new HBox(10); // Espaciado entre elementos
            hbox.setPrefWidth(vboxChats.getPrefWidth() - 20);
            hbox.setMaxWidth(vboxChats.getPrefWidth() - 20);
            hbox.setAlignment(Pos.CENTER_RIGHT);

            Label mensajeLabel = new Label(textoMensaje);
            mensajeLabel.setWrapText(true);
            mensajeLabel.setMaxWidth(hbox.getPrefWidth() * 0.75);
            mensajeLabel.getStyleClass().add("chat-bubble-emisor"); // Aplicar estilo de burbuja del emisor

            Button btnEliminar = new Button("");
            btnEliminar.getStyleClass().add("deletechat-button");
            btnEliminar.setOnAction(e -> onActionEliminarMensaje(mensajeDto));

            hbox.getChildren().addAll(btnEliminar,mensajeLabel );

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
            new Mensaje().show(Alert.AlertType.WARNING, bundle.getString("warningTitle"), bundle.getString("warningNoChatToDelete"));
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(bundle.getString("deleteChatTitle"));
        alert.setHeaderText(bundle.getString("deleteChatHeader"));
        alert.setContentText(bundle.getString("deleteChatContent"));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Respuesta respuesta = chatsService.eliminarChat(currentChat.getId().toString());

            if (respuesta.getEstado()) {
                new Mensaje().show(Alert.AlertType.INFORMATION, bundle.getString("successTitle"), bundle.getString("successChatDeleted"));
                tbvContactos.getItems().remove(tbvContactos.getSelectionModel().getSelectedItem());
                tbvContactos.refresh();

                currentChat = null;
                vboxChats.getChildren().clear();
                Label noChatLabel = new Label(bundle.getString("noChatLabel"));
                noChatLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 14px;");
                vboxChats.getChildren().add(noChatLabel);
            } else {
                new Mensaje().show(Alert.AlertType.ERROR, bundle.getString("errorTitle"), bundle.getString("errorDeletingChat") + respuesta.getMensaje());
            }
        } else {
            System.out.println(bundle.getString("chatDeletionCancelled"));
        }
    }



    private void onActionEliminarMensaje(MessagesDto mensaje) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(bundle.getString("deleteMessageTitle"));
        alert.setHeaderText(bundle.getString("deleteMessageHeader"));
        alert.setContentText(bundle.getString("deleteMessageContent"));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            MensajesService mensajesService = new MensajesService();
            Respuesta respuesta = mensajesService.eliminarMensaje(mensaje.getId());

            if (respuesta.getEstado()) {
                new Mensaje().show(Alert.AlertType.INFORMATION, bundle.getString("successTitle"), bundle.getString("successMessageDeleted"));
                currentChat.setMessages(currentChat.getMessages().stream()
                        .filter(m -> !m.equals(mensaje))
                        .collect(Collectors.toList()));
                mostrarMensajesDelChat(currentChat.getMessages());
            } else {
                new Mensaje().show(Alert.AlertType.ERROR, bundle.getString("errorTitle"), bundle.getString("errorDeletingMessage") + respuesta.getMensaje());
            }
        }
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
                new Mensaje().show(Alert.AlertType.INFORMATION, bundle.getString("infoTitle"), bundle.getString("infoChatAlreadyExists"));
            }
        }
    }



    @FXML
    void onBtnGuardarEstado(ActionEvent event) {
        String nuevoEstado = txtEstado.getText();


        if (nuevoEstado == null || nuevoEstado.isEmpty()) {
            new Mensaje().show(Alert.AlertType.WARNING, bundle.getString("warningTitle"), bundle.getString("warningEmptyStatus"));
            return;
        }

        UsersDto usuarioActual = (UsersDto) AppContext.getInstance().get("UsuarioActual");

        usuarioActual.setStatus(nuevoEstado);

        Respuesta respuesta = usuariosService.actualizarEstadoUsuario(usuarioActual);
        if (respuesta.getEstado()) {
            new Mensaje().show(Alert.AlertType.INFORMATION, bundle.getString("successTitle"), bundle.getString("successStatusUpdated"));
        } else {
            new Mensaje().show(Alert.AlertType.ERROR, bundle.getString("errorTitle"), bundle.getString("errorUpdatingStatus") + respuesta.getMensaje());
        }
    }


    private void iniciarActualizacionPeriodica() {
        if (timeline != null) {
            timeline.stop(); // Detener cualquier actualización previa antes de iniciar una nueva
        }
        timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            actualizarMensajes();
            //actualizarListaDeContactosConChats();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE); // Ejecutar indefinidamente
        timeline.play();
    }

    private void actualizarMensajes() {
        if (currentChat != null) {
            MensajesService mensajesService = new MensajesService();
            Respuesta respuestaMensajes = mensajesService.getMensajesByChat(currentChat.getId());

            if (respuestaMensajes.getEstado()) {
                List<MessagesDto> nuevosMensajes = (List<MessagesDto>) respuestaMensajes.getResultado("Mensajes");

                // Solo actualizar la vista si hay mensajes nuevos
                if (!nuevosMensajes.equals(currentChat.getMessages())) {
                    currentChat.setMessages(nuevosMensajes);
                    mostrarMensajesDelChat(nuevosMensajes);
                }
            } else {
                System.out.println("Error obteniendo mensajes actualizados: " + respuestaMensajes.getMensaje());
            }
        }
    }

    private void actualizarListaDeContactosConChats() {
        Respuesta respuesta = chatsService.getUsuarios();
        if (respuesta.getEstado()) {
            List<UsersDto> usuarios = (List<UsersDto>) respuesta.getResultado("Usuarios");
            List<UsersDto> usuariosConChats = new ArrayList<>();

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

            if (!usuariosList.equals(tbvContactos.getItems())) {
                tbvContactos.setItems(usuariosList);
                tbvContactos.refresh();
            }
        } else {
            System.out.println("Error obteniendo usuarios: " + respuesta.getMensaje());
        }
    }

    @FXML
    void onActionBtnAttach(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo para adjuntar");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Todos los archivos", "*.*"),
                new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.png", "*.gif"),
                new FileChooser.ExtensionFilter("Documentos", "*.pdf", "*.docx", "*.xlsx")
        );

        File file = fileChooser.showOpenDialog(this.getStage());
        if (file != null) {
            try {
                archivoAdjunto = Files.readAllBytes(file.toPath());
                nombreArchivoAdjunto = file.getName();
                new Mensaje().show(Alert.AlertType.INFORMATION, bundle.getString("infoTitle"), "Archivo adjuntado: " + nombreArchivoAdjunto);
            } catch (IOException e) {
                new Mensaje().show(Alert.AlertType.ERROR, bundle.getString("errorTitle"), "Error al adjuntar archivo: " + e.getMessage());
                archivoAdjunto = null;
                nombreArchivoAdjunto = null;
            }
        }
    }

    private void descargarArchivoAdjunto(Long mensajeId) {
        MensajesService mensajesService = new MensajesService();
        Respuesta respuesta = mensajesService.descargarArchivo(mensajeId);

        if (!respuesta.getEstado() || respuesta.getResultado("ArchivoAdjunto") == null) {
            new Mensaje().show(Alert.AlertType.ERROR, bundle.getString("errorDownloadAttachment"), respuesta.getMensaje());
            return;
        }

        MessagesDto mensajeDto = (MessagesDto) respuesta.getResultado("ArchivoAdjunto");

        if (mensajeDto == null || mensajeDto.getArchive() == null || mensajeDto.getExtension() == null) {
            new Mensaje().show(Alert.AlertType.ERROR, bundle.getString("errorDownloadAttachment"), "Archivo no encontrado o datos incompletos.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar archivo adjunto");
        fileChooser.setInitialFileName("adjunto_" + mensajeId + mensajeDto.getExtension());
        File archivoGuardar = fileChooser.showSaveDialog(this.getStage());

        if (archivoGuardar != null) {
            try {
                Files.write(archivoGuardar.toPath(), mensajeDto.getArchive());
                new Mensaje().show(Alert.AlertType.INFORMATION, bundle.getString("successDownloadAttachment"),
                        "Archivo descargado: " + archivoGuardar.getAbsolutePath());
            } catch (IOException e) {
                new Mensaje().show(Alert.AlertType.ERROR, bundle.getString("errorDownloadAttachment"),
                        "Error al guardar el archivo: " + e.getMessage());
            }
        }
    }

    private volatile boolean grabando = false;
    private TargetDataLine line;
    private File audioFile;
    private byte[] audioAdjunto;

    @FXML
    void onActionBtnVoiceRecorder(ActionEvent event) {
        if (!grabando) {
            grabando = true;
            btnVoiceRecorder.setText("Detener");
            new Thread(this::iniciarGrabacion).start();
        } else {
            detenerGrabacion();
            grabando = false;
            btnVoiceRecorder.setText("Grabar");
        }
    }

    private void iniciarGrabacion() {
        AudioFormat format = new AudioFormat(8000, 8, 1, true, true); // 8000 Hz, 8 bits, 1 canal (mono)
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Línea no soportada");
            return;
        }

        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            AudioInputStream ais = new AudioInputStream(line);

            // Escribir datos de audio en un hilo separado
            new Thread(() -> {
                try {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while (grabando && (bytesRead = ais.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                    out.flush();
                    audioAdjunto = out.toByteArray(); // Guardar el audio en un byte array
                    System.out.println("Grabación completada.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void detenerGrabacion() {
        if (line != null && grabando) {
            grabando = false;
            line.stop();
            line.close();
            System.out.println("Grabación detenida.");
        }
    }

    // Enviar el audio como adjunto en el mensaje
    private void enviarAudio() {
        if (audioAdjunto != null) {
            MessagesDto mensajeDto = new MessagesDto();
            mensajeDto.setArchive(audioAdjunto); // Adjuntar el audio grabado
            mensajeDto.setExtension(".wav"); // Especificar la extensión

            // Enviar el mensaje como cualquier otro mensaje
            MensajesService mensajesService = new MensajesService();
            Respuesta respuesta = mensajesService.guardarMensaje(mensajeDto);

            if (respuesta.getEstado()) {
                System.out.println("Audio enviado correctamente.");
            } else {
                System.out.println("Error enviando el audio: " + respuesta.getMensaje());
            }
        } else {
            System.out.println("No hay audio para enviar.");
        }
    }

    // Reproducir el audio grabado en la aplicación
    private void reproducirAudio() {
        if (audioAdjunto != null) {
            try {
                // Convertir el byte[] de audio a un AudioInputStream
                ByteArrayInputStream bais = new ByteArrayInputStream(audioAdjunto);
                AudioInputStream ais = new AudioInputStream(bais, new AudioFormat(8000, 8, 1, true, true), audioAdjunto.length);

                // Crear un clip de audio y reproducirlo
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                clip.start();
                System.out.println("Reproduciendo audio...");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error al reproducir el audio.");
            }
        } else {
            System.out.println("No hay audio para reproducir.");
        }
    }

}






