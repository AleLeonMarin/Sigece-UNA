package cr.ac.una.wssigeceuna.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import java.io.Serializable;

@JsonbPropertyOrder({"id","url","type","version","variable"})
@Schema(description = "Esta clase contiene la informacion de las variables multimedia")
public class MultimediaVariablesDto implements Serializable{

    @Schema(description = "Identificador de la variable multimedia", example = "1")
    private Long id;
    @Schema(description = "URL de la variable multimedia", example = "http://www.google.com")
    private String url;
    @Schema(description = "Tipo de la variable multimedia", example = "imagen")
    private String type;
    @Schema(description = "Version de la variable multimedia", example = "1")
    private Long version;
    @Schema(description = "Variable a la que pertenece la variable multimedia")
    private VariablesDto variable;

    public MultimediaVariablesDto() {
    }

    public MultimediaVariablesDto(MultimediaVariables multimediaVariables) {
        this.id = multimediaVariables.getId();
        this.url = multimediaVariables.getUrl();
        this.type = multimediaVariables.getType();
        this.version = multimediaVariables.getMediaVersion();
        this.variable = new VariablesDto(multimediaVariables.getVariables());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public VariablesDto getVariable() {
        return variable;
    }

    public void setVariable(VariablesDto variable) {
        this.variable = variable;
    }

    @Override
    public String toString() {
        return "MultimediaVariablesDto{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", type='" + type + '\'' +
                ", version=" + version +
                ", variable=" + variable +
                '}';
    }
    
}
