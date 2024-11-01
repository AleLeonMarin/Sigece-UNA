package cr.ac.una.admin.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;

public class ActivitiesDto implements Serializable {

    public SimpleStringProperty id;
    public SimpleStringProperty name;
    public AreasDto area;
    public Long version;
    public List<SubactivitiesDto> subactivities;

    public ActivitiesDto() {
        this.id = new SimpleStringProperty("");
        this.name = new SimpleStringProperty("");
        subactivities = new ArrayList<>();
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

    public AreasDto getArea() {
        return area;
    }

    public void setArea(AreasDto area) {
        this.area = area;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<SubactivitiesDto> getSubactivities() {
        return subactivities;
    }

    public void setSubactivities(List<SubactivitiesDto> subactivities) {
        this.subactivities = subactivities;
    }
}
