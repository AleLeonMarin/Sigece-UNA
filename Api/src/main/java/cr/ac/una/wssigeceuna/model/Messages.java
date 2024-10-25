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
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author aletr
 */
@Entity
@Table(name = "SIS_MENSAJES")
@NamedQueries({
        @NamedQuery(name = "Messages.findAll", query = "SELECT s FROM Messages s"),
        @NamedQuery(name = "Messages.findByChat", query = "SELECT s FROM Messages s WHERE s.chat.id = :chat"),
/*
 * @NamedQuery(name = "SisMensajes.findBySmsId", query =
 * "SELECT s FROM SisMensajes s WHERE s.smsId = :smsId"),
 * 
 * @NamedQuery(name = "SisMensajes.findBySmsTiempo", query =
 * "SELECT s FROM SisMensajes s WHERE s.smsTiempo = :smsTiempo"),
 * 
 * @NamedQuery(name = "SisMensajes.findBySmsVersion", query =
 * "SELECT s FROM SisMensajes s WHERE s.smsVersion = :smsVersion")
 */ })
public class Messages implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sis_mensajes_seq")
    @SequenceGenerator(name = "sis_mensajes_seq", sequenceName = "SIS_MENSAJES_SEQ01", allocationSize = 1)
    @Column(name = "SMS_ID")
    private Long id;

    @Basic(optional = false)
    @Lob
    @Column(name = "SMS_TEXTO")
    private String text;

    @Lob
    @Column(name = "SMS_ARCHIVO")
    private Serializable archive;

    @Basic(optional = false)
    @Column(name = "SMS_TIEMPO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Version
    @Column(name = "SMS_VERSION")
    private Long version;

    @JoinColumn(name = "SMS_CHAT_ID", referencedColumnName = "CHT_ID")
    @ManyToOne(optional = false)
    private Chats chat;

    @JoinColumn(name = "SMS_USU_ID_EMISOR", referencedColumnName = "USER_ID")
    @ManyToOne(optional = false)
    private Users user;

    public Messages() {
    }

    public Messages(Long id) {
        this();
        this.id = id;
    }

    public Messages(MessagesDto sms) {
        this();
        this.id = sms.getId();
        update(sms);
    }

    public void update(MessagesDto sms) {
        this.text = sms.getText();
        this.archive = sms.getArchive();
        this.date = sms.getDate();
        this.version = sms.getVersion();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Serializable getArchive() {
        return archive;
    }

    public void setArchive(Serializable archive) {
        this.archive = archive;
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

    public Chats getChat() {
        return chat;
    }

    public void setChat(Chats chat) {
        this.chat = chat;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Messages)) {
            return false;
        }
        Messages other = (Messages) obj;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Messages{" + "id=" + id + '}';
    }

}
