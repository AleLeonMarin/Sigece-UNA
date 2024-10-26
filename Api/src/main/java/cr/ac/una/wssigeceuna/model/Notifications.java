/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.wssigeceuna.model;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aletr
 */
@Entity
@Table(name = "SIS_NOTIFICACION")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Notifications.findAll", query = "SELECT s FROM Notifications s"),
        @NamedQuery(name = "Notifications.findById", query = "SELECT s FROM Notifications s WHERE s.id = :id"),
/*
 * @NamedQuery(name = "SisNotificacion.findByNotNombre", query =
 * "SELECT s FROM SisNotificacion s WHERE s.notNombre = :notNombre"),
 * 
 * @NamedQuery(name = "SisNotificacion.findByNotVersion", query =
 * "SELECT s FROM SisNotificacion s WHERE s.notVersion = :notVersion")
 */ })
public class Notifications implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sis_notificacion_seq")
    @SequenceGenerator(name = "sis_notificacion_seq", sequenceName = "SIS_NOTIFICACION_SEQ01", allocationSize = 1)
    @Column(name = "NOT_ID")
    private Long id;

    @Basic(optional = false)
    @Column(name = "NOT_NOMBRE")
    private String name;

    @Basic(optional = false)
    @Lob
    @Column(name = "NOT_PLANTILLA")
    private String html;

    @Version
    @Column(name = "NOT_VERSION")
    private Long version;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "notifications", orphanRemoval = true)
    private List<Variables> variables;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "notification")
    private List<Mails> mails;

    public Notifications() {
    }

    public Notifications(Long id) {
        this.id = id;
    }

    public Notifications(NotificationsDto notificationsDto) {
        this();
        this.id = notificationsDto.getId();
        update(notificationsDto);
    }

    public void update(NotificationsDto notificationsDto) {
        this.name = notificationsDto.getName();
        this.html = notificationsDto.getHtml();
        this.version = notificationsDto.getVersion();

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

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<Variables> getVariables() {
        return variables;
    }

    public void setVariables(List<Variables> variables) {
        this.variables = variables;
    }

    public List<Mails> getMails() {
        return mails;
    }

    public void setMails(List<Mails> mails) {
        this.mails = mails;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Notifications)) {
            return false;
        }
        Notifications other = (Notifications) obj;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Notifications{" + "id=" + id + '}';
    }

}
