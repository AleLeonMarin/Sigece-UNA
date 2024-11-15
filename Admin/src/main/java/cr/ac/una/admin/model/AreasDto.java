package cr.ac.una.admin.model;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class AreasDto implements Serializable {
    public SimpleStringProperty id;
    public SimpleStringProperty name;
    public SimpleBooleanProperty state;
    public Long version;
    public List<UsersDto> users;
    public List<ActivitiesDto> activities;

    public AreasDto() {

        this.id = new SimpleStringProperty("");
        this.name = new SimpleStringProperty("");
        this.state = new SimpleBooleanProperty(true);
        users = new ArrayList<>();
        activities = new ArrayList<>();
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

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getState() {
        return state.get() ? "A" : "I";
    }

    public void setState(String state) {
        this.state.set(state.equals("A"));
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
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
}
