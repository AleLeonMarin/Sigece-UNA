package cr.ac.una.mails.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleLongProperty;

import java.util.List;

public class SubactivitiesDto {

    private SimpleStringProperty id;
    private SimpleStringProperty name;
    private ActivitiesDto activity;
    private SimpleStringProperty version;
    private List<GestionsDto> gestions;

    public SubactivitiesDto() {
        this.id = new SimpleStringProperty("");
        this.name = new SimpleStringProperty("");
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

    public ActivitiesDto getActivity() {
        return activity;
    }

    public void setActivity(ActivitiesDto activity) {
        this.activity = activity;
    }

    public String getVersion() {
        return version.get();
    }

    public void setVersion(String version) {
        this.version.set(version);
    }

    public List<GestionsDto> getGestions() {
        return gestions;
    }

    public void setGestions(List<GestionsDto> gestions) {
        this.gestions = gestions;
    }

    @Override
    public String toString() {
        return "SubactivitiesDto{" +
                "id=" + id.get() +
                ", name='" + name.get() + '\'' +
                ", activity=" + activity +
                ", version=" + version.get() +
                '}';
    }
}
