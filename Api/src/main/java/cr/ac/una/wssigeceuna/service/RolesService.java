package cr.ac.una.wssigeceuna.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.model.Roles;
import cr.ac.una.wssigeceuna.model.RolesDto;
import cr.ac.una.wssigeceuna.model.Systems;
import cr.ac.una.wssigeceuna.util.CodigoRespuesta;
import cr.ac.una.wssigeceuna.util.Respuesta;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@LocalBean
@Stateless
public class RolesService {

    private static final Logger LOG = Logger.getLogger(RolesService.class.getName());
    @PersistenceContext(unitName = "SigeceUnaWsPU")
    private EntityManager em;

    public Respuesta saveRol(RolesDto rolesDto) {
        try {
            Roles roles;

            // Validación inicial de system en rolesDto
            if (rolesDto.getSystem() == null || rolesDto.getSystem().getId() == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE,
                        "Debe asociar un sistema válido al rol.", "saveRol NullPointerException");
            }

            // Verificar si estamos en modo de actualización o creación
            if (rolesDto.getId() != null && rolesDto.getId() > 0) {
                // Modo actualización
                roles = em.find(Roles.class, rolesDto.getId());
                if (roles == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                            "No se encontró el rol a modificar.", "saveRol NoResultException");
                }

                roles.update(rolesDto); // Actualizar datos del rol desde DTO
                roles = em.merge(roles);
            } else {
                // Modo creación
                roles = new Roles(rolesDto);
                em.persist(roles);
            }

            // Validación y asociación de sistema al rol
            Systems sistemaEntity = em.find(Systems.class, rolesDto.getSystem().getId());
            if (sistemaEntity == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                        "No se encontró el sistema asociado al rol.", "saveRol NoResultException");
            }

            // Verificar que el rol esté asociado al sistema
            if (!sistemaEntity.getRoles().contains(roles)) {
                sistemaEntity.getRoles().add(roles); // Asociar rol al sistema
                em.merge(sistemaEntity); // Guardar cambios en el sistema
            }

            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Rol", new RolesDto(roles));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al guardar el rol.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al guardar el rol.",
                    "saveRol " + ex.getMessage());
        }
    }

    public Respuesta getRoles() {
        try {
            List<Roles> roles = em.createNamedQuery("Roles.findAll", Roles.class).getResultList();
            List<RolesDto> rolesDto = new ArrayList<>();
            for (Roles rol : roles) {
                rolesDto.add(new RolesDto(rol));
            }
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Roles", rolesDto);
        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "", "getRoles NoResultException", ex);
        } catch (NonUniqueResultException ex) {
            LOG.log(Level.SEVERE, "Ocurrio un error al consultar los roles.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "", "getRoles NonUniqueResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrio un error al consultar los roles.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "", "getRoles NonUniqueResultException");
        }
    }

    public Respuesta deleteRol(Long id) {
        try {
            Roles roles;
            if (id != null && id > 0) {
                roles = em.find(Roles.class, id);
                if (roles == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró el rol a eliminar.",
                            "deleteRol NoResultException");
                }
                Systems sistema = em.find(Systems.class, roles.getSystem().getId());
                sistema.getRoles().remove(roles);
                em.merge(sistema);
                em.remove(roles);
            } else {
                return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE, "Debe indicar el id del rol a eliminar.",
                        "deleteRol NoResultException");
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Rol", new RolesDto(roles));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrio un error al eliminar el rol.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrio un error al eliminar el rol.",
                    "deleteRol " + ex.getMessage());
        }
    }

    public Respuesta getRol(Long id) {
        try {
            Query query = em.createNamedQuery("Roles.findById", Roles.class);
            query.setParameter("id", id);
            Roles roles = (Roles) query.getSingleResult();
            RolesDto rolesDto = new RolesDto(roles);
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Rol", rolesDto);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrio un error al obtener el rol.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrio un error al obtener el rol.",
                    "getRol " + ex.getMessage());
        }
    }

}