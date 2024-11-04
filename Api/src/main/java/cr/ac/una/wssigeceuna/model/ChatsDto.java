package cr.ac.una.wssigeceuna.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.annotation.JsonbTransient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonbPropertyOrder({
    "id",
    "date",
    "receptor",
    "emisor",
    "version"
})

@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id"
)

@Schema(description = "Esta clase contiene la informacion de los chats")
public class ChatsDto implements Serializable {

    @Schema(description = "Identificador del chat", example = "1")
    private Long id;

    @Schema(description = "Fecha del chat", example = "2021-09-01")
    private Date date;

    
    @Schema(description = "Receptor del chat")
    private Users receptor;


    @Schema(description = "Emisor del chat")
    private Users emisor;

    @Schema(description = "Version del chat", example = "1")
    private Long version;

    private List<MessagesDto> messages;

    public ChatsDto() {
        messages = new ArrayList<>();
    }

    public ChatsDto(Chats chats) {
        this();
        this.id = chats.getId();
        this.date = chats.getDate();
        this.receptor = chats.getReceptor();
        this.emisor = chats.getEmisor();
        this.version = chats.getVersion();
        
           if (chats.getMessages() != null) {
        this.messages = new ArrayList<>();
        for (Messages message : chats.getMessages()) {
            MessagesDto messageDto = new MessagesDto();
            messageDto.setId(message.getId());
            messageDto.setText(message.getText());
            this.messages.add(messageDto);
        }
    }
           
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Users getReceptor() {
        return receptor;
    }

    public void setReceptor(Users receptor) {
        this.receptor = receptor;
    }

    public Users getEmisor() {
        return emisor;
    }

    public void setEmisor(Users emisor) {
        this.emisor = emisor;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<MessagesDto> getMessages() {
        return messages;
    }

    public void setMessages(List<MessagesDto> messages) {
        this.messages = messages;
    }

}
