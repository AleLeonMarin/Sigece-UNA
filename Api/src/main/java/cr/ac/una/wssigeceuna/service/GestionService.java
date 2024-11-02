package cr.ac.una.wssigeceuna.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.model.Follows;
import cr.ac.una.wssigeceuna.model.FollowsDto;
import cr.ac.una.wssigeceuna.model.Gestions;
import cr.ac.una.wssigeceuna.model.GestionsDto;
import cr.ac.una.wssigeceuna.model.Users;
import cr.ac.una.wssigeceuna.model.UsersDto;
import cr.ac.una.wssigeceuna.util.CodigoRespuesta;
import cr.ac.una.wssigeceuna.util.Respuesta;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Stateless
@LocalBean
public class GestionService {

    private static final Logger LOG = Logger.getLogger(GestionService.class.getName());
    @PersistenceContext(unitName = "SigeceUnaWsPU")
    private EntityManager em;

    public Respuesta createGestion(GestionsDto gestion) {
        try {
            Gestions gestions;
            if (gestion.getId() != null || gestion.getId() > 0) {
                gestions = em.find(Gestions.class, gestion.getId());
                if (gestions == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                            "No se encontró la gestión a modificar",
                            "createGestion No se encontró la gestión a modificar");
                }
                gestions.update(gestion);

                for (UsersDto users : gestion.getDeletedApprovers()) {
                    Users userDeleted = em.find(Users.class, users.getId());
                    if (userDeleted != null) {
                        userDeleted.getApprovers().remove(gestions);
                        gestions.getApprovers().remove(userDeleted);
                        em.merge(userDeleted);
                        em.merge(gestions);
                    }
                }
                if (!gestions.getApprovers().isEmpty()) {
                    for (UsersDto u : gestion.getApprovers()) {
                        Users user = em.find(Users.class, u.getId());
                        if (user != null) {
                            user.getApprovers().add(gestions);
                            gestions.getApprovers().add(user);
                            em.merge(user);
                            em.merge(gestions);
                        }
                    }
                }

                gestions = em.merge(gestions);
            } else {
                gestions = new Gestions(gestion);
                em.persist(gestions);

                for (UsersDto u : gestion.getApprovers()) {

                    Users user = em.find(Users.class, u.getId());

                    if (user != null) {
                        user.getApprovers().add(gestions);
                        gestions.getApprovers().add(user);
                        em.merge(user);
                        em.merge(gestions);
                    }

                }
            }

            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Gestion", new GestionsDto(gestions));
        } catch (NoResultException ex) {
            LOG.log(Level.SEVERE, "Gestion no encontrada.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                    "No se encontró la gestion a modificar.",
                    "createGestion " + ex.getMessage());
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al guardar la gestion.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error al guardar la gestion.",
                    "createGestion " + ex.getMessage());
        }

    }

    public Respuesta getGestiones() {
        try {
            Query query = em.createNamedQuery("Gestions.findAll", Gestions.class);
            List<Gestions> gestions = query.getResultList();
            List<GestionsDto> gestionsDto = new ArrayList<>();

            for (Gestions g : gestions) {
                GestionsDto dto = new GestionsDto(g);
                gestionsDto.add(dto);
            }

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Gestiones", query.getResultList());
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar las gestiones.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error al consultar las gestiones.",
                    "getGestiones " + ex.getMessage());
        }
    }

    public Respuesta getGestion(Long id) {
        try {
            Query query = em.createNamedQuery("Gestions.findById", Gestions.class);
            query.setParameter("id", id);
            Gestions gestions = (Gestions) query.getSingleResult();
            GestionsDto dto = new GestionsDto(gestions);

            for (Follows f : gestions.getFollows()) {
                dto.getFollows().add(new FollowsDto(f));
            }

            for (Users u : gestions.getApprovers()) {
                dto.getApprovers().add(new UsersDto(u));
            }
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Gestion", new GestionsDto(gestions));
        } catch (NoResultException ex) {
            LOG.log(Level.SEVERE, "Gestion no encontrada.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                    "No se encontró la gestión a modificar.",
                    "getGestion " + ex.getMessage());
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar la gestión.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error al consultar la gestión.",
                    "getGestion " + ex.getMessage());
        }
    }

    public Respuesta deleteGestion(Long id) {
        try {
            Gestions gestions;
            if (id != null && id > 0) {
                gestions = em.find(Gestions.class, id);
                if (gestions == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                            "No se encontró la gestión a eliminar",
                            "deleteGestion No se encontró la gestión a eliminar");
                }
                em.remove(gestions);
            } else {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                        "No se encontró la gestión a eliminar",
                        "deleteGestion No se encontró la gestión a eliminar");
            }
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "", "");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al eliminar la gestión.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error al eliminar la gestión.",
                    "deleteGestion " + ex.getMessage());
        }
    }

}