package cr.ac.una.wssigeceuna.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SIS_SISTEMAS")
@NamedQueries({
        @NamedQuery(name = "Systems.findAll", query = "SELECT s FROM Systems s"),
        @NamedQuery(name = "Systems.findById", query = "SELECT s FROM Systems s WHERE s.id = :id")
})
public class Systems implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "GENERATOR_SISTEMAS_SEQUENCE", sequenceName = "SIS_SISTEMAS_SEQ01", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENERATOR_SISTEMAS_SEQUENCE")
    @Column(name = "SIS_ID")
    private Long id;

    @Basic(optional = false)
    @Column(name = "SIS_NOMBRE")
    private String name;

    @Version
    @Column(name = "SIS_VERSION")
    private Long version;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "system", fetch = FetchType.LAZY)
    private List<Roles> roles = new ArrayList<>();

    public Systems() {
    }

    public Systems(Long id) {
        this();
        this.id = id;
    }

    public Systems(SystemsDto systemsDto) {
        this();
        this.id = systemsDto.getId();
        update(systemsDto);
    }

    public void update(SystemsDto systemsDto) {
        this.name = systemsDto.getName();
        this.version = systemsDto.getVersion();
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<Roles> getRoles() {
        return roles;
    }

    public void setRoles(List<Roles> roles) {
        this.roles = roles;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Systems))
            return false;
        Systems other = (Systems) obj;
        return id != null && id.equals(other.id);
    }

    @Override
    public String toString() {
        return "Systems{" + "id=" + id + '}';
    }
}
