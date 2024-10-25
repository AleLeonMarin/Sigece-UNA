package cr.ac.una.chats.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.util.Date;

public class ApprovalsDto implements Serializable {

    private SimpleStringProperty id;
    private SimpleStringProperty state;
    private SimpleStringProperty description;
    private SimpleStringProperty comment;
    private SimpleStringProperty solution;
    private SimpleObjectProperty<Date> date;
    private SimpleObjectProperty<Serializable> archive;
    private SimpleStringProperty version;
    private UsersDto users;
    private GestionsDto gestion;

    public ApprovalsDto() {
        this.id = new SimpleStringProperty("");
        this.state = new SimpleStringProperty("");
        this.description = new SimpleStringProperty("");
        this.comment = new SimpleStringProperty("");
        this.solution = new SimpleStringProperty("");
        this.date = new SimpleObjectProperty<>(new Date());
        this.archive = new SimpleObjectProperty<>(null);
        this.version = new SimpleStringProperty("1");
    }


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

    public String getState() {
        return state.get();
    }

    public void setState(String state) {
        this.state.set(state);
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

    public Date getDate() {
        return date.get();
    }

    public void setDate(Date date) {
        this.date.set(date);
    }

    public Serializable getArchive() {
        return archive.get();
    }

    public void setArchive(Serializable archive) {
        this.archive.set(archive);
    }

    public String getVersion() {
        return version.get();
    }

    public void setVersion(String version) {
        this.version.set(version);
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

    @Override
    public String toString() {
        return "ApprovalsDto{" +
                "id=" + id.get() +
                ", state='" + state.get() + '\'' +
                ", description='" + description.get() + '\'' +
                ", comment='" + comment.get() + '\'' +
                ", solution='" + solution.get() + '\'' +
                ", date=" + date.get() +
                ", archive=" + archive.get() +
                ", version=" + version.get() +
                ", users=" + users +
                ", gestion=" + gestion +
                '}';
    }
}
