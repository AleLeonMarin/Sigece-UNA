package cr.ac.una.admin.model;

import java.io.Serializable;
import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;

public class FollowsDto implements Serializable {
    private SimpleStringProperty id;
    private ObjectProperty<LocalDate> date;
    private SimpleStringProperty description;
    private ObjectProperty<byte[]> archive;
    private Long version;
    private GestionsDto Gestions;
    private UsersDto Users;

    public FollowsDto() {

        this.id = new SimpleStringProperty("");
        this.date = new SimpleObjectProperty<>(LocalDate.now());
        this.description = new SimpleStringProperty("");
        this.archive = new SimpleObjectProperty<>(new byte[0]);
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

}
