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
@Table(name = "SIS_SEGUIMIENTO")
@NamedQueries({
        @NamedQuery(name = "Follows.findAll", query = "SELECT s FROM Follows s"),
/*
 * @NamedQuery(name = "SisSeguimiento.findBySegId", query =
 * "SELECT s FROM SisSeguimiento s WHERE s.segId = :segId"),
 * 
 * @NamedQuery(name = "SisSeguimiento.findBySegFecha", query =
 * "SELECT s FROM SisSeguimiento s WHERE s.segFecha = :segFecha"),
 * 
 * @NamedQuery(name = "SisSeguimiento.findBySegDescripcion", query =
 * "SELECT s FROM SisSeguimiento s WHERE s.segDescripcion = :segDescripcion"),
 * 
 * @NamedQuery(name = "SisSeguimiento.findBySegVersion", query =
 * "SELECT s FROM SisSeguimiento s WHERE s.segVersion = :segVersion")
 */ })
public class Follows implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "GENERATOR_FOLLOWS_SEQUENCE", sequenceName = "SIS_SEGUIMIENTO_SEQ01", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENERATOR_FOLLOWS_SEQUENCE")
    @Column(name = "SEG_ID")
    private Long id;

    @Basic(optional = false)
    @Column(name = "SEG_FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Basic(optional = false)
    @Column(name = "SEG_DESCRIPCION")
    private String description;

    @Lob
    @Column(name = "SEG_ARCHIVO")
    private String archive;

    @Version
    @Column(name = "SEG_VERSION")
    private Long version;

    @JoinColumn(name = "SEG_GES_ID", referencedColumnName = "GES_ID")
    @ManyToOne(optional = false)
    private Gestions gestion;

    @JoinColumn(name = "SEG_USER_ID", referencedColumnName = "USER_ID")
    @ManyToOne(optional = false)
    private Users users;

    public Follows() {
    }

    public Follows(Long id) {
        this();
        this.id = id;
    }

    public Follows(FollowsDto followDto) {
        this();
        this.id = followDto.getId();
        update(followDto);
    }

    public void update(FollowsDto followDto) {
        this.date = followDto.getDate();
        this.description = followDto.getDescription();
        this.archive = followDto.getArchive();
        this.version = followDto.getVersion();
        this.gestion = new Gestions(followDto.getGestions());
        this.users = new Users(followDto.getUsers());
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArchive() {
        return archive;
    }

    public void setArchive(String archive) {
        this.archive = archive;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Gestions getGestion() {
        return gestion;
    }

    public void setGestion(Gestions gestion) {
        this.gestion = gestion;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Follows)) {
            return false;
        }
        Follows other = (Follows) obj;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Follows{" + "id=" + id + '}';
    }

}
