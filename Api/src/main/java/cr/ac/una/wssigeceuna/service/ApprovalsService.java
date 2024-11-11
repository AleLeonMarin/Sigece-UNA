package cr.ac.una.wssigeceuna.service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.model.Approvals;
import cr.ac.una.wssigeceuna.model.ApprovalsDto;
import cr.ac.una.wssigeceuna.model.FollowsDto;
import cr.ac.una.wssigeceuna.model.Gestions;
import cr.ac.una.wssigeceuna.model.Users;
import cr.ac.una.wssigeceuna.util.CodigoRespuesta;
import cr.ac.una.wssigeceuna.util.Respuesta;
import jakarta.ejb.EJB;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
@LocalBean
public class ApprovalsService {

    private static final Logger LOGGER = Logger.getLogger(ApprovalsService.class.getName());

    @PersistenceContext(unitName = "SigeceUnaWsPU")
    private EntityManager em;

    @EJB
    FollowsService followService;

    @EJB
    private GestionService gestionService; // Inyectamos GestionService para actualizar el estado de la gestión

    public Respuesta createApprovals(ApprovalsDto approvals) {
        try {
            Approvals approval;
            List<String> changes = new ArrayList<>();

            if (approvals.getId() != null && approvals.getId() > 0) {
                approval = em.find(Approvals.class, approvals.getId());
                if (approval == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                            "No se encontró la aprobación a modificar",
                            "createApprovals No se encontró la aprobación a modificar");
                }

                boolean modified = approvals.getId() != null && approvals.getId() > 0;
                if (modified) {
                    if (approvals.getDescription() != null
                            && !approvals.getDescription().equals(approval.getDescription())) {
                        changes.add("Se modificó la descripción de la aprobación");
                    }
                    if (approvals.getDate() != null && !approvals.getDate().equals(approval.getDate())) {
                        changes.add("Se modificó la fecha de aprobación");
                    }
                    if (approvals.getState() != null && !approvals.getState().equals(approval.getState())) {
                        changes.add("El usuario: " + approvals.getUsers().getName()
                                + " modificó el estado de la aprobación");
                    }
                    if (approvals.getSolution() != null && !approvals.getSolution().equals(approval.getSolution())) {
                        changes.add("Se modificó la solución de la aprobación");
                    }
                    if (approvals.getComment() != null && !approvals.getComment().equals(approval.getComentary())) {
                        changes.add("Se modificó el comentario de la aprobación");
                    }
                }
                approval.update(approvals);

                approval = em.merge(approval);

                if (!changes.isEmpty()) {
                    FollowsDto follow = new FollowsDto();
                    follow.setDescription("Se ha realizado una modificación en la aprobación");
                    follow.setGestions(approvals.getGestion());
                    follow.setUsers(approvals.getUsers());
                    follow.setArchive(
                            ("Cambios automaticos realizados: " + String.join(", ", changes))
                                    .getBytes(StandardCharsets.UTF_8));
                    followService.createFollow(follow);
                }
            } else {
                approval = new Approvals(approvals);
                approval.setUser(em.find(Users.class, approvals.getUsers().getId()));
                approval.setGestion(em.find(Gestions.class, approvals.getGestion().getId()));

                changes.add("Se ha creado una nueva aprobación");

                em.persist(approval);

                if (!changes.isEmpty()) {
                    FollowsDto follow = new FollowsDto();
                    follow.setDescription("Se ha creado una nueva aprobación");
                    follow.setGestions(approvals.getGestion());
                    follow.setUsers(approvals.getUsers());
                    follow.setState("S");
                    follow.setDate(approvals.getDate());
                    follow.setArchive(
                            ("Cambios automaticos realizados: " + String.join(", ", changes))
                                    .getBytes(StandardCharsets.UTF_8));
                    followService.createFollow(follow);
                }
            }

            Respuesta respuesta = gestionService.updateGestionStatus(approvals.getGestion().getId());
            if (!respuesta.getEstado()) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                        "No se encontró la gestión a actualizar",
                        "deleteGestion No se encontró la gestión a actualizar");
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "approval", new ApprovalsDto(approval));
        } catch (Exception e) {
            LOGGER.severe("Error creando/modificando la aprobación: " + e.getMessage());
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO,
                    "Error creando/modificando la aprobación", "createApprovals " + e.getMessage());
        }
    }

    public Respuesta getApproval(Long id) {
        try {
            Approvals approval = em.find(Approvals.class, id);
            if (approval == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                        "No se encontró la aprobación", "getApproval No se encontró la aprobación");
            }
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "approval", new ApprovalsDto(approval));
        } catch (Exception e) {
            LOGGER.severe("Error obteniendo la aprobación: " + e.getMessage());
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO,
                    "Error obteniendo la aprobación", "getApproval " + e.getMessage());
        }
    }

    public Respuesta getApprovals() {
        try {
            List<Approvals> approvals = em.createNamedQuery("Approvals.findAll", Approvals.class).getResultList();
            List<ApprovalsDto> approvalsDto = new ArrayList<>();
            approvals.forEach(approval -> {
                approvalsDto.add(new ApprovalsDto(approval));
            });
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "approvals", approvalsDto);
        } catch (Exception e) {
            LOGGER.severe("Error obteniendo las aprobaciones: " + e.getMessage());
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO,
                    "Error obteniendo las aprobaciones", "getApprovals " + e.getMessage());
        }
    }
}
