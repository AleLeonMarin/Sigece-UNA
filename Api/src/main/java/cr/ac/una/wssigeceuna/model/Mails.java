/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.wssigeceuna.model;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author aletr
 */
@Entity
@Table(name = "SIS_CORREOS")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Mails.findAll", query = "SELECT s FROM Mails s"),
/*
 * @NamedQuery(name = "SisCorreos.findByCorId", query =
 * "SELECT s FROM SisCorreos s WHERE s.corId = :corId"),
 * 
 * @NamedQuery(name = "SisCorreos.findByCorAsunto", query =
 * "SELECT s FROM SisCorreos s WHERE s.corAsunto = :corAsunto"),
 * 
 * @NamedQuery(name = "SisCorreos.findByCorDestinatario", query =
 * "SELECT s FROM SisCorreos s WHERE s.corDestinatario = :corDestinatario"),
 * 
 * @NamedQuery(name = "SisCorreos.findByCorEstado", query =
 * "SELECT s FROM SisCorreos s WHERE s.corEstado = :corEstado"),
 * 
 * @NamedQuery(name = "SisCorreos.findByCorFecha", query =
 * "SELECT s FROM SisCorreos s WHERE s.corFecha = :corFecha"),
 * 
 * @NamedQuery(name = "SisCorreos.findByCorVersion", query =
 * "SELECT s FROM SisCorreos s WHERE s.corVersion = :corVersion")
 */ })
public class Mails implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sis_correos_seq")
    @SequenceGenerator(name = "sis_correos_seq", sequenceName = "SIS_CORREOS_SEQ01", allocationSize = 1)
    @Column(name = "COR_ID")
    private Long id;

    @Basic(optional = false)
    @Column(name = "COR_ASUNTO")
    private String subject;

    @Basic(optional = false)
    @Column(name = "COR_DESTINATARIO")
    private String destinatary;

    @Basic(optional = false)
    @Lob
    @Column(name = "COR_RESULTADO")
    private String result;

    @Basic(optional = false)
    @Column(name = "COR_ESTADO")
    private String state;

    @Basic(optional = false)
    @Column(name = "COR_FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Version
    @Column(name = "COR_VERSION")
    private Long version;

    @JoinColumn(name = "COR_NOT_ID", referencedColumnName = "NOT_ID")
    @ManyToOne(optional = false)
    @JsonbTransient 
    private Notifications notification;
    
    
    @Lob
    @Column(name = "COR_ADJUNTOS")
    private List<byte[]> attachments;

    public Mails() {
    }

    public Mails(Long id) {
        this();
        this.id = id;
    }

    public Mails(MailsDto mailsDto) {
        this();
        this.id = mailsDto.getId();
        update(mailsDto);
    }

    public void update(MailsDto mailsDto) {
        this.subject = mailsDto.getSubject();
        this.destinatary = mailsDto.getDestinatary();
        this.result = mailsDto.getResult();
        this.state = mailsDto.getState();
        this.date = mailsDto.getDate();
        this.version = mailsDto.getVersion();

         if (mailsDto.getNotification()!= null) {
        Notifications notificacion = new Notifications();
        notificacion.setId(mailsDto.getNotification().getId());
        this.notification = notificacion;
    }
         
          this.attachments = mailsDto.getAttachments();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDestinatary() {
        return destinatary;
    }

    public void setDestinatary(String destinatary) {
        this.destinatary = destinatary;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Notifications getNotification() {
        return notification;
    }

    public void setNotification(Notifications notification) {
        this.notification = notification;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Mails)) {
            return false;
        }
        Mails other = (Mails) obj;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Mails{" + "id=" + id + '}';
    }
    
     public List<byte[]> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<byte[]> attachments) {
        this.attachments = attachments;
    }

}
