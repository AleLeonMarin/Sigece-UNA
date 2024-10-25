package cr.ac.una.wssigeceuna.model;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.annotation.JsonbTransient;

@JsonbPropertyOrder({
        "id",
        "name",
        "state",
        "version"
})

@Schema(description = "Esta clase contiene la información de las áreas")
public class AreasDto implements Serializable {

    @Schema(description = "Identificador del área", example = "1")
    private Long id;

    @Schema(description = "Nombre del área", example = "Área de prueba")
    private String name;

    @Schema(description = "Estado del área", example = "Activo")
    private String state;

    @Schema(description = "Versión del área", example = "1")
    private Long version;

    @JsonbTransient
    private List<UsersDto> users;

    @JsonbTransient
    private List<ActivitiesDto> activities;

    public AreasDto() {
        users = new ArrayList<>();
        activities = new ArrayList<>();
    }

    public AreasDto(Areas areas) {
        this.id = areas.getId();
        this.name = areas.getName();
        this.state = areas.getState();
        this.version = areas.getVersion();
        // Convertir las actividades de la entidad a DTO si es necesario
        if (areas.getActivity() != null) {
            this.activities = new ArrayList<>();
            for (Activities activity : areas.getActivity()) {
                this.activities.add(new ActivitiesDto(activity));
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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
