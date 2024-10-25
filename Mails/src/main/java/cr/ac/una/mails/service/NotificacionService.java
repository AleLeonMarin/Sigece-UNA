package cr.ac.una.mails.service;

import cr.ac.una.mails.model.NotificationsDto;
import cr.ac.una.mails.util.Request;
import cr.ac.una.mails.util.Respuesta;
import jakarta.ws.rs.core.GenericType;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificacionService {

    public Respuesta getNotificacionById(Long id) {
        try {
            Request request = new Request("notifications/get/" + id);
            request.get();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            NotificationsDto notificacion = (NotificationsDto) request.readEntity(NotificationsDto.class);
            return new Respuesta(true, "", "", "Notificacion", notificacion);

        } catch (Exception ex) {
            Logger.getLogger(NotificacionService.class.getName()).log(Level.SEVERE, "Error obteniendo la notificación.", ex);
            return new Respuesta(false, "Error obteniendo la notificación.", "getNotificacionById " + ex.getMessage());
        }
    }

    public Respuesta guardarNotificacion(NotificationsDto notificacionDTO) {
        try {
            Request request = new Request("notifications/save");
            request.post(notificacionDTO);

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            NotificationsDto notificacionGuardada = (NotificationsDto) request.readEntity(NotificationsDto.class);
            return new Respuesta(true, "", "", "Notificacion", notificacionGuardada);

        } catch (Exception ex) {
            Logger.getLogger(NotificacionService.class.getName()).log(Level.SEVERE, "Error guardando la notificación.", ex);
            return new Respuesta(false, "Error guardando la notificación.", "guardarNotificacion " + ex.getMessage());
        }
    }


    public Respuesta eliminarNotificacion(Long id) {
        try {
            Request request = new Request("notifications/delete/" + id);
            request.delete();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            return new Respuesta(true, "", "Notificación eliminada exitosamente.");

        } catch (Exception ex) {
            Logger.getLogger(NotificacionService.class.getName()).log(Level.SEVERE, "Error eliminando la notificación.", ex);
            return new Respuesta(false, "Error eliminando la notificación.", "eliminarNotificacion " + ex.getMessage());
        }
    }

    public Respuesta obtenerNotificaciones() {
        try {
            Request request = new Request("notifications/getAll");
            request.get();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            List<NotificationsDto> notificaciones = (List<NotificationsDto>) request.readEntity(new GenericType<List<NotificationsDto>>() {});
            return new Respuesta(true, "", "", "Notificaciones", notificaciones);

        } catch (Exception ex) {
            Logger.getLogger(NotificacionService.class.getName()).log(Level.SEVERE, "Error obteniendo las notificaciones.", ex);
            return new Respuesta(false, "Error obteniendo las notificaciones.", "getAllNotificaciones " + ex.getMessage());
        }
    }
}
