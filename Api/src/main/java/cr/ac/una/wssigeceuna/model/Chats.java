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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author aletr
 */
@Entity
@Table(name = "SIS_CHATS")
@NamedQueries({
        @NamedQuery(name = "Chats.findAll", query = "SELECT s FROM Chats s"),

        @NamedQuery(name = "Chats.findByChtId", query =
        "SELECT s FROM Chats s WHERE s.id = :id"),
 /* 
 * @NamedQuery(name = "SisChats.findByChtFecha", query =
 * "SELECT s FROM SisChats s WHERE s.chtFecha = :chtFecha"),
 * 
 * @NamedQuery(name = "SisChats.findByChtVersion", query =
 * "SELECT s FROM SisChats s WHERE s.chtVersion = :chtVersion")
 */ })
public class Chats implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sis_chats_seq")
    @SequenceGenerator(name = "sis_chats_seq", sequenceName = "SIS_CHATS_SEQ01", allocationSize = 1)
    @Column(name = "CHT_ID")
    private Long id;

    @Basic(optional = false)
    @Column(name = "CHT_FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Version
    @Column(name = "CHT_VERSION")
    private Long version;

    @JoinColumn(name = "CHT_RECEPTOR_ID", referencedColumnName = "USER_ID")
    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    private Users receptor;

    @JoinColumn(name = "CHT_EMISOR_ID", referencedColumnName = "USER_ID")
    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    private Users emisor;

   @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "chat")
    private List<Messages> messages;

    public Chats() {
      
    }

    public Chats(Long id) {
        this();
        this.id = id;
    }

    public Chats(ChatsDto cht) {
        this();
        this.id = cht.getId();
        update(cht);
    }

    public void update(ChatsDto cht) {
        this.date = cht.getDate();
        this.version = cht.getVersion();
        this.receptor = cht.getReceptor();
        this.emisor = cht.getEmisor();
        
        if (cht.getMessages() != null) {
        this.messages = new ArrayList<>();
        for (MessagesDto mensajeDto : cht.getMessages()) {
            Messages mensaje = new Messages(mensajeDto);
            mensaje.setChat(this);
            this.messages.add(mensaje);
        }
    }
          if (cht.getReceptor() != null) {
        this.receptor = new Users();
        this.receptor=cht.getReceptor();
    }
    if (cht.getEmisor() != null) {
        this.emisor = new Users();
        this.emisor=cht.getEmisor();
    }


    if (cht.getMessages() != null) {
        this.messages = new ArrayList<>();
        for (MessagesDto mensajeDto : cht.getMessages()) {
            this.messages.add(new Messages(mensajeDto));
        }
    }
        
        

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Users getReceptor() {
        return receptor;
    }

    public void setReceptor(Users receptor) {
        this.receptor = receptor;
    }

    public Users getEmisor() {
        return emisor;
    }

    public void setEmisor(Users emisor) {
        this.emisor = emisor;
    }

    public List<Messages> getMessages() {
        return messages;
    }

    public void setMessages(List<Messages> messages) {
        this.messages = messages;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Chats)) {
            return false;
        }
        Chats other = (Chats) obj;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Chats{" + "id=" + id + '}';
    }

}
