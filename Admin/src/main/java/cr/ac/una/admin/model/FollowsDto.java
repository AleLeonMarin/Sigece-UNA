package cr.ac.una.admin.model;

import java.io.Serializable;
import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;

public class FollowsDto implements Serializable {
    public SimpleStringProperty id;
    public ObjectProperty<LocalDate> date;
    public SimpleStringProperty description;
    public ObjectProperty<byte[]> archive;
    public SimpleStringProperty state;
    public Long version;
    public GestionsDto Gestions;
    public UsersDto Users;

    public FollowsDto() {

        this.id = new SimpleStringProperty("");
        this.date = new SimpleObjectProperty<>(LocalDate.now());
        this.description = new SimpleStringProperty("");
        this.archive = new SimpleObjectProperty<>(new byte[0]);
        this.state = new SimpleStringProperty("S");
    }

    public Long getId() {
        if (id != null && id.get() != null && !id.get().isEmpty()) {
            return Long.valueOf(id.get());
        } else {
            return null;
        }
    }

    public void setId(Long id) {
        this.id.set(id.toString());
    }

    public LocalDate getDate() {
        return date.get();
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public byte[] getArchive() {
        return archive.get();
    }

    public void setArchive(byte[] archive) {
        this.archive.set(archive);
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public GestionsDto getGestions() {
        return Gestions;
    }

    public void setGestions(GestionsDto Gestions) {
        this.Gestions = Gestions;
    }

    public UsersDto getUsers() {
        return Users;
    }

    public void setUsers(UsersDto Users) {
        this.Users = Users;
    }

    public String getState() {
        return state.get();
    }

    public void setState(String state) {
        if (state.equals("A") || state.equals("R") || state.equals("S")) {
            this.state.set(state);
        } else {
            throw new IllegalArgumentException("Estado inválido: " + state);
        }
    }

}
