package cr.ac.una.chats.model;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import java.io.Serializable;
import java.util.Date;

public class MessagesDto {

    public SimpleStringProperty id;
    public SimpleStringProperty text;
    public SimpleObjectProperty<Serializable> archive;
    public SimpleObjectProperty<Date> date;
    public ChatsDto chat;
    public UsersDto user;
    public SimpleLongProperty version;

    public MessagesDto() {
        this.id = new SimpleStringProperty("");
        this.text = new SimpleStringProperty("");
        this.archive = new SimpleObjectProperty<>(null);
        this.date = new SimpleObjectProperty<>(new Date());
        this.chat = new ChatsDto();
        this.user = new UsersDto();
        this.version = new SimpleLongProperty(1L);
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

    public String getText() {
        return text.get();
    }

    public void setText(String text) {
        this.text.set(text);
    }

    public Serializable getArchive() {
        return archive.get();
    }

    public void setArchive(Serializable archive) {
        this.archive.set(archive);
    }

    public Date getDate() {
        return date.get();
    }

    public void setDate(Date date) {
        this.date.set(date);
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
        return version.get();
    }

    public void setVersion(Long version) {
        this.version.set(version);
    }

    @Override
    public String toString() {
        return "MessagesDto{" +
                "id=" + id.get() +
                ", text='" + text.get() + '\'' +
                ", archive=" + archive.get() +
                ", date=" + date.get() +
                ", chat=" + chat +
                ", user=" + user +
                ", version=" + version.get() +
                '}';
    }
}
