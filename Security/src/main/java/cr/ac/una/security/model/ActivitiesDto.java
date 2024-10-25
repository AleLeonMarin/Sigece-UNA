package cr.ac.una.security.model;

import javafx.beans.property.SimpleStringProperty;
import java.io.Serializable;
import java.util.List;

public class ActivitiesDto implements Serializable {

    private SimpleStringProperty id;
    private SimpleStringProperty name;
    private AreasDto area;
    private SimpleStringProperty version;
    private List<SubactivitiesDto> subactivities;

    public ActivitiesDto() {
        this.id = new SimpleStringProperty("");
        this.name = new SimpleStringProperty("");
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

    public SimpleStringProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public AreasDto getArea() {
        return area;
    }

    public void setArea(AreasDto area) {
        this.area = area;
    }

    public String getVersion() {
        return version.get();
    }

    public void setVersion(String version) {
        this.version.set(version);
    }

    public SimpleStringProperty versionProperty() {
        return version;
    }

    public List<SubactivitiesDto> getSubactivities() {
        return subactivities;
    }

    public void setSubactivities(List<SubactivitiesDto> subactivities) {
        this.subactivities = subactivities;
    }

    @Override
    public String toString() {
        return "ActivitiesDto{" +
                "id=" + id.get() +
                ", name='" + name.get() + '\'' +
                ", area=" + area +
                ", version=" + version.get() +
                '}';
    }
}
