package cr.ac.una.chats.model;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import java.util.Date;
import java.util.List;

public class ChatsDto {

    public SimpleStringProperty id;
    public SimpleObjectProperty<Date> date;
    public UsersDto receptor;
    public UsersDto emisor;
    public SimpleLongProperty version;
    public List<MessagesDto> messages;

    public ChatsDto() {
        this.id = new SimpleStringProperty("");
        this.date = new SimpleObjectProperty<>(new Date());
        this.receptor = new UsersDto();
        this.emisor = new UsersDto();
        this.version = new SimpleLongProperty(1L);
        this.messages = FXCollections.observableArrayList();
    }

    public Long getId() {
        if (this.id.get() != null && !this.id.get().isEmpty()) {
            return Long.valueOf(this.id.get());
        } else {
            return null;
        }
    }

    public void setId(Long id) {
        this.id.set(id.toString());
    }

    public Date getDate() {
        return date.get();
    }

    public void setDate(Date date) {
        this.date.set(date);
    }

    public UsersDto getReceptor() {
        return receptor;
    }

    public void setReceptor(UsersDto receptor) {
        this.receptor = receptor;
    }

    public UsersDto getEmisor() {
        return emisor;
    }

    public void setEmisor(UsersDto emisor) {
        this.emisor = emisor;
    }

    public Long getVersion() {
        return version.get();
    }

    public void setVersion(Long version) {
        this.version.set(version);
    }

    public List<MessagesDto> getMessages() {
        return messages;
    }

    public void setMessages(List<MessagesDto> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "ChatsDto{" +
                "id=" + id.get() +
                ", date=" + date.get() +
                ", receptor=" + receptor +
                ", emisor=" + emisor +
                ", version=" + version.get() +
                '}';
    }
}
