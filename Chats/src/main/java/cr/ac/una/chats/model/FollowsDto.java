package cr.ac.una.chats.model;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.util.Date;

public class FollowsDto implements Serializable {

    private SimpleStringProperty id;
    private SimpleObjectProperty<Date> date;
    private SimpleStringProperty description;
    private SimpleStringProperty archive;
    private SimpleStringProperty version;
    private GestionsDto gestions;
    private UsersDto users;

    public FollowsDto() {
        this.id = new SimpleStringProperty("");
        this.date = new SimpleObjectProperty<>(new Date());
        this.description = new SimpleStringProperty("");
        this.archive = new SimpleStringProperty("");
        this.version = new SimpleStringProperty("1");
    }


    // Getters y Setters con propiedades simples
    public Long getId() {
        if (this.id.get() != null && !this.id.get().isEmpty()) {
            return Long.valueOf(this.id.get());
        } else {
            return null;
        }
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public Date getDate() {
        return date.get();
    }

    public void setDate(Date date) {
        this.date.set(date);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getArchive() {
        return archive.get();
    }

    public void setArchive(String archive) {
        this.archive.set(archive);
    }

    public String getVersion() {
        return version.get();
    }

    public void setVersion(String version) {
        this.version.set(version);
    }

    public GestionsDto getGestions() {
        return gestions;
    }

    public void setGestions(GestionsDto gestions) {
        this.gestions = gestions;
    }

    public UsersDto getUsers() {
        return users;
    }

    public void setUsers(UsersDto users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "FollowsDto{" +
                "id=" + id.get() +
                ", date=" + date.get() +
                ", description='" + description.get() + '\'' +
                ", archive='" + archive.get() + '\'' +
                ", version=" + version.get() +
                ", gestions=" + gestions +
                ", users=" + users +
                '}';
    }
}
