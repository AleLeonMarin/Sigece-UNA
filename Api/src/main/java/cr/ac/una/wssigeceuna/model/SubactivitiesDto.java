package cr.ac.una.wssigeceuna.model;

import java.util.ArrayList;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.annotation.JsonbTransient;

@JsonbPropertyOrder({"id", "name", "version", "activity"})

@Schema(description = "Clase que contiene la información de las subactividades")
public class SubactivitiesDto {

    @Schema(description = "ID de la subactividad", example = "1")
    private Long id;

    @Schema(description = "Nombre de la subactividad", example = "Subactividad 1")
    private String name;

    @Schema(description = "ID de la actividad a la que pertenece", example = "1")
    private ActivitiesDto activity;

    @Schema(description = "Versión de la subactividad", example = "1")
    private Long version;

    @JsonbTransient
    private List<GestionsDto> gestions;

    public SubactivitiesDto() {
        gestions = new ArrayList<>();
    }

    public SubactivitiesDto(Subactivities subactivities) {
        this();
        this.id = subactivities.getId();
        this.name = subactivities.getName();
        this.version = subactivities.getVersion();
        if (subactivities.getActivity() != null) {
            // Solo asignamos el ID para evitar la recursión
            this.activity = new ActivitiesDto();
            this.activity.setId(subactivities.getActivity().getId());
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
