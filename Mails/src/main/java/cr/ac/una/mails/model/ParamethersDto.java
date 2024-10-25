package cr.ac.una.mails.model;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class ParamethersDto {

    public SimpleLongProperty id;
    public SimpleStringProperty mail;
    public SimpleStringProperty password;
    public SimpleStringProperty protocol;
    public SimpleLongProperty port;
    public SimpleStringProperty server;
    public SimpleLongProperty timeout;
    public SimpleLongProperty version;

    public ParamethersDto() {
        this.id = new SimpleLongProperty();
        this.mail = new SimpleStringProperty("");
        this.password = new SimpleStringProperty("");
        this.protocol = new SimpleStringProperty("");
        this.port = new SimpleLongProperty(0L);
        this.server = new SimpleStringProperty("");
        this.timeout = new SimpleLongProperty(0L);
        this.version = new SimpleLongProperty(1L);
    }

    public ParamethersDto(Long id, String mail, String password, String protocol, Long port, String server, Long timeout, Long version) {
        this();
        this.id.set(id);
        this.mail.set(mail);
        this.password.set(password);
        this.protocol.set(protocol);
        this.port.set(port);
        this.server.set(server);
        this.timeout.set(timeout);
        this.version.set(version);
    }

    public Long getId() {
        return id.get();
    }

    public void setId(Long id) {
        this.id.set(id);
    }

    public String getMail() {
        return mail.get();
    }

    public void setMail(String mail) {
        this.mail.set(mail);
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getProtocol() {
        return protocol.get();
    }

    public void setProtocol(String protocol) {
        this.protocol.set(protocol);
    }

    public Long getPort() {
        return port.get();
    }

    public void setPort(Long port) {
        this.port.set(port);
    }

    public String getServer() {
        return server.get();
    }

    public void setServer(String server) {
        this.server.set(server);
    }

    public Long getTimeout() {
        return timeout.get();
    }

    public void setTimeout(Long timeout) {
        this.timeout.set(timeout);
    }

    public Long getVersion() {
        return version.get();
    }

    public void setVersion(Long version) {
        this.version.set(version);
    }

    @Override
    public String toString() {
        return "ParamethersDto{" +
                "id=" + id.get() +
                ", mail='" + mail.get() + '\'' +
                ", password='" + password.get() + '\'' +
                ", protocol='" + protocol.get() + '\'' +
                ", port=" + port.get() +
                ", server='" + server.get() + '\'' +
                ", timeout=" + timeout.get() +
                ", version=" + version.get() +
                '}';
    }
}
