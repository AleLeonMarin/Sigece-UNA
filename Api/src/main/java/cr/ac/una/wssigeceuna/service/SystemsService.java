package cr.ac.una.wssigeceuna.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.model.Roles;
import cr.ac.una.wssigeceuna.model.RolesDto;
import cr.ac.una.wssigeceuna.model.Systems;
import cr.ac.una.wssigeceuna.model.SystemsDto;
import cr.ac.una.wssigeceuna.util.CodigoRespuesta;
import cr.ac.una.wssigeceuna.util.Respuesta;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.Objects;

@Stateless
@LocalBean
public class SystemsService {

    private static final Logger LOG = Logger.getLogger(SystemsService.class.getName());
    @PersistenceContext(unitName = "SigeceUnaWsPU")
    private EntityManager em;

    public Respuesta getSystems() {

        try {
            Query query = em.createNamedQuery("Systems.findAll", Systems.class);
            List<Systems> system = query.getResultList();
            List<SystemsDto> systemsDto = new ArrayList<>();
            for (Systems systems : system) {
                SystemsDto systemDto = new SystemsDto(systems);
                systemsDto.add(systemDto);
            }

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Sistemas", systemsDto);
        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "", "getSistemas NoResultException", ex);
        } catch (NonUniqueResultException ex) {
            Logger.getLogger(SystemsService.class.getName()).log(Level.SEVERE,
                    "Ocurrio un error al consultar los sistemas.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "", "getSistemas NonUniqueResultException");
        } catch (Exception ex) {
            Logger.getLogger(SystemsService.class.getName()).log(Level.SEVERE,
                    "Ocurrio un error al consultar los sistemas.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "", "getSistemas NonUniqueResultException");
        }
    }

    public Respuesta deleteSystem(Long id) {
        try {
            Systems sistema;
            if (id != null && id > 0) {
                sistema = em.find(Systems.class, id);
                if (sistema == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                            "No se encontro el sistema a eliminar.", "deleteSystem NoResultException");
                }

                em.remove(sistema);
            } else {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "Debe cargar el sistema a eliminar.",
                        "deleteSystem NoResultException");
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrio un error al eliminar el sistema.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrio un error al eliminar el sistema.",
                    "deleteSystem " + ex.getMessage());
        }
    }

    public Respuesta saveSystem(SystemsDto sistemasDto) {
        try {
            Systems systems;

            // Modificar sistema existente
            if (sistemasDto.getId() != null && sistemasDto.getId() > 0) {
                systems = em.find(Systems.class, sistemasDto.getId());
                if (systems == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                            "No se encontró el sistema a modificar.", "saveSystem NoResultException");
                }

                // Actualiza el sistema con los datos del DTO
                systems.update(sistemasDto);
                systems = em.merge(systems); // Merge para actualizar el sistema
            }
            // Crear un nuevo sistema
            else {
                systems = new Systems(sistemasDto);
                em.persist(systems); // Persistir el nuevo sistema
            }

            em.flush(); // Asegura que los cambios se escriban a la base de datos
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Sistema", new SystemsDto(systems));

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al guardar el sistema.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al guardar el sistema.",
                    "saveSystem " + ex.getMessage());
        }
    }

    public Respuesta getSystem(Long id) {
        try {
            Query query = em.createNamedQuery("Systems.findById", Systems.class);
            query.setParameter("id", id);
            Systems sistemas = (Systems) query.getSingleResult();
            SystemsDto sistemasDto = new SystemsDto(sistemas);

            for (Roles rol : sistemas.getRoles()) {
                sistemasDto.getRoles().add(new RolesDto(rol));
            }

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Sistema", sistemasDto);
        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "", "getSystem NoResultException", ex);
        } catch (NonUniqueResultException ex) {
            Logger.getLogger(SystemsService.class.getName()).log(Level.SEVERE,
                    "Ocurrio un error al consultar los sistemas.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "", "getSystem NonUniqueResultException");
        } catch (Exception ex) {
            Logger.getLogger(SystemsService.class.getName()).log(Level.SEVERE,
                    "Ocurrio un error al consultar los sistemas.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "", "getSystem NonUniqueResultException");
        }
    }

}
