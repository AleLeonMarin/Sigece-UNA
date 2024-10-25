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
                if (notification == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                            "No se encontró la notificación a modificar.", "saveNotification NoResultException");
                }
                notification.update(notificationDto);

                List<Variables> variablesList = new ArrayList<>();
                for (VariablesDto variables : notificationDto.getVariables()) {
                    Variables variable = new Variables(variables);
                    variable.setNotifications(notification);
                    variablesList.add(variable);

                    if (variable.getId() == null) {
                        em.persist(variable);
                    } else {
                        em.merge(variable);
                    }
                }
                notification.setVariables(variablesList);
                notification = em.merge(notification);
            } else {

                notification = new Notifications(notificationDto);

                List<Variables> variablesList = new ArrayList<>();
                for (VariablesDto variables : notificationDto.getVariables()) {
                    Variables variable = new Variables(variables);
                    variable.setNotifications(notification);
                    variablesList.add(variable);
                    em.persist(variable);
                    em.merge(variable);
                }
                notification.setVariables(variablesList);
                em.persist(notification);
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Notificacion",
                    new NotificationsDto(notification)); // Retornar DTO
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al guardar la notificación.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al guardar la notificación.",
                    "guardarNotificacion " + ex.getMessage());
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
