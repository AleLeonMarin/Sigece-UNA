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
                // Buscar la notificación existente
                notification = em.find(Notifications.class, notificationDto.getId());
                if (notification == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                            "No se encontró la notificación a modificar.", "saveNotification NoResultException");
                }

                // Actualizar la notificación con datos nuevos
                notification.update(notificationDto);

                // Eliminar variables
                /*
                for (VariablesDto varDto : notificationDto.getEliminatedVariables()) {
                    Variables variable = em.find(Variables.class, varDto.getId());
                    if (variable != null) {
                        variable.setNotifications(null);
                        notification.getVariables().remove(variable);
                        em.remove(variable);
                    }
                    LOG.log(Level.INFO, "Variable eliminada: {0}", varDto.getId());
                }
                 */
                // Agregar o actualizar variables
                for (VariablesDto varDto : notificationDto.getVariables()) {
                    Variables variable;

                    if (varDto.getId() != null && varDto.getId() > 0) {
                        // Si existe el ID, intenta obtener la variable de la base de datos
                        variable = em.find(Variables.class, varDto.getId());
                        if (variable == null) {
                            LOG.log(Level.WARNING, "La variable con ID {0} no se encontró y no se puede actualizar", varDto.getId());
                            continue; // Saltar si no se encuentra
                        }
                        variable.update(varDto); // Actualiza si existe
                        em.merge(variable);
                    } else {
                        // Si no tiene ID, es una nueva variable
                        variable = new Variables(varDto);
                        variable.setNotifications(notification);
                        em.persist(variable);
                        notification.getVariables().add(variable);
                    }
                    LOG.log(Level.INFO, "Variable procesada con ID: {0}", variable.getId());
                }

                // Confirmar la actualización de la notificación en la base de datos
                notification = em.merge(notification);
            } else {
                // Crear una nueva notificación
                notification = new Notifications(notificationDto);
                em.persist(notification);

                // Asociar y guardar nuevas variables
                for (VariablesDto varDto : notificationDto.getVariables()) {
                    Variables variable = new Variables(varDto);
                    variable.setNotifications(notification);
                    em.persist(variable);
                    notification.getVariables().add(variable);
                }
            }

            // Confirmar todos los cambios en la base de datos
            em.flush();
            LOG.log(Level.INFO, "Notificación guardada correctamente: {0}", notification);

            // Retornar respuesta exitosa
            return new Respuesta(true, CodigoRespuesta.CORRECTO,
                    "La notificación se guardó correctamente", "", "Notification",
                    new NotificationsDto(notification));
        } catch (NoResultException ex) {
            LOG.log(Level.SEVERE, "Notificación no encontrada.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                    "No se encontró la notificación a modificar.", "saveNotification " + ex.getMessage());
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al guardar la notificación.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error al guardar la notificación.", "saveNotification " + ex.getMessage());
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
