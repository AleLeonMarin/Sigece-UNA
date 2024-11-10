package cr.ac.una.admin.model;

import java.io.Serializable;
import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ApprovalsDto implements Serializable {

    public SimpleStringProperty id;
    public SimpleBooleanProperty state;
    public SimpleStringProperty description;
    public SimpleStringProperty comment;
    public SimpleStringProperty solution;
    public ObjectProperty<LocalDate> date;
    public ObjectProperty<byte[]> archive;
    public Long version;
    public UsersDto users;
    public GestionsDto gestion;

    public ApprovalsDto() {

        this.id = new SimpleStringProperty("");
        this.state = new SimpleBooleanProperty(true);
        this.description = new SimpleStringProperty("");
        this.comment = new SimpleStringProperty("");
        this.solution = new SimpleStringProperty("");
        this.date = new SimpleObjectProperty<>(LocalDate.now());
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

    public String getState() {
        return state.get() ? "A" : "R";
    }

    public void setState(String state) {
        this.state.set(state.equals("A"));
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getComment() {
        return comment.get();
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }

    public String getSolution() {
        return solution.get();
    }

    public void setSolution(String solution) {
        this.solution.set(solution);
    }

    public LocalDate getDate() {
        return date.get();
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
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

    public UsersDto getUsers() {
        return users;
    }

    public void setUsers(UsersDto users) {
        this.users = users;
    }

    public GestionsDto getGestion() {
        return gestion;
    }

    public void setGestion(GestionsDto gestion) {
        this.gestion = gestion;
    }

}
