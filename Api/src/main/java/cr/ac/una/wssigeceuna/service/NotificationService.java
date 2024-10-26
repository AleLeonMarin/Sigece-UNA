package cr.ac.una.wssigeceuna.service;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.model.Notifications;
import cr.ac.una.wssigeceuna.model.NotificationsDto;
import cr.ac.una.wssigeceuna.model.Variables;
import cr.ac.una.wssigeceuna.model.VariablesDto;
import cr.ac.una.wssigeceuna.util.CodigoRespuesta;
import cr.ac.una.wssigeceuna.util.Respuesta;

@Stateless
@LocalBean
public class NotificationService {

    private static final Logger LOG = Logger.getLogger(NotificationService.class.getName());

    @PersistenceContext(unitName = "SigeceUnaWsPU")
    private EntityManager em;

    public Respuesta getNotification(Long id) {
        try {
            Query qryNotificacion = em.createNamedQuery("Notifications.findById", Notifications.class);
            qryNotificacion.setParameter("id", id);

            Notifications notification = (Notifications) qryNotificacion.getSingleResult();
            em.refresh(notification);
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Notificacion",
                    new NotificationsDto(notification));

        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                    "No existe una notificación con el código ingresado.",
                    "obtenerNotificacionPorId NoResultException");
        } catch (NonUniqueResultException ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar la notificación.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar la notificación.",
                    "obtenerNotificacionPorId NonUniqueResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar la notificación.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar la notificación.",
                    "obtenerNotificacionPorId " + ex.getMessage());
        }
    }

    public Respuesta saveNotification(NotificationsDto notificationDto) {
        try {
            Notifications notification;

            if (notificationDto.getId() != null && notificationDto.getId() > 0) {
                notification = em.find(Notifications.class, notificationDto.getId());
                if (notification != null) {
                    notification.update(notificationDto);

                    List<Variables> currentVariables = notification.getVariables();
                    List<Variables> updatedVariables = new ArrayList<>();

                    for (VariablesDto variableDto : notificationDto.getVariables()) {
                        if (variableDto.getName() == null || variableDto.getName().isEmpty()) {
                            LOG.log(Level.SEVERE, "Variable name is null or empty. Skipping variable with ID: {0}", variableDto.getId());
                            continue; // Ignora la variable si no tiene nombre
                        }

                        Variables variable;
                        if (variableDto.getId() != null) {
                            variable = em.find(Variables.class, variableDto.getId());
                            if (variable != null) {
                                variable.update(variableDto);
                                variable.setNotifications(notification); // Asegura la relación
                                em.merge(variable);
                                updatedVariables.add(variable);
                            }
                        } else {
                            variable = new Variables(variableDto);
                            variable.setNotifications(notification); // Asigna la notificación al crear la variable
                            em.persist(variable);
                            updatedVariables.add(variable);
                        }
                    }

                    // Eliminar variables obsoletas
                    currentVariables.stream()
                            .filter(var -> updatedVariables.stream().noneMatch(updVar -> updVar.getId().equals(var.getId())))
                            .forEach(var -> em.remove(var));

                    notification.setVariables(updatedVariables);
                    notification = em.merge(notification);
                } else {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                            "No se encontró la notificación a actualizar.", "saveNotification NoResultException");
                }
            } else {
                notification = new Notifications(notificationDto);
                List<Variables> variablesList = new ArrayList<>();
                for (VariablesDto variableDto : notificationDto.getVariables()) {
                    if (variableDto.getName() == null || variableDto.getName().isEmpty()) {
                        LOG.log(Level.SEVERE, "Variable name is null or empty. Skipping variable with ID: {0}", variableDto.getId());
                        continue; // Ignora la variable si no tiene nombre
                    }

                    Variables variable = new Variables(variableDto);
                    variable.setNotifications(notification); // Asigna la notificación al crear la variable
                    em.persist(variable);
                    variablesList.add(variable);
                }
                notification.setVariables(variablesList);
                em.persist(notification);
            }
            em.flush();

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Notificacion", new NotificationsDto(notification));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al guardar la notificación.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al guardar la notificación.", "guardarNotificacion " + ex.getMessage());
        }
    }

    public Respuesta deleteNotification(Long id) {
        try {
            Notifications notification = em.find(Notifications.class, id);
            if (notification == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                        "No se encontró la notificación a eliminar.", "deleteNotification NoResultException");
            }
            em.remove(notification);
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "Notificación eliminada correctamente.", "");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al eliminar la notificación.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al eliminar la notificación.",
                    "deleteNotification " + ex.getMessage());
        }
    }

    public Respuesta getAll() {
        try {
            Query qryNotificaciones = em.createNamedQuery("Notifications.findAll", Notifications.class);
            List<Notifications> notifications = qryNotificaciones.getResultList();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Notificaciones", notifications);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar las notificaciones.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Error al consultar las notificaciones.",
                    "getAll " + ex.getMessage());
        }
    }
}
