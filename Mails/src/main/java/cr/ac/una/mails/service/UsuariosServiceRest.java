package cr.ac.una.mails.service;

import cr.ac.una.mails.model.UsersDto;

import cr.ac.una.mails.util.Request;
import cr.ac.una.mails.util.Respuesta;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuariosServiceRest {

    public Respuesta actualizarEstadoUsuario(UsersDto usuarioDto) {
        try {
            Request request = new Request("UsersController/actualizarEstado");
            request.put(usuarioDto);

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            UsersDto usuarioActualizado = (UsersDto) request.readEntity(UsersDto.class);
            return new Respuesta(true, "", "", "Usuario", usuarioActualizado);

        } catch (Exception ex) {
            Logger.getLogger(UsersService.class.getName()).log(Level.SEVERE, "Error actualizando el estado del usuario.", ex);
            return new Respuesta(false, "Error actualizando el estado del usuario.", "actualizarEstadoUsuario " + ex.getMessage());
        }
    }

}
