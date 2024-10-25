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
@Table(name = "SIS_APROBACIONES")
@NamedQueries({
        @NamedQuery(name = "Approvals.findAll", query = "SELECT s FROM Approvals s"),
/*
 * @NamedQuery(name = "SisAprobaciones.findByAproId", query =
 * "SELECT s FROM SisAprobaciones s WHERE s.aproId = :aproId"),
 * 
 * @NamedQuery(name = "SisAprobaciones.findByAproEstado", query =
 * "SELECT s FROM SisAprobaciones s WHERE s.aproEstado = :aproEstado"),
 * 
 * @NamedQuery(name = "SisAprobaciones.findByAproDescripcion", query =
 * "SELECT s FROM SisAprobaciones s WHERE s.aproDescripcion = :aproDescripcion"
 * ),
 * 
 * @NamedQuery(name = "SisAprobaciones.findByAproComentario", query =
 * "SELECT s FROM SisAprobaciones s WHERE s.aproComentario = :aproComentario"),
 * 
 * @NamedQuery(name = "SisAprobaciones.findByAproSolucion", query =
 * "SELECT s FROM SisAprobaciones s WHERE s.aproSolucion = :aproSolucion"),
 * 
 * @NamedQuery(name = "SisAprobaciones.findByAproFecha", query =
 * "SELECT s FROM SisAprobaciones s WHERE s.aproFecha = :aproFecha"),
 * 
 * @NamedQuery(name = "SisAprobaciones.findByAproVersion", query =
 * "SELECT s FROM SisAprobaciones s WHERE s.aproVersion = :aproVersion")
 */ })
public class Approvals implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "GENERATOR_APPROVAL_SEQUENCE", sequenceName = "SIS_APROBACIONES_SEQ01", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENERATOR_APPROVAL_SEQUENCE")
    @Column(name = "APRO_ID")
    private Long id;

    @Basic(optional = false)
    @Column(name = "APRO_ESTADO")
    private String state;

    @Basic(optional = false)
    @Column(name = "APRO_DESCRIPCION")
    private String description;

    @Column(name = "APRO_COMENTARIO")
    private String comentary;

    @Column(name = "APRO_SOLUCION")
    private String solution;

    @Basic(optional = false)
    @Column(name = "APRO_FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Lob
    @Column(name = "APRO_ARCHIVO")
    private String archive;

    @Version
    @Column(name = "APRO_VERSION")
    private Long version;

    @JoinColumn(name = "APRO_GES_ID", referencedColumnName = "GES_ID")
    @ManyToOne(optional = false)
    private Gestions gestion;

    @JoinColumn(name = "APRO_USER_ID", referencedColumnName = "USER_ID")
    @ManyToOne(optional = false)
    private Users user;

    public Approvals() {
    }

    public Approvals(Long id) {
        this();
        this.id = id;
    }

    public Approvals(ApprovalsDto approvalsDto) {
        this();
        this.id = approvalsDto.getId();
        update(approvalsDto);
    }

    public void update(ApprovalsDto approvalsDto) {
        this.state = approvalsDto.getState();
        this.description = approvalsDto.getDescription();
        this.comentary = approvalsDto.getComment();
        this.solution = approvalsDto.getSolution();
        this.date = approvalsDto.getDate();
        this.archive = (String) approvalsDto.getArchive();
        this.version = approvalsDto.getVersion();
        this.gestion = new Gestions(approvalsDto.getGestion());
        this.user = new Users(approvalsDto.getUsers());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComentary() {
        return comentary;
    }

    public void setComentary(String comentary) {
        this.comentary = comentary;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
        if (!(obj instanceof Approvals)) {
            return false;
        }
        Approvals other = (Approvals) obj;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Approvals{" + "id=" + id + '}';
    }

}
