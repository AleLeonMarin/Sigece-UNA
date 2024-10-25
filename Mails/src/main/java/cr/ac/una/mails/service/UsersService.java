package cr.ac.una.mails.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import cr.ac.una.mails.model.UsersDto;
import cr.ac.una.mails.util.Request;
import cr.ac.una.mails.util.Respuesta;
import java.util.HashMap;
import java.util.Map;



public class UsersService {

     private static final Logger LOG = Logger.getLogger(UsersService.class.getName());

    public Respuesta logIn(String user, String password) {
        try {

            Map<String,Object> params = new HashMap<>();
            params.put("user", user);
            params.put("password", password);
            Request request = new Request("UsersController/logIn","/{user}/{password}",params);
            request.get();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            UsersDto userDto = (UsersDto) request.readEntity(UsersDto.class);
            return new Respuesta(true, "", "", "Usuario", userDto);

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error iniciando sesión.", ex);
            return new Respuesta(false, "Error iniciando sesión.", "logIn " + ex.getMessage());
        }
    }

   public Respuesta guardarUsuario(UsersDto userDto) {
        try {
            Request request = new Request("UsersController/user");
            request.post(userDto);

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            UsersDto usuarioGuardado = (UsersDto) request.readEntity(UsersDto.class);
            return new Respuesta(true, "", "", "Usuario", usuarioGuardado);

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error guardando el usuario.", ex);
            return new Respuesta(false, "Error guardando el usuario.", "guardarUsuario " + ex.getMessage());
        }
    }

}