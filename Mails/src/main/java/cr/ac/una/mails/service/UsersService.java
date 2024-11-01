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
            request.getToken();

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

    public Respuesta getByPass(String password) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("password", password);

            Request request = new Request("UsersController/getByPass", "/{password}", parametros);
            request.get();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            UsersDto user = (UsersDto) request.readEntity(UsersDto.class);
            return new Respuesta(true, "", "", "Usuario", user);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error obteniendo el usuario por contraseña.", ex);
            return new Respuesta(false, "Error obteniendo el usuario por contraseña.", "getByPass " + ex.getMessage());
        }
    }

    public Respuesta updatePassword(String email, String newPass) {
        try {
            UsersDto usuarioDto = new UsersDto();
            usuarioDto.setEmail(email);
            usuarioDto.setPassword(newPass);

            Request request = new Request("UsersController/updatePasswordByEmail");

            request.put(usuarioDto);

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            return new Respuesta(true, "", "", "Contraseña actualizada correctamente.", "");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error actualizando la contraseña.", ex);
            return new Respuesta(false, "Error actualizando la contraseña.", "updatePassword " + ex.getMessage());
        }
    }

    public Respuesta getByMail(String mail) {

        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("mail", mail);

            Request request = new Request("UsersController/getByMail", "/{mail}", parametros);
            request.get();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            UsersDto user = (UsersDto) request.readEntity(UsersDto.class);
            return new Respuesta(true, "", "", "Usuario", user);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error obteniendo el usuario por correo.", ex);
            return new Respuesta(false, "Error obteniendo el usuario por correo.", "getByMail " + ex.getMessage());

        }
    }

    public Respuesta renovarToken() {
        try {
            Request request = new Request("UsersController/renovar");
            request.getRenewal();
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }
            String token = (String) request.readEntity(String.class);
            return new Respuesta(true, "", "", "Token", token);
        } catch (Exception ex) {
            Logger.getLogger(UsersService.class.getName()).log(Level.SEVERE, "Error obteniendo el token", ex);
            return new Respuesta(false, "Error renovando el token.", "renovarToken " + ex.getMessage());
        }
    }

}