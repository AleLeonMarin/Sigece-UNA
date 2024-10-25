package cr.ac.una.wssigeceuna.model;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.json.bind.annotation.JsonbPropertyOrder;

@JsonbPropertyOrder({
        "id",
        "value",
        "result",
        "version",
        "Variable"
})

@Schema(description = "Esta clase contiene la informacion de las variables condicionales")
public class ConditionalVariablesDto implements Serializable{

    @Schema(description = "Identificador de la variable condicional", example = "1")
    private Long id;

    @Schema(description = "Valor de la variable condicional", example = "Valor de prueba")
    private String value;

    @Schema(description = "Resultado de la variable condicional", example = "Resultado de prueba")
    private String result;

    @Schema(description = "Version de la variable condicional", example = "1")
    private Long version;

    @Schema(description = "Variable de la variable condicional")
    private VariablesDto Variable;

    public ConditionalVariablesDto() {
    }

    public ConditionalVariablesDto(ConditionalVariables conditionalVariables) {
        this.id = conditionalVariables.getId();
        this.value = conditionalVariables.getValue();
        this.result = conditionalVariables.getResult();
        this.version = conditionalVariables.getVersion();
        this.Variable = new VariablesDto(conditionalVariables.getVariable());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public VariablesDto getVariable() {
        return Variable;
    }

    public void setVariable(VariablesDto Variable) {
        this.Variable = Variable;
    }

    @Override
    public String toString() {
        return "ConditionalVariablesDto{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", result='" + result + '\'' +
                ", version=" + version +
                ", Variable=" + Variable +
                '}';
    }

}
