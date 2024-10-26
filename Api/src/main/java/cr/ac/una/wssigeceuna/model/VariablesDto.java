package cr.ac.una.wssigeceuna.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.annotation.JsonbTransient;

@JsonbPropertyOrder({
        "id",
        "name",
        "type",
        "value",
        "version",
        "notification",

})

@Schema(description = "Clase que contiene la informacion de las variables")
public class VariablesDto implements Serializable {

    @Schema(description = "Id de la variable", example = "1")
    public Long id;

    @Schema(description = "Nombre de la variable", example = "nombre")
    public String name;

    @Schema(description = "Tipo de la variable", example = "String")
    public String type;

    @Schema(description = "Valor de la variable", example = "Alejandro")
    public String value;

    @Schema(description = "Version de la variable", example = "1")
    public Long version;

    @JsonbTransient
    public NotificationsDto notification;

    @JsonbTransient
    List<ConditionalVariablesDto> conditionalVariables;

    @JsonbTransient
    List<MultimediaVariablesDto> multimediaVariables;

    public VariablesDto() {
      
    }

    public VariablesDto(Variables variables) {
        this.id = variables.getId();
        this.name = variables.getName();
        this.type = variables.getType();
        this.value = variables.getValue();
        this.version = variables.getVersion();
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public NotificationsDto getNotification() {
        return notification;
    }

    public void setNotification(NotificationsDto notification) {
        this.notification = notification;
    }

    public List<ConditionalVariablesDto> getConditionalVariables() {
        return conditionalVariables;
    }

    public void setConditionalVariables(List<ConditionalVariablesDto> conditionalVariables) {
        this.conditionalVariables = conditionalVariables;
    }

    public List<MultimediaVariablesDto> getMultimediaVariables() {
        return multimediaVariables;
    }

    public void setMultimediaVariables(List<MultimediaVariablesDto> multimediaVariables) {
        this.multimediaVariables = multimediaVariables;
    }

}
