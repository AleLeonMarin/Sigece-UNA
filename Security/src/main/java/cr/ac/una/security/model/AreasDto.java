package cr.ac.una.security.model;

import javafx.beans.property.SimpleStringProperty;
import javassist.Loader.Simple;

import java.io.Serializable;
import java.util.List;

public class AreasDto implements Serializable {

    private SimpleStringProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty state;
    private SimpleStringProperty version;
    private List<UsersDto> users;
    private List<ActivitiesDto> activities;

    public AreasDto() {
        this.id = new SimpleStringProperty("");
        this.name = new SimpleStringProperty("");
        this.state = new SimpleStringProperty("");
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

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getState() {
        return state.get();
    }

    public void setState(String state) {
        this.state.set(state);
    }

    public String getVersion() {
        return version.get();
    }

    public void setVersion(String version) {
        this.version.set(version);
    }

    public List<UsersDto> getUsers() {
        return users;
    }

    public void setUsers(List<UsersDto> users) {
        this.users = users;
    }

    public List<ActivitiesDto> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivitiesDto> activities) {
        this.activities = activities;
    }

    public SimpleStringProperty namePrroperty() {
        return name;
    }

    @Override
    public String toString() {
        return "AreasDto{" +
                "id=" + id.get() +
                ", name='" + name.get() + '\'' +
                ", state='" + state.get() + '\'' +
                ", version=" + version.get() +
                '}';
    }
}
