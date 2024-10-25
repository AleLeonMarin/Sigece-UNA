package cr.ac.una.wssigeceuna.model;

import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.json.bind.annotation.JsonbPropertyOrder;

@JsonbPropertyOrder({ "id", "mail", "password", "protocol", "port", "server", "protocol", "timeout", "version" })
@Schema(description = "Esta clase contiene la informacion de los parametros")
public class ParamethersDto implements Serializable {

    @Schema(description = "ID del parametro", example = "1")
    private Long id;

    @Schema(description = "Mail del parametro", example = "sigeceuna@gmail.com")
    private String mail;

    @Schema(description = "Password del parametro", example = "password123")
    private String password;

    @Schema(description = "Protocolo del parametro", example = "SMTP")
    private String protocol;

    @Schema(description = "Puerto del parametro", example = "587")
    private Long port;

    @Schema(description = "Servidor del parametro", example = "smtp.gmail.com")
    private String server;

    @Schema(description = "Timeout del parametro", example = "30000")
    private Long timeout;

    @Schema(description = "Version del parametro", example = "1.0")
    private Long version;

    public ParamethersDto() {
    }

    public ParamethersDto(Paramethers paramethers) {

        this.id = paramethers.getId();
        this.mail = paramethers.getMail();
        this.password = paramethers.getPassword();
        this.protocol = paramethers.getProtocol();
        this.port = paramethers.getPort();
        this.server = paramethers.getServer();
        this.timeout = paramethers.getTimeout();
        this.version = paramethers.getVersion();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Long getPort() {
        return port;
    }

    public void setPort(Long port) {
        this.port = port;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}