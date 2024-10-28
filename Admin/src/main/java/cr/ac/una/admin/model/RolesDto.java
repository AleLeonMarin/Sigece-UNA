package cr.ac.una.admin.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;

public class RolesDto implements Serializable {

    public SimpleStringProperty id;
    public SimpleStringProperty name;
    public Long version;
    public SystemsDto system;
    List<UsersDto> users = new ArrayList<>();

    public RolesDto() {
        this.id = new SimpleStringProperty("");
        this.name = new SimpleStringProperty("");
        users = new ArrayList<>();
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public SystemsDto getSystem() {
        return system;
    }

    public void setSystem(SystemsDto system) {
        this.system = system;
    }

    public List<UsersDto> getUsers() {
        return users;
    }

    public void setUsers(List<UsersDto> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "RolesDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", version=" + version +
                ", system=" + system +
                '}';
    }
}
