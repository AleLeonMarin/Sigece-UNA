package cr.ac.una.chats.service;

import cr.ac.una.chats.model.ChatsDto;
import cr.ac.una.chats.model.UsersDto;
import cr.ac.una.chats.util.Request;
import cr.ac.una.chats.util.Respuesta;
import jakarta.ws.rs.core.GenericType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatsService {

    public Respuesta getUsuarios() {
        try {
            Request request = new Request("UsersController/users");
            request.get();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            List<UsersDto> usuariosList = (List< UsersDto>) request.readEntity(new GenericType<List<UsersDto>>() {});

            return new Respuesta(true, "", "", "Usuarios", usuariosList);
        } catch (Exception ex) {
            Logger.getLogger(ChatsService.class.getName()).log(Level.SEVERE, "Error obteniendo la lista de usuarios.", ex);
            return new Respuesta(false, "Error obteniendo la lista de usuarios.", "getUsuarios " + ex.getMessage());
        }
    }

    public Respuesta getChatsEntreUsuarios(Long idEmisor, Long idReceptor) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("idEmisor", idEmisor);
            parametros.put("idReceptor", idReceptor);

            Request request = new Request("ChatsController/chats", "/{idEmisor}/{idReceptor}", parametros);
            request.get();

            List<ChatsDto> chatsList = (List<ChatsDto>) request.readEntity(new GenericType<List<ChatsDto>>() {});

            return new Respuesta(true, "", "", "Chats", chatsList);
        } catch (Exception ex) {
            Logger.getLogger(ChatsService.class.getName()).log(Level.SEVERE, "Error obteniendo los chats entre usuarios [idEmisor: " + idEmisor + ", idReceptor: " + idReceptor + "]", ex);
            return new Respuesta(false, "Error obteniendo los chats entre usuarios.", "getChatsEntreUsuarios " + ex.getMessage());
        }
    }


    public Respuesta guardarChat(ChatsDto chatDto) {
        try {
            Request request = new Request("ChatsController/chat");
            request.post(chatDto);

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            ChatsDto chatGuardado = (ChatsDto) request.readEntity(ChatsDto.class);
            return new Respuesta(true, "", "", "Chat", chatGuardado);
        } catch (Exception ex) {
            Logger.getLogger(ChatsService.class.getName()).log(Level.SEVERE, "Error guardando el chat.", ex);
            return new Respuesta(false, "Error guardando el chat.", "guardarChat " + ex.getMessage());
        }
    }

    public Respuesta eliminarChat(String idChat) {
        try {
            Request request = new Request("ChatsController/chat/" + idChat);
            request.delete();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            return new Respuesta(true, "", "", "Chat eliminado correctamente.", null);
        } catch (Exception ex) {
            Logger.getLogger(ChatsService.class.getName()).log(Level.SEVERE, "Error eliminando el chat con ID: " + idChat, ex);
            return new Respuesta(false, "Error eliminando el chat.", "eliminarChat " + ex.getMessage());
        }
    }
}
