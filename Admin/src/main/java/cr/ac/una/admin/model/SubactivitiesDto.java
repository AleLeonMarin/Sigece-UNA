package cr.ac.una.admin.model;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;

public class SubactivitiesDto {

    private SimpleStringProperty id;
    private SimpleStringProperty name;
    private ActivitiesDto activity;
    private Long version;
    private List<GestionsDto> gestions;

    public SubactivitiesDto() {

        this.id = new SimpleStringProperty("");
        this.name = new SimpleStringProperty("");
        gestions = new ArrayList<>();
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

    public ActivitiesDto getActivity() {
        return activity;
    }

    public void setActivity(ActivitiesDto activity) {
        this.activity = activity;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<GestionsDto> getGestions() {
        return gestions;
    }

    public void setGestions(List<GestionsDto> gestions) {
        this.gestions = gestions;
    }
}
