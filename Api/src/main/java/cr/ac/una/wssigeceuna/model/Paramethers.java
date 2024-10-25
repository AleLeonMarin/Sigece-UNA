/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.wssigeceuna.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.io.Serializable;

/**
 *
 * @author aletr
 */
@Entity
@Table(name = "SIS_PARAMETROS")
@NamedQueries({
        @NamedQuery(name = "Paramethers.findAll", query = "SELECT s FROM Paramethers s"),
/*
 * @NamedQuery(name = "SisParametros.findByParId", query =
 * "SELECT s FROM SisParametros s WHERE s.parId = :parId"),
 * 
 * @NamedQuery(name = "SisParametros.findByParCorreo", query =
 * "SELECT s FROM SisParametros s WHERE s.parCorreo = :parCorreo"),
 * 
 * @NamedQuery(name = "SisParametros.findByParClave", query =
 * "SELECT s FROM SisParametros s WHERE s.parClave = :parClave"),
 * 
 * @NamedQuery(name = "SisParametros.findByParPuerto", query =
 * "SELECT s FROM SisParametros s WHERE s.parPuerto = :parPuerto"),
 * 
 * @NamedQuery(name = "SisParametros.findByParServer", query =
 * "SELECT s FROM SisParametros s WHERE s.parServer = :parServer"),
 * 
 * @NamedQuery(name = "SisParametros.findByParProtocolo", query =
 * "SELECT s FROM SisParametros s WHERE s.parProtocolo = :parProtocolo"),
 * 
 * @NamedQuery(name = "SisParametros.findByParTimeout", query =
 * "SELECT s FROM SisParametros s WHERE s.parTimeout = :parTimeout"),
 * 
 * @NamedQuery(name = "SisParametros.findByParVersion", query =
 * "SELECT s FROM SisParametros s WHERE s.parVersion = :parVersion")
 */ })
public class Paramethers implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sis_parametros_seq")
    @SequenceGenerator(name = "sis_parametros_seq", sequenceName = "SIS_PARAMETROS_SEQ01", allocationSize = 1)
    @Column(name = "PAR_ID")
    private Long id;

    @Basic(optional = false)
    @Column(name = "PAR_CORREO")
    private String mail;

    @Basic(optional = false)
    @Column(name = "PAR_CLAVE")
    private String password;

    @Basic(optional = false)
    @Column(name = "PAR_PUERTO")
    private Long port;

    @Basic(optional = false)
    @Column(name = "PAR_SERVER")
    private String server;

    @Basic(optional = false)
    @Column(name = "PAR_PROTOCOLO")
    private String protocol;

    @Basic(optional = false)
    @Column(name = "PAR_TIMEOUT")
    private Long timeout;

    @Version
    @Column(name = "PAR_VERSION")
    private Long version;

    public Paramethers() {
    }

    public Paramethers(Long id) {
        this();
        this.id = id;
    }

    public Paramethers(ParamethersDto paramethers) {
        this();
        this.id = paramethers.getId();
        update(paramethers);
    }

    public void update(ParamethersDto paramethers) {
        this.mail = paramethers.getMail();
        this.password = paramethers.getPassword();
        this.port = paramethers.getPort();
        this.server = paramethers.getServer();
        this.protocol = paramethers.getProtocol();
        this.timeout = paramethers.getTimeout();
        this.version = paramethers.getVersion();
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Long getTimeout() {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != 0 ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Paramethers)) {
            return false;
        }
        Paramethers other = (Paramethers) obj;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Paramethers{" + "id=" + id + '}';
    }

}
