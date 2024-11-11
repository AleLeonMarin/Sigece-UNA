package cr.ac.una.wssigeceuna.service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.jersey.jaxb.internal.XmlCollectionJaxbProvider.App;

import cr.ac.una.wssigeceuna.model.Activities;
import cr.ac.una.wssigeceuna.model.Approvals;
import cr.ac.una.wssigeceuna.model.ApprovalsDto;
import cr.ac.una.wssigeceuna.model.Follows;
import cr.ac.una.wssigeceuna.model.FollowsDto;
import cr.ac.una.wssigeceuna.model.Gestions;
import cr.ac.una.wssigeceuna.model.GestionsDto;
import cr.ac.una.wssigeceuna.model.Subactivities;
import cr.ac.una.wssigeceuna.model.Users;
import cr.ac.una.wssigeceuna.model.UsersDto;
import cr.ac.una.wssigeceuna.util.CodigoRespuesta;
import cr.ac.una.wssigeceuna.util.Respuesta;
import jakarta.ejb.EJB;
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

    @EJB
    FollowsService followService;

    public Respuesta createGestion(GestionsDto gestion) {
        try {
            Gestions gestions;

            List<String> changes = new ArrayList<>();

            if (gestion.getId() != null && gestion.getId() > 0) {
                // Si la gestión ya existe, la buscamos y actualizamos
                gestions = em.find(Gestions.class, gestion.getId());
                if (gestions == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                            "No se encontró la gestión a modificar",
                            "createGestion No se encontró la gestión a modificar");
                }
                if (gestion.getActivity() != null) {
                    gestions.setActivity(em.find(Activities.class, gestion.getActivity().getId()));
                } else if (gestion.getSubactivities() != null) {
                    gestions.setSubactivities(em.find(Subactivities.class, gestion.getSubactivities().getId()));
                }
                gestion.setDeletedApprovers(gestion.getDeletedApprovers());
                boolean modificated = gestion.getId() != null && gestion.getId() > 0;
                gestions.setSolutionDate(gestion.getSolutionDate());
                if (modificated) {
                    if (!gestion.getDescription().equals(gestions.getDescription())) {
                        changes.add("Se realizó un cambio en la descripción de la gestión");
                    }
                    if (!gestion.getCreationDate().equals(gestions.getCreationDate())) {
                        changes.add("Se realizó un cambio en la fecha de inicio de la gestión");
                    }
                    if (!gestion.getSolutionDate().equals(gestions.getSolutionDate())) {
                        changes.add("Se realizó un cambio en la fecha de fin de la gestión");
                    }
                    if (!gestion.getState().equals(gestions.getState())) {
                        changes.add("Se realizó un cambio en el estado de la gestión");
                    }
                    if (gestion.getAssigned().getId() != gestions.getAssigned().getId()) {
                        changes.add("Se realizó un cambio en el usuario asignado a la gestión");
                    }
                    if (gestion.getApprovers().size() != gestions.getApprovers().size()) {
                        changes.add("Se realizó un cambio en los aprobadores de la gestión");
                    }
                    if (!gestion.getSubject().equals(gestions.getSubject())) {
                        changes.add("Se realizó un cambio en el asunto de la gestión");
                    }
                    if (gestion.getActivity() != null && gestions.getActivity() != null
                            && !gestion.getActivity().getId().equals(gestions.getActivity().getId())) {
                        changes.add("Se realizó un cambio en la actividad de la gestión");
                    }
                    if (gestion.getSubactivities() != null && gestions.getSubactivities() != null
                            && !gestion.getSubactivities().getId().equals(gestions.getSubactivities().getId())) {
                        changes.add("Se realizó un cambio en la subactividad de la gestión");
                    }

                }
                gestions.update(gestion);

                // Remover los aprobadores eliminados
                for (UsersDto users : gestion.getDeletedApprovers()) {
                    Users userDeleted = em.find(Users.class, users.getId());
                    if (userDeleted != null) {
                        userDeleted.getApprovers().remove(gestions);
                        gestions.getApprovers().remove(userDeleted);
                        em.merge(userDeleted);
                        em.merge(gestions);
                    }
                }

                // Agregar aprobadores nuevos evitando duplicados
                if (!gestions.getApprovers().isEmpty()) {
                    for (UsersDto u : gestion.getApprovers()) {
                        Users user = em.find(Users.class, u.getId());
                        if (user != null && !gestions.getApprovers().contains(user)) {
                            user.getApprovers().add(gestions);
                            gestions.getApprovers().add(user);
                            em.merge(user);
                            em.merge(gestions);
                        }
                    }
                }

                gestions = em.merge(gestions);

                if (!changes.isEmpty()) {
                    FollowsDto follow = new FollowsDto();
                    follow.setDescription("Modificación de la gestión");
                    follow.setGestions(new GestionsDto(gestions));
                    follow.setUsers(new UsersDto(gestions.getRequester()));
                    follow.setState("S");
                    follow.setArchive(
                            ("Cambios automaticos realizados: " + String.join(", ", changes))
                                    .getBytes(StandardCharsets.UTF_8));
                    followService.createFollow(follow);
                }
            } else {
                // Si es una nueva gestión, la creamos
                gestions = new Gestions(gestion);
                gestions.setRequester(em.find(Users.class, gestion.getRequester().getId()));
                gestions.setAssigned(em.find(Users.class, gestion.getAssigned().getId()));
                if (gestion.getActivity() != null) {
                    gestions.setActivity(em.find(Activities.class, gestion.getActivity().getId()));
                } else if (gestion.getSubactivities() != null) {
                    gestions.setSubactivities(em.find(Subactivities.class, gestion.getSubactivities().getId()));
                }
                gestions.setSolutionDate(gestion.getSolutionDate());

                changes.add("Creación de la gestión");

                em.persist(gestions);

                // Agregar aprobadores evitando duplicados
                for (UsersDto u : gestion.getApprovers()) {
                    Users user = em.find(Users.class, u.getId());
                    if (user != null && !gestions.getApprovers().contains(user)) {
                        user.getApprovers().add(gestions);
                        gestions.getApprovers().add(user);
                        em.merge(user);
                        em.merge(gestions);
                    }
                }

                if (!changes.isEmpty()) {
                    FollowsDto follow = new FollowsDto();
                    follow.setDescription("Creación de la gestión");
                    follow.setGestions(new GestionsDto(gestions));
                    follow.setUsers(new UsersDto(gestions.getRequester()));
                    follow.setState("S");
                    follow.setArchive(
                            ("Cambios realizados: " + String.join(", ", changes)).getBytes(StandardCharsets.UTF_8));
                    followService.createFollow(follow);
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

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Gestiones", gestionsDto);
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

            for (Users u : gestions.getApprovers()) {
                dto.getApprovers().add(new UsersDto(u));
            }

            for (Follows f : gestions.getFollows()) {
                dto.getFollows().add(new FollowsDto(f));
            }

            for (Approvals a : gestions.getApprovals()) {
                dto.getApprovals().add(new ApprovalsDto(a));
            }

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Gestion", dto);
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

    public Respuesta updateGestionStatus(Long gestionId) {
        try {
            // Buscar la gestión por ID
            Gestions gestion = em.find(Gestions.class, gestionId);
            if (gestion == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                        "No se encontró la gestión a actualizar",
                        "deleteGestion No se encontró la gestión a actualizar");
            }

            // Contar aprobaciones por estado
            Query query = em.createQuery(
                    "SELECT a.state, COUNT(a) FROM Approvals a WHERE a.gestion.id = :gestionId GROUP BY a.state");
            query.setParameter("gestionId", gestionId);
            List<Object[]> results = query.getResultList();

            int totalApproved = 0;
            int totalRejected = 0;

            // Procesar los resultados
            for (Object[] result : results) {
                String state = (String) result[0];
                Long count = (Long) result[1];
                if ("A".equals(state)) {
                    totalApproved += count;
                } else if ("R".equals(state)) {
                    totalRejected += count;
                }
            }

            // Número total de aprobadores
            int totalApprovers = gestion.getApprovers().size();

            // Actualizar el estado de la gestión basado en las reglas
            if (totalApproved > totalApprovers / 2) {
                // Mayoría de aprobaciones
                gestion.setState("S");
            } else if (totalRejected > totalApprovers / 2) {
                // Mayoría de rechazos
                gestion.setState("R");
            } else if (totalApproved + totalRejected >= totalApprovers) {
                // Si todos han aprobado o rechazado
                if (totalApproved > totalRejected) {
                    gestion.setState("S");
                } else {
                    gestion.setState("R");
                }
            } else if (totalApproved >= totalApprovers / 2) {
                // Si la mitad o más han aprobado
                gestion.setState("A");
            } else if (totalApproved + totalRejected >= 1) {
                // Si al menos una persona ha aprobado o rechazado
                gestion.setState("E");
            } else {
                // Si no hay aprobaciones o rechazos
                gestion.setState("C");
            }

            // Persistir cambios en la base de datos
            em.merge(gestion);
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Gestion", new GestionsDto(gestion));

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al actualizar el estado de la gestion.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error al actualizar el estado de la gestion.",
                    "updateGestionStatus " + ex.getMessage());
        }
    }
}
