package cr.ac.una.chats.service;

import cr.ac.una.chats.model.MessagesDto;
import cr.ac.una.chats.util.Request;
import cr.ac.una.chats.util.Respuesta;
import jakarta.ws.rs.core.GenericType;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MensajesService {

    public Respuesta guardarMensaje(MessagesDto mensajeDto) {
        try {
            Request request = new Request("messages/messages");
            request.post(mensajeDto);

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            MessagesDto mensajeGuardado = (MessagesDto) request.readEntity(MessagesDto.class);

            return new Respuesta(true, "", "", "Mensaje", mensajeGuardado);
        } catch (Exception ex) {
            Logger.getLogger(MensajesService.class.getName()).log(Level.SEVERE, "Ocurrió un error al guardar el mensaje.", ex);
            return new Respuesta(false, "Ocurrió un error al guardar el mensaje.", "guardarMensaje " + ex.getMessage());
        }
    }

    public Respuesta eliminarMensaje(Long id) {
        try {
            Request request = new Request("messages/delete/" + id);
            request.delete();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            return new Respuesta(true, "", "");
        } catch (Exception ex) {
            Logger.getLogger(MensajesService.class.getName()).log(Level.SEVERE, "Ocurrió un error al eliminar el mensaje.", ex);
            return new Respuesta(false, "Ocurrió un error al eliminar el mensaje.", "eliminarMensaje " + ex.getMessage());
        }
    }

    public Respuesta getMensajesByChat(Long chatId) {
        try {
            Request request = new Request("messages/getByChat/" + chatId);
            request.get();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            List<MessagesDto> mensajes = (List<MessagesDto>) request.readEntity(new GenericType<List<MessagesDto>>() {});

            return new Respuesta(true, "", "", "Mensajes", mensajes);
        } catch (Exception ex) {
            Logger.getLogger(MensajesService.class.getName()).log(Level.SEVERE, "Ocurrió un error al obtener los mensajes del chat.", ex);
            return new Respuesta(false, "Ocurrió un error al obtener los mensajes del chat.", "getMensajesByChat " + ex.getMessage());
        }
    }

    public Respuesta descargarArchivo(Long mensajeId) {
        try {
            Request request = new Request("messages/download/" + mensajeId);
            request.get();  // Realizamos la solicitud GET para descargar el archivo

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            MessagesDto mensajeDto = (MessagesDto) request.readEntity(MessagesDto.class); // Lee directamente como MessagesDto

            return new Respuesta(true, "", "", "ArchivoAdjunto", mensajeDto);
        } catch (Exception ex) {
            Logger.getLogger(MensajesService.class.getName()).log(Level.SEVERE, "Ocurrió un error al descargar el archivo adjunto.", ex);
            return new Respuesta(false, "Ocurrió un error al descargar el archivo adjunto.", "descargarArchivo " + ex.getMessage());
        }
    }



}


