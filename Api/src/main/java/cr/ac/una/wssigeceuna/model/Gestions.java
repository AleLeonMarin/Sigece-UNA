/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.wssigeceuna.model;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.QueryHint;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aletr
 */
@Entity
@Table(name = "SIS_GESTION")
@NamedQueries({
        @NamedQuery(name = "Gestions.findAll", query = "SELECT s FROM Gestions s"),
        @NamedQuery(name = "Gestions.findById", query = "SELECT s FROM Gestions s WHERE s.id = :id", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
/*
 * @NamedQuery(name =
 * "SisGestion.findByGesId", query =
 * "SELECT s FROM SisGestion s WHERE s.gesId = :gesId"
 * ),
 * 
 * @NamedQuery(name =
 * "SisGestion.findByGesFechaCreacion",
 * query =
 * "SELECT s FROM SisGestion s WHERE s.gesFechaCreacion = :gesFechaCreacion"
 * ),
 * 
 * @NamedQuery(name =
 * "SisGestion.findByGesFechaSolucion",
 * query =
 * "SELECT s FROM SisGestion s WHERE s.gesFechaSolucion = :gesFechaSolucion"
 * ),
 * 
 * @NamedQuery(name =
 * "SisGestion.findByGesAsunto",
 * query =
 * "SELECT s FROM SisGestion s WHERE s.gesAsunto = :gesAsunto"
 * ),
 * 
 * @NamedQuery(name =
 * "SisGestion.findByGesDescripcion",
 * query =
 * "SELECT s FROM SisGestion s WHERE s.gesDescripcion = :gesDescripcion"
 * ),
 * 
 * @NamedQuery(name =
 * "SisGestion.findByGesEstado",
 * query =
 * "SELECT s FROM SisGestion s WHERE s.gesEstado = :gesEstado"
 * ),
 * 
 * @NamedQuery(name =
 * "SisGestion.findByGesVersion",
 * query =
 * "SELECT s FROM SisGestion s WHERE s.gesVersion = :gesVersion"
 * )
 */ })
public class Gestions implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "GENERATOR_GESTIONS_SEQUENCE", sequenceName = "SIS_GESTION_SEQ01", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENERATOR_GESTIONS_SEQUENCE")
    @Column(name = "GES_ID")
    private Long id;

    @Basic(optional = false)
    @Column(name = "GES_FECHA_CREACION")
    private LocalDate creationDate;

    @Basic(optional = false)
    @Column(name = "GES_FECHA_SOLUCION")
    private LocalDate solutionDate;

    @Basic(optional = false)
    @Column(name = "GES_ASUNTO")
    private String subject;

    @Basic(optional = false)
    @Column(name = "GES_DESCRIPCION")
    private String description;

    @Basic(optional = false)
    @Column(name = "GES_ESTADO")
    private String state;

    @Basic(optional = false)
    @Lob
    @Column(name = "GES_ARCHIVO")
    private byte[] archive;

    @Version
    @Column(name = "GES_VERSION")
    private Long version;

    @ManyToMany(mappedBy = "approvers")
    private List<Users> approvers;

    @JoinColumn(name = "GES_ACT_ID", referencedColumnName = "ACT_ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Activities activity;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gestion")
    private List<Follows> follows;

    @JoinColumn(name = "GES_SUB_ID", referencedColumnName = "SUB_ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Subactivities subactivities;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "GES_SOLICITANTE_ID", referencedColumnName = "USER_ID") // Cambiado de gestionRequester a USER_ID
    private Users requester;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "GES_ASIGNADO_ID", referencedColumnName = "USER_ID") // Cambiado de gestionAssigned a USER_ID
    private Users assigned;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gestion")
    private List<Approvals> approvals;

    public Gestions() {
        approvers = new ArrayList<>();
        follows = new ArrayList<>();
        approvals = new ArrayList<>();
    }

    public Gestions(Long id) {
        this();
        this.id = id;
    }

    public Gestions(GestionsDto gestionsDto) {
        this();
        this.id = gestionsDto.getId();
        update(gestionsDto);
    }

    public void update(GestionsDto gestionsDto) {
        this.creationDate = gestionsDto.getCreationDate();
        this.solutionDate = gestionsDto.getSolutionDate();
        this.subject = gestionsDto.getSubject();
        this.description = gestionsDto.getDescription();
        this.state = gestionsDto.getState();
        this.archive = gestionsDto.getArchive();
        this.version = gestionsDto.getVersion();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getSolutionDate() {
        return solutionDate;
    }

    public void setSolutionDate(LocalDate solutionDate) {
        this.solutionDate = solutionDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public byte[] getArchive() {
        return archive;
    }

    public void setArchive(byte[] archive) {
        this.archive = archive;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<Follows> getFollows() {
        return follows;
    }

    public void setFollows(List<Follows> follows) {
        this.follows = follows;
    }

    public Activities getActivity() {
        return activity;
    }

    public void setActivity(Activities activity) {
        this.activity = activity;
    }

    public Subactivities getSubactivities() {
        return subactivities;
    }

    public void setSubactivities(Subactivities subactivities) {
        this.subactivities = subactivities;
    }

    public Users getRequester() {
        return requester;
    }

    public void setRequester(Users requester) {
        this.requester = requester;
    }

    public Users getAssigned() {
        return assigned;
    }

    public void setAssigned(Users assigned) {
        this.assigned = assigned;
    }

    public List<Approvals> getApprovals() {
        return approvals;
    }

    public void setApprovals(List<Approvals> approvals) {
        this.approvals = approvals;
    }

    public List<Users> getApprovers() {
        return approvers;
    }

    public void setApprovers(List<Users> approvers) {
        this.approvers = approvers;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Gestions)) {
            return false;
        }
        Gestions other = (Gestions) obj;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Gestions{" + "id=" + id + '}';
    }

}
