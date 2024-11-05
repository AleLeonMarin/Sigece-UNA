package cr.ac.una.wssigeceuna.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import java.io.Serializable;
import java.util.Date;
import jakarta.json.bind.annotation.JsonbTransient;

@JsonbPropertyOrder({ "id", "text", "archive", "date", "chat", "user", "version" })
@Schema(description = "Esta clase contiene la informacion de los mensajes")
public class MessagesDto implements Serializable {

    @Schema(description = "Identificador del mensaje", example = "1")
    private Long id;
    @Schema(description = "Texto del mensaje", example = "Hola")
    private String text;
    @Schema(description = "Archivo del mensaje")
    private byte[] archive;
    @Schema(description = "Fecha del mensaje", example = "2021-09-01")
    private Date date;
    @Schema(description = "Chat al que pertenece el mensaje")
    private ChatsDto chat;
    @Schema(description = "Usuario que envio el mensaje")
    private UsersDto user;
    @Schema(description = "Version del mensaje", example = "1")
    private Long version;
    
    
    private String extension;
    private String mimeType;

    public MessagesDto() {
    }

    public MessagesDto(Messages messages) {
        this.id = messages.getId();
        this.text = messages.getText();
        this.archive = messages.getArchive();
        this.date = messages.getDate();
        this.user = new UsersDto(messages.getUser());
        this.version = messages.getVersion();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public byte[] getArchive() {
        return archive;
    }

    public void setArchive(byte[] archive) {
        this.archive = archive;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ChatsDto getChat() {
        return chat;
    }

    public void setChat(ChatsDto chat) {
        this.chat = chat;
    }

    public UsersDto getUser() {
        return user;
    }

    public void setUser(UsersDto user) {
        this.user = user;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
    
      public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }

}
