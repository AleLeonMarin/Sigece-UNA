package cr.ac.una.wssigeceuna.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.json.bind.annotation.JsonbPropertyOrder;

@JsonbPropertyOrder({
        "id",
        "name",
        "area",
        "version"
})

@Schema(description = "Esta clase contiene la información de las actividades")
public class ActivitiesDto implements Serializable {

    @Schema(description = "Identificador de la actividad", example = "1")
    private Long id;

    @Schema(description = "Nombre de la actividad", example = "Actividad de prueba")
    private String name;

    @Schema(description = "Área de la actividad")
    private AreasDto area;

    @Schema(description = "Versión de la actividad", example = "1")
    private Long version;

    private List<SubactivitiesDto> subactivities;

    private List<GestionsDto> gestions;

    public ActivitiesDto() {
        subactivities = new ArrayList<>();
        gestions = new ArrayList<>();
    }

    public ActivitiesDto(Activities activities) {
        this();
        this.id = activities.getId();
        this.name = activities.getName();
        this.area = new AreasDto(activities.getArea());
        this.version = activities.getVersion();
        // Convertir las subactividades de la entidad a DTO si es necesario
        if (activities.getSubactivities() != null) {
            this.subactivities = new ArrayList<>();
            for (Subactivities sub : activities.getSubactivities()) {
                this.subactivities.add(new SubactivitiesDto(sub));
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

    public List<GestionsDto> getGestions() {
        return gestions;
    }

    public void setGestions(List<GestionsDto> gestions) {
        this.gestions = gestions;
    }
}
